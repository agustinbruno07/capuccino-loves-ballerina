package com.capuccinolovesballerina.game.red.hilos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class HiloDescubrimiento extends Thread {

    private static final int PUERTO_DESCUBRIMIENTO = 9993;
    private static final int PUERTO_JUEGO = 9992;

    private DatagramSocket socket;
    private boolean fin = false;

    public HiloDescubrimiento() {
        try {
            socket = new DatagramSocket(PUERTO_DESCUBRIMIENTO);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!fin) {
            byte[] datos = new byte[64];
            DatagramPacket dp = new DatagramPacket(datos, datos.length);
            try {
                socket.receive(dp);
                String msg = new String(dp.getData(), 0, dp.getLength());
                if ("DISCOVER".equals(msg)) {
                    responder(dp.getSocketAddress());
                }
            } catch (IOException e) {
                if (!fin) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responder(SocketAddress destino) {
        byte[] data = ("SERVER|" + PUERTO_JUEGO).getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length, destino);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detener() {
        fin = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
