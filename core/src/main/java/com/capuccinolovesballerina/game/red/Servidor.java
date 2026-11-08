package com.capuccinolovesballerina.game.red;

import com.capuccinolovesballerina.game.red.hilos.HiloDescubrimiento;
import com.capuccinolovesballerina.game.red.hilos.HiloServidor;

public class Servidor {

    static HiloServidor hs;
    static HiloDescubrimiento hd;

    public static void main(String[] args) {
        hs = new HiloServidor();
        hs.start();
        hd = new HiloDescubrimiento();
        hd.start();
        System.out.println("Servidor de juego escuchando en puerto 9992");
        System.out.println("Servidor de descubrimiento escuchando en puerto 9993");
    }
}
