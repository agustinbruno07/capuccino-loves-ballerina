package com.capuccinolovesballerina.game.red.hilos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class HiloServidor extends Thread {

    private static final int PUERTO = 9992;
    private static final int MAX_JUGADORES = 2;

    private DatagramSocket socket;
    private boolean fin = false;

    private final InetSocketAddress[] destino = new InetSocketAddress[MAX_JUGADORES];
    private final boolean[] conectado = new boolean[MAX_JUGADORES];
    private final float[][] pos = new float[MAX_JUGADORES][4];
    private final boolean[] enSalida = new boolean[MAX_JUGADORES];

    private int siguienteRol = 0;

    private final Set<Integer> cortados = new HashSet<>();
    private boolean palanca = false;
    private boolean puerta = false;
    private float espumaX = 0f;
    private float espumaY = 0f;
    private boolean hayEspuma = false;
    private int muerto = -1;
    private boolean victoria = false;

    public HiloServidor() {
        try {
            socket = new DatagramSocket(PUERTO);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!fin) {
            byte[] datos = new byte[1024];
            DatagramPacket dp = new DatagramPacket(datos, datos.length);
            try {
                socket.receive(dp);
                procesarMensaje(dp);
            } catch (IOException e) {
                if (!fin) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void procesarMensaje(DatagramPacket dp) {
        String msg = new String(dp.getData(), 0, dp.getLength());
        String[] p = msg.split("\\|", -1);
        if (p.length == 0) {
            return;
        }
        InetSocketAddress origen = (InetSocketAddress) dp.getSocketAddress();
        String tipo = p[0];

        try {
            switch (tipo) {
                case "JOIN":
                    manejarJoin(origen);
                    break;
                case "POS":
                    manejarPos(origen, p);
                    break;
                case "CORTE":
                    manejarCorte(origen, p);
                    break;
                case "ESPUMA":
                    manejarEspuma(origen, p);
                    break;
                case "PALANCA":
                    manejarPalanca(origen);
                    break;
                case "SALIDA":
                    manejarSalida(origen, p);
                    break;
                case "MUERTE":
                    manejarMuerte(origen);
                    break;
                case "REINICIO":
                    manejarReinicio(origen);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("mensaje invalido: " + msg);
        }
    }

    private int buscarRol(InetSocketAddress origen) {
        for (int i = 0; i < MAX_JUGADORES; i++) {
            if (conectado[i] && origen.equals(destino[i])) {
                return i;
            }
        }
        return -1;
    }

    private void mandar(InetSocketAddress a, String msg) {
        byte[] data = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length, a);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String msg) {
        for (int i = 0; i < MAX_JUGADORES; i++) {
            if (conectado[i]) {
                mandar(destino[i], msg);
            }
        }
    }

    private void manejarJoin(InetSocketAddress origen) {
        int ya = buscarRol(origen);
        if (ya >= 0) {
            mandar(origen, "ROL|" + ya);
            mandarSnapshot(origen, ya);
            return;
        }
        if (siguienteRol >= MAX_JUGADORES) {
            mandar(origen, "LLENO");
            return;
        }
        int rol = siguienteRol++;
        destino[rol] = origen;
        conectado[rol] = true;
        mandar(origen, "ROL|" + rol);
        mandarSnapshot(origen, rol);
    }

    private void mandarSnapshot(InetSocketAddress a, int rol) {
        for (Integer i : cortados) {
            mandar(a, "CORTE|" + i);
        }
        mandar(a, "PALANCA|" + palanca + "|" + puerta);
        if (hayEspuma) {
            mandar(a, "ESPUMA|" + espumaX + "|" + espumaY);
        }
        if (muerto >= 0) {
            mandar(a, "MUERTE|" + muerto);
        }
        if (victoria) {
            mandar(a, "VICTORIA");
        }
        int otro = 1 - rol;
        if (otro >= 0 && otro < MAX_JUGADORES && conectado[otro]) {
            boolean mirandoOtro = pos[otro][2] > 0.5f;
            boolean sueloOtro = pos[otro][3] > 0.5f;
            mandar(a, "POS|" + otro + "|" + pos[otro][0] + "|" + pos[otro][1] + "|" + mirandoOtro + "|" + sueloOtro);
        }
    }

    private void manejarPos(InetSocketAddress origen, String[] p) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        pos[rol][0] = Float.parseFloat(p[1]);
        pos[rol][1] = Float.parseFloat(p[2]);
        pos[rol][2] = Boolean.parseBoolean(p[3]) ? 1f : 0f;
        pos[rol][3] = Boolean.parseBoolean(p[4]) ? 1f : 0f;
        broadcast("POS|" + rol + "|" + p[1] + "|" + p[2] + "|" + p[3] + "|" + p[4]);
    }

    private void manejarCorte(InetSocketAddress origen, String[] p) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        int indice = Integer.parseInt(p[1]);
        cortados.add(indice);
        broadcast("CORTE|" + indice);
    }

    private void manejarEspuma(InetSocketAddress origen, String[] p) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        espumaX = Float.parseFloat(p[1]);
        espumaY = Float.parseFloat(p[2]);
        hayEspuma = true;
        broadcast("ESPUMA|" + espumaX + "|" + espumaY);
    }

    private void manejarPalanca(InetSocketAddress origen) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        palanca = !palanca;
        puerta = palanca;
        broadcast("PALANCA|" + palanca + "|" + puerta);
    }

    private void manejarSalida(InetSocketAddress origen, String[] p) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        enSalida[rol] = Boolean.parseBoolean(p[1]);
        if (!victoria && enSalida[0] && enSalida[1]) {
            victoria = true;
            broadcast("VICTORIA");
        }
    }

    private void manejarMuerte(InetSocketAddress origen) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        muerto = rol;
        broadcast("MUERTE|" + rol);
    }

    private void manejarReinicio(InetSocketAddress origen) {
        int rol = buscarRol(origen);
        if (rol < 0) {
            return;
        }
        cortados.clear();
        palanca = false;
        puerta = false;
        hayEspuma = false;
        muerto = -1;
        victoria = false;
        enSalida[0] = false;
        enSalida[1] = false;
        broadcast("REINICIO");
    }

    public void detener() {
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
