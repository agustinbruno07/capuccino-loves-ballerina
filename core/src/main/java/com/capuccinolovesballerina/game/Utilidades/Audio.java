package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class Audio {

    private static Music musica;
    private static float volumen = 0.07f;

    public static void reproducirMusica(String ruta) {
        detenerMusica();
        musica = Gdx.audio.newMusic(Gdx.files.internal(ruta));
        musica.setLooping(true);
        musica.setVolume(volumen);
        musica.play();
    }

    public static void setVolumen(float nuevoVolumen) {
        volumen = Math.max(0f, Math.min(1f, nuevoVolumen));
        if (musica != null) {
            musica.setVolume(volumen);
        }
    }

    public static float getVolumen() {
        return volumen;
    }

    public static void detenerMusica() {
        if (musica != null) {
            musica.stop();
            musica.dispose();
            musica = null;
        }
    }

    public static void dispose() {
        detenerMusica();
    }
}
