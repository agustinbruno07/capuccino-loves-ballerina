package com.capuccinolovesballerina.game.red;

import com.capuccinolovesballerina.game.red.hilos.HiloCliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private static final int PUERTO_DESCUBRIMIENTO = 9993;
    private static final int TIMEOUT_DESCUBRIMIENTO = 2000;
    private static final int TIMEOUT_ROL = 3000;

    private HiloCliente hc;
    private int miRol = -1;
    private String ipServer;
    private int puertoServer;

    private final List<String> pendientes = new ArrayList<>();

    public boolean conectar() {
        if (!descubrirServidor()) {
            return false;
        }
        hc = new HiloCliente(ipServer, puertoServer);
        hc.start();
        hc.enviarMensaje("JOIN");
        return esperarRol();
    }

    private boolean descubrirServidor() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.setSoTimeout(TIMEOUT_DESCUBRIMIENTO);

            byte[] pedido = "DISCOVER".getBytes();
            InetAddress broadcast = InetAddress.getByName("255.255.255.255");
            DatagramPacket dpOut = new DatagramPacket(pedido, pedido.length, broadcast, PUERTO_DESCUBRIMIENTO);
            socket.send(dpOut);

            byte[] buffer = new byte[64];
            DatagramPacket dpIn = new DatagramPacket(buffer, buffer.length);
            socket.receive(dpIn);

            String msg = new String(dpIn.getData(), 0, dpIn.getLength());
            String[] partes = msg.split("\\|", -1);
            if (partes.length < 2 || !"SERVER".equals(partes[0])) {
                return false;
            }
            ipServer = dpIn.getAddress().getHostAddress();
            puertoServer = Integer.parseInt(partes[1]);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private boolean esperarRol() {
        long inicio = System.currentTimeMillis();
        while (miRol == -1) {
            for (String m : hc.drenar()) {
                String[] p = m.split("\\|", -1);
                if (p.length >= 2 && "ROL".equals(p[0])) {
                    miRol = Integer.parseInt(p[1]);
                } else {
                    pendientes.add(m);
                }
            }
            if (miRol != -1) {
                return true;
            }
            if (System.currentTimeMillis() - inicio > TIMEOUT_ROL) {
                return false;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    public void enviarPosicion(float x, float y, boolean mirando, boolean enSuelo) {
        if (hc == null) return;
        hc.enviarMensaje("POS|" + x + "|" + y + "|" + mirando + "|" + enSuelo);
    }

    public void enviarCorte(int indice) {
        if (hc == null) return;
        hc.enviarMensaje("CORTE|" + indice);
    }

    public void enviarEspuma(float x, float y) {
        if (hc == null) return;
        hc.enviarMensaje("ESPUMA|" + x + "|" + y);
    }

    public void enviarPalanca() {
        if (hc == null) return;
        hc.enviarMensaje("PALANCA");
    }

    public void enviarSalida(boolean dentro) {
        if (hc == null) return;
        hc.enviarMensaje("SALIDA|" + dentro);
    }

    public void enviarMuerte() {
        if (hc == null) return;
        hc.enviarMensaje("MUERTE");
    }

    public void enviarReinicio() {
        if (hc == null) return;
        hc.enviarMensaje("REINICIO");
    }

    public List<String> drenarMensajes() {
        List<String> out = new ArrayList<>();
        out.addAll(pendientes);
        pendientes.clear();
        if (hc != null) {
            out.addAll(hc.drenar());
        }
        return out;
    }

    public int getMiRol() {
        return miRol;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void cerrar() {
        if (hc != null) {
            hc.detener();
        }
    }

    public static void main(String[] args) {
        Cliente c = new Cliente();
        if (c.conectar()) {
            System.out.println("conectado como rol " + c.getMiRol() + " a " + c.getIpServer());
            c.enviarPosicion(100f, 200f, true, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (String m : c.drenarMensajes()) {
                System.out.println("recibi: " + m);
            }
        } else {
            System.out.println("no se encontro servidor");
        }
        c.cerrar();
    }
}
