package com.capuccinolovesballerina.game.red;

import com.capuccinolovesballerina.game.Utilidades.Entrada;
import com.capuccinolovesballerina.game.red.hilos.HiloCliente;

public class Cliente {

    static HiloCliente hc;
    static boolean fin = false;

    public static void main(String[] args) {
        Entrada entrada = new Entrada();
        hc = new HiloCliente();
        hc.start();

        do {
            System.out.println("Ingrese una opción");
            System.out.println("1) Enviar mensaje");
            entrada.ingresarEntero(1, 1);

            System.out.println("Ingrese el mensaje que desea enviar");
            String msg = entrada.ingresarTexto();
            hc.enviarMensaje(msg);

        } while (!fin);
    }
}
