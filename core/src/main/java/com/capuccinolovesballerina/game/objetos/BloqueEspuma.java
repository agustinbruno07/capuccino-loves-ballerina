package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.math.Rectangle;

public class BloqueEspuma {

    private final Rectangle zona;
    private float vida;

    public BloqueEspuma(float x, float y, float ancho, float alto, float vida) {
        this.zona = new Rectangle(x, y, ancho, alto);
        this.vida = vida;
    }

    public void actualizar(float delta) {
        vida -= delta;
    }

    public boolean estaViva() {
        return vida > 0f;
    }

    public Rectangle getZona() {
        return zona;
    }

    //alpha es la opacidad, cosa de que se vaya desvaneciendo poco a poco. queda prime confien
    public float getAlpha() {
        float fade = 0.5f;
        if (vida >= fade) return 1f;
        return Math.max(0f, vida / fade);
    }
}
