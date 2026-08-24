package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.math.Rectangle;

public class Puerta {
    private final Rectangle zona;
    private boolean abierta;

    public Puerta(Rectangle zona) {
        this.zona = zona;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    public boolean estaAbierta() {
        return abierta;
    }

    public boolean bloquea() {
        return !abierta;
    }

    public Rectangle getZona() {
        return zona;
    }
}
