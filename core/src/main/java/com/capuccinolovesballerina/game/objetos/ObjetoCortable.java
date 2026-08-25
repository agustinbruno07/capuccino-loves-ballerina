package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.math.Rectangle;

public class ObjetoCortable {

    private final String tipo;
    private final Rectangle zona;
    private boolean cortado;

    public ObjetoCortable(String tipo, Rectangle zona) {
        this.tipo = tipo;
        this.zona = zona;
    }

    public String getTipo() {
        return tipo;
    }

    public Rectangle getZona() {
        return zona;
    }

    public boolean estaCortado() {
        return cortado;
    }

    public void setCortado(boolean cortado) {
        this.cortado = cortado;
    }

    public boolean bloquea() {
        return !cortado;
    }
}
