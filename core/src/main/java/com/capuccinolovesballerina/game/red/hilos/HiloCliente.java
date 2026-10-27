package com.capuccinolovesballerina.game.red.hilos;

import java.io.IOException;
import java.net.*;

public class HiloCliente extends Thread{
    DatagramSocket socket;
    boolean fin = false;
    private InetAddress ipServer;
    private int puesto = 9992;
    public HiloCliente() {
        try {
            socket = new DatagramSocket();
            ipServer = InetAddress.getByName("127.0.0.1");
        } catch (SocketException | UnknownHostException e) {
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
                e.printStackTrace();
            }
        }
    }

    private void procesarMensaje(DatagramPacket dp) {
        String msg = new String(dp.getData(), 0, dp.getLength());
        System.out.println(msg);
    }

    public void enviarMensaje(String msg)  {
        byte[] data = msg.getBytes();
        InetAddress ipDestino;
        try {
            int puerto = 9992;
            DatagramPacket dp = new DatagramPacket(data, data.length, ipServer, puerto);
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
