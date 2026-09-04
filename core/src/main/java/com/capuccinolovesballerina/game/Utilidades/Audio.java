package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class Audio {

    private static Music musica;

    public static void reproducirMusica(String ruta) {
        detenerMusica();
        musica = Gdx.audio.newMusic(Gdx.files.internal(ruta));
        musica.setLooping(true);
        musica.play();
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
