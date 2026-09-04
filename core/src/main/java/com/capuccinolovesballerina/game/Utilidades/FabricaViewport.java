package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FabricaViewport {

    private FabricaViewport() {}

    public static Viewport crear() {
        return new FitViewport(Constantes.ANCHO_MUNDO, Constantes.ALTO_MUNDO);
    }
    public static Viewport crear(OrthographicCamera camera) {
        return new FitViewport(Constantes.ANCHO_MUNDO, Constantes.ALTO_MUNDO, camera);
    }
}
