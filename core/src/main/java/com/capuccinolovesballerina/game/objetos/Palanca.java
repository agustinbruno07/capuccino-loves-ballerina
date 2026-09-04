package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.math.Rectangle;

public class Palanca {
    private final Rectangle zona;
    private boolean activada;

    public Palanca(Rectangle zona) {
        this.zona = zona;
    }

    public void alternar() {
        activada = !activada;
    }

    public boolean estaActivada() {
        return activada;
    }

    public Rectangle getZona() {
        return zona;
    }
}
