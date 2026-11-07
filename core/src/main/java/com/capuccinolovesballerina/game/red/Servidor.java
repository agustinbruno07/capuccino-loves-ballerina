package com.capuccinolovesballerina.game.red;

import com.capuccinolovesballerina.game.red.hilos.HiloServidor;

public class Servidor {

    static HiloServidor hs;

    public static void main(String[] args) {
        hs = new HiloServidor();
        hs.start();
        System.out.println("Servidor escuchando en puerto 9992");
    }
}
