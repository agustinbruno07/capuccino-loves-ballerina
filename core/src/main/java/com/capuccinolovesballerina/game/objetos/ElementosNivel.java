package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.mapa.Nivel;

public class ElementosNivel {

    private final Array<ObjetoCortable> cortables = new Array<>();
    private final Array<Rectangle> peligros = new Array<>();

    private final Palanca palanca;
    private final Puerta puerta;

    private final float spawnX;
    private final float spawnY;

    private final float spawnBallerinaX;
    private final float spawnBallerinaY;

    public ElementosNivel(Nivel nivel) {

        Rectangle zonaPuerta = nivel.buscarObjeto("puerta");
        puerta = zonaPuerta == null ? null : new Puerta(new Rectangle(zonaPuerta));

        Rectangle zonaPalanca = nivel.buscarObjeto("palanca");
        palanca = zonaPalanca == null ? null : new Palanca(new Rectangle(zonaPalanca));

        cargarCortables(nivel);
        cargarPeligros(nivel);

        Rectangle spawn = nivel.buscarObjeto("spawn_cappuccino");
        if (spawn != null) {
            spawnX = spawn.x + (spawn.getWidth() - Constantes.ANCHO_PERSONAJE) / 2f;
            spawnY = spawn.y;
        } else {
            spawnX = 320;
            spawnY = 192;
        }

        Rectangle spawnBallerina = nivel.buscarObjeto("spawn_ballerina");
        if (spawnBallerina != null) {
            spawnBallerinaX = spawnBallerina.x + (spawnBallerina.getWidth() - Constantes.ANCHO_PERSONAJE) / 2f;
            spawnBallerinaY = spawnBallerina.y;
        } else {
            spawnBallerinaX = spawnX + 80f;
            spawnBallerinaY = spawnY;
        }
    }

    private void cargarCortables(Nivel nivel) {
        String[] tipos = {"cuerda", "liana", "barril"};
        for (String tipo : tipos) {
            for (RectangleMapObject obj : nivel.buscarObjetos(tipo)) {
                cortables.add(new ObjetoCortable(tipo, new Rectangle(obj.getRectangle())));
            }
        }
    }

    private void cargarPeligros(Nivel nivel) {
        for (RectangleMapObject obj : nivel.buscarObjetos("peligro")) {
            peligros.add(new Rectangle(obj.getRectangle()));
        }
    }

    public Array<ObjetoCortable> getCortables() { return cortables; }
    public Array<Rectangle> getPeligros() { return peligros; }
    public Palanca getPalanca() { return palanca; }
    public Puerta getPuerta() { return puerta; }
    public float getSpawnX() { return spawnX; }
    public float getSpawnY() { return spawnY; }
    public float getSpawnBallerinaX() { return spawnBallerinaX; }
    public float getSpawnBallerinaY() { return spawnBallerinaY; }
}
