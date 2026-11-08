package com.capuccinolovesballerina.game.objetos;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.mapa.Nivel;

public class ElementosNivel {

    private final Array<ObjetoCortable> cortables = new Array<>();
    private final Array<Rectangle> peligros = new Array<>();
    private final Array<Rectangle> hitboxPeligros = new Array<>();

    private final Palanca palancaCappuccino;
    private final Palanca palancaBallerina;
    private final Puerta puerta;

    private final float spawnX;
    private final float spawnY;

    private final float spawnBallerinaX;
    private final float spawnBallerinaY;

    public ElementosNivel(Nivel nivel) {

        // Nivel ya devuelve los rectangulos en unidades del mundo (escalados),
        // asi que se pueden usar directo.
        Rectangle zonaPuerta = nivel.buscarObjeto("puerta");
        puerta = zonaPuerta == null ? null : new Puerta(zonaPuerta);

        Rectangle zonaPalCap = nivel.buscarObjeto("palanca_cappuccino");
        palancaCappuccino = zonaPalCap == null ? null : new Palanca(zonaPalCap);

        Rectangle zonaPalBal = nivel.buscarObjeto("palanca_ballerina");
        palancaBallerina = zonaPalBal == null ? null : new Palanca(zonaPalBal);

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
            for (Rectangle zona : nivel.buscarObjetos(tipo)) {
                cortables.add(new ObjetoCortable(tipo, zona));
            }
        }
    }

    private void cargarPeligros(Nivel nivel) {
        for (Rectangle zona : nivel.buscarObjetos("peligro")) {
            peligros.add(zona);                       // lo que se dibuja (no cambia)

            float altoSprite = zona.width / 2f;       // igual que en RenderizadorJuego
            hitboxPeligros.add(new Rectangle(
                zona.x + Constantes.MARGEN_LATERAL_PELIGRO,
                zona.y,
                zona.width - 2 * Constantes.MARGEN_LATERAL_PELIGRO,
                altoSprite - Constantes.MARGEN_SUPERIOR_PELIGRO
            ));
        }
    }
    public Array<Rectangle> getHitboxPeligros() { return hitboxPeligros; }
    public Array<ObjetoCortable> getCortables() { return cortables; }
    public Array<Rectangle> getPeligros() { return peligros; }
    public Palanca getPalancaCappuccino() { return palancaCappuccino; }
    public Palanca getPalancaBallerina() { return palancaBallerina; }
    public Puerta getPuerta() { return puerta; }
    public float getSpawnX() { return spawnX; }
    public float getSpawnY() { return spawnY; }
    public float getSpawnBallerinaX() { return spawnBallerinaX; }
    public float getSpawnBallerinaY() { return spawnBallerinaY; }
}
