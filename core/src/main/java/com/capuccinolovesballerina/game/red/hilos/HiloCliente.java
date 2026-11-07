package com.capuccinolovesballerina.game.red.hilos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class HiloCliente extends Thread {

    private DatagramSocket socket;
    private boolean fin = false;
    private InetAddress ipServer;
    private int puertoServer;

    private final List<String> bandeja = new ArrayList<>();

    public HiloCliente(String ip, int puerto) {
        try {
            socket = new DatagramSocket();
            ipServer = InetAddress.getByName(ip);
            puertoServer = puerto;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                String msg = new String(dp.getData(), 0, dp.getLength());
                synchronized (bandeja) {
                    bandeja.add(msg);
                }
            } catch (IOException e) {
                if (!fin) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void enviarMensaje(String msg) {
        byte[] data = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length, ipServer, puertoServer);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> drenar() {
        synchronized (bandeja) {
            List<String> copia = new ArrayList<>(bandeja);
            bandeja.clear();
            return copia;
        }
    }

    public void detener() {
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
