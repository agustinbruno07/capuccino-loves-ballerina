package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.HashMap;

public class Recursos {


    private static final HashMap<String, Texture> imagenes = new HashMap<String, Texture>();
    private static Skin skin;

    public static Texture getImagen(String ruta) {
        if (!imagenes.containsKey(ruta)) {
            imagenes.put(ruta, new Texture(Gdx.files.internal(ruta)));
        }
        return imagenes.get(ruta);
    }

    public static Skin getSkin() {
        if (skin == null) {
            skin = new Skin(Gdx.files.internal(Constantes.RUTA_SKIN));
        }
        return skin;
    }

    public static void dispose() {
        for (Texture textura : imagenes.values()) {
            textura.dispose();
        }
        imagenes.clear();
        if (skin != null) {
            skin.dispose();
        }
    }
}
