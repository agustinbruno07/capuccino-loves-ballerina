package com.capuccinolovesballerina.game.Utilidades;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Entrada {

    private Scanner s = new Scanner(System.in);

    public int ingresarEntero(int min, int max) {
        boolean error;
        int numero = 0;

        do {
            error = false;

            try {
                numero = s.nextInt();

                if (numero > max || numero < min) {
                    System.out.println(
                        "Ingrese un número entre " + min + " y " + max
                    );
                    error = true;
                }

            } catch (InputMismatchException e) {
                System.out.println("Ingrese un valor numérico");
                error = true;

            } finally {
                s.nextLine();
            }

        } while (error);

        return numero;
    }

    public String ingresarTexto() {
        return s.nextLine();
    }
}
