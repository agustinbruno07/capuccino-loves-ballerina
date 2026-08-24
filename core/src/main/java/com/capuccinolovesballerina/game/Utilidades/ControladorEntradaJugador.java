package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class ControladorEntradaJugador extends InputAdapter {

    private boolean izquierda;
    private boolean derecha;

    private boolean saltoW;
    private boolean saltoEspacio;
    private boolean saltoPresionado;

    private boolean interactuarPresionado;
    private boolean habilidadPresionada;

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.ESCAPE) {
            return false;
        }

        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            izquierda = true;
        }

        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            derecha = true;
        }

        if (keycode == Input.Keys.W) {
            if (!saltoW) {
                saltoW = true;
                if (!saltoEspacio) {
                    saltoPresionado = true;
                }
            }
        }

        if (keycode == Input.Keys.SPACE) {
            if (!saltoEspacio) {
                saltoEspacio = true;
                if (!saltoW) {
                    saltoPresionado = true;
                }
            }
        }

        if (keycode == Input.Keys.E) {
            interactuarPresionado = true;
        }

        if (keycode == Input.Keys.F) {
            habilidadPresionada = true;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            izquierda = false;
        }

        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            derecha = false;
        }

        if (keycode == Input.Keys.W) {
            saltoW = false;
        }

        if (keycode == Input.Keys.SPACE) {
            saltoEspacio = false;
        }

        return true;
    }

    public boolean isIzquierda() {
        return izquierda;
    }

    public boolean isDerecha() {
        return derecha;
    }

    public boolean isSaltoPresionado() {
        return saltoPresionado;
    }

    public boolean isInteractuarPresionado() {
        return interactuarPresionado;
    }

    public boolean isHabilidadPresionada() {
        return habilidadPresionada;
    }

    public void limpiarEventos() {
        saltoPresionado = false;
        interactuarPresionado = false;
        habilidadPresionada = false;
    }
}
