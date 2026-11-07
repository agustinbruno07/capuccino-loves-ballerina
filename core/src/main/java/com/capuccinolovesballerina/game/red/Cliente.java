package com.capuccinolovesballerina.game.red;

import com.capuccinolovesballerina.game.red.hilos.HiloCliente;

import java.util.List;

public class Cliente {

    private static final String IP_SERVER = "127.0.0.1";
    private static final int PUERTO_SERVER = 9992;

    private HiloCliente hc;
    private int miRol = -1;

    public void conectar() {
        hc = new HiloCliente(IP_SERVER, PUERTO_SERVER);
        hc.start();
        hc.enviarMensaje("JOIN");
    }

    public void enviarPosicion(float x, float y, boolean mirando, boolean enSuelo) {
        hc.enviarMensaje("POS|" + x + "|" + y + "|" + mirando + "|" + enSuelo);
    }

    public void enviarCorte(int indice) {
        hc.enviarMensaje("CORTE|" + indice);
    }

    public void enviarEspuma(float x, float y) {
        hc.enviarMensaje("ESPUMA|" + x + "|" + y);
    }

    public void enviarPalanca() {
        hc.enviarMensaje("PALANCA");
    }

    public void enviarSalida(boolean dentro) {
        hc.enviarMensaje("SALIDA|" + dentro);
    }

    public void enviarMuerte() {
        hc.enviarMensaje("MUERTE");
    }

    public void enviarReinicio() {
        hc.enviarMensaje("REINICIO");
    }

    public List<String> drenarMensajes() {
        return hc.drenar();
    }

    public int getMiRol() {
        return miRol;
    }

    public void setMiRol(int rol) {
        this.miRol = rol;
    }

    public void cerrar() {
        if (hc != null) {
            hc.detener();
        }
    }

    public static void main(String[] args) {
        Cliente c = new Cliente();
        c.conectar();
        try {
            Thread.sleep(500);
            c.enviarPosicion(100f, 200f, true, true);
            Thread.sleep(1000);
            for (String m : c.drenarMensajes()) {
                System.out.println("recibi: " + m);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.cerrar();
    }
}
