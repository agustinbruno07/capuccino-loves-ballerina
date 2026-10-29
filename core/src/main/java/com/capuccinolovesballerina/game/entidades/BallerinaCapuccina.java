package com.capuccinolovesballerina.game.entidades;

import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.objetos.BloqueEspuma;

public class BallerinaCapuccina extends Player {

    private BloqueEspuma espuma = null;

    private float cooldownEspuma = 0f;

    public BallerinaCapuccina(float x, float y) {
        super(x, y, Constantes.ANCHO_PERSONAJE, Constantes.ALTO_PERSONAJE);
    }

    public void crearEspuma() {
        if (cooldownEspuma > 0f) {
            return;
        }

        espuma = null;

        float ancho = Constantes.ANCHO_ESPUMA;
        float alto = Constantes.ALTO_ESPUMA;
        float gap = Constantes.DISTANCIA_ESPUMA;

        float xEspuma;
        if (isMirandoDerecha()) {
            xEspuma = getX() + getAncho() + gap;
        } else {
            xEspuma = getX() - gap - ancho;
        }

        float yEspuma;
        if(enSuelo) {
            yEspuma = getY();
        }else{
            yEspuma = getY() - alto;
        }

        espuma = new BloqueEspuma(xEspuma, yEspuma, ancho, alto, Constantes.VIDA_ESPUMA);
        cooldownEspuma = Constantes.COOLDOWN_ESPUMA;
    }

    public void actualizarHabilidad(float delta) {
        if (cooldownEspuma > 0f) {
            cooldownEspuma -= delta;
            if (cooldownEspuma < 0f) cooldownEspuma = 0f;
        }

        if (espuma != null) {
            espuma.actualizar(delta);
            if (!espuma.estaViva()) {
                espuma = null;
            }
        }
    }

    public BloqueEspuma getEspuma() {
        return espuma;
    }

    public boolean puedeUsarEspuma() {
        return cooldownEspuma <= 0f;
    }

    public void reiniciarHabilidad() {
        espuma = null;
        cooldownEspuma = 0f;
    }
}
