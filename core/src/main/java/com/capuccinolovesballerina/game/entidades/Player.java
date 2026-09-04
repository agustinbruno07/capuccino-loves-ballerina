package com.capuccinolovesballerina.game.entidades;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.capuccinolovesballerina.game.Utilidades.Constantes;

public class Player {

    private int vida;
    private String nombre;

    protected float x;
    protected float y;
    protected float ancho;
    protected float alto;

    protected float velocidadX;
    protected float velocidadY;

    protected boolean enSuelo;
    protected boolean mirandoDerecha;

    private final Rectangle caja = new Rectangle();

    public Player() {
        this(0, 0, Constantes.ANCHO_PERSONAJE, Constantes.ALTO_PERSONAJE);
    }

    public Player(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.mirandoDerecha = true;
    }

    public void actualizar(float delta, boolean izquierda, boolean derecha, boolean saltar, Array<Rectangle> solidos) {

        float direccion = 0;

        if (izquierda) {
            direccion = -1;
        }

        if (derecha) {
            direccion = 1;
        }

        velocidadX = direccion * Constantes.VELOCIDAD_MOVIMIENTO;

        if (direccion > 0) {
            mirandoDerecha = true;
        }

        if (direccion < 0) {
            mirandoDerecha = false;
        }

        if (saltar && enSuelo) {
            velocidadY = Constantes.VELOCIDAD_SALTO;
            enSuelo = false;
        }

        velocidadY -= Constantes.GRAVEDAD * delta;

        if (velocidadY < -Constantes.VELOCIDAD_MAX_CAIDA) {
            velocidadY = -Constantes.VELOCIDAD_MAX_CAIDA;
        }

        moverEnX(delta, solidos);
        moverEnY(delta, solidos);
    }

    private void moverEnX(float delta, Array<Rectangle> solidos) {

        x += velocidadX * delta;
        caja.set(x, y, ancho, alto);

        for (Rectangle solido : solidos) {
            if (caja.overlaps(solido)) {

                if (velocidadX > 0) {
                    x = solido.x - ancho;
                } else if (velocidadX < 0) {
                    x = solido.x + solido.width;
                }

                caja.set(x, y, ancho, alto);
                velocidadX = 0;
            }
        }
    }

    private void moverEnY(float delta, Array<Rectangle> solidos) {

        y += velocidadY * delta;
        caja.set(x, y, ancho, alto);

        enSuelo = false;

        for (Rectangle solido : solidos) {
            if (caja.overlaps(solido)) {

                if (velocidadY < 0) {
                    y = solido.y + solido.height;
                    enSuelo = true;
                } else if (velocidadY > 0) {
                    y = solido.y - alto;
                }

                caja.set(x, y, ancho, alto);
                velocidadY = 0;
            }
        }
    }

    public Rectangle getCaja() {
        caja.set(x, y, ancho, alto);
        return caja;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    public boolean isEnSuelo() {
        return enSuelo;
    }

    public boolean isMirandoDerecha() {
        return mirandoDerecha;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void reaparecer(float x, float y) {
        this.x = x;
        this.y = y;
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.enSuelo = false;
    }
}
