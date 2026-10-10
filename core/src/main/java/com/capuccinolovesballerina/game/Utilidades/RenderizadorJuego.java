
package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.graficos.AnimacionesCappuccino;
import com.capuccinolovesballerina.game.mapa.Nivel;
import com.capuccinolovesballerina.game.objetos.ElementosNivel;
import com.capuccinolovesballerina.game.objetos.ObjetoCortable;
import com.capuccinolovesballerina.game.objetos.Palanca;
import com.capuccinolovesballerina.game.objetos.Puerta;
import com.capuccinolovesballerina.game.pantallas.HUD;
import com.capuccinolovesballerina.game.pantallas.MenuGameOver;
import com.capuccinolovesballerina.game.pantallas.MenuPausa;
import com.capuccinolovesballerina.game.pantallas.MenuVictoria;

public class RenderizadorJuego {

    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderizador;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;

    private final CapuccinoAssassino cappuccino;
    private final AnimacionesCappuccino animaciones;
    private final ElementosNivel elementos;

    private final HUD hud;
    private final MenuPausa menuPausa;
    private final MenuGameOver menuGameOver;
    private final MenuVictoria menuVictoria;

    private Texture texturaCerrada;
    private Texture texturaAbierta;
    private Texture texturaPalancaOff;
    private Texture texturaPalancaOn;
    private Texture texturaPinchos;
    private Texture texturaLiana;

    public RenderizadorJuego(OrthographicCamera camera, Nivel nivel, CapuccinoAssassino cappuccino, AnimacionesCappuccino animaciones, ElementosNivel elementos, HUD hud, MenuPausa menuPausa, MenuGameOver menuGameOver, MenuVictoria menuVictoria) {
        this.camera = camera;
        this.renderizador = new OrthogonalTiledMapRenderer(nivel.getMapa(), 1f);
        this.cappuccino = cappuccino;
        this.animaciones = animaciones;
        this.elementos = elementos;
        this.hud = hud;
        this.menuPausa = menuPausa;
        this.menuGameOver = menuGameOver;
        this.menuVictoria = menuVictoria;

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        cargarTexturas();
    }

    private void cargarTexturas() {

        texturaCerrada = new Texture("objetos/puerta_cerrada.png");
        texturaAbierta = new Texture("objetos/puerta_abierta.png");
        texturaPalancaOff = new Texture("objetos/palanca_apagada.png");
        texturaPalancaOn = new Texture("objetos/palanca_encendida.png");
        texturaPinchos = new Texture("objetos/pinchos.png");
        texturaLiana = new Texture("objetos/liana.png");

        texturaCerrada.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaAbierta.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaPalancaOff.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaPalancaOn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaPinchos.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaLiana.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void dibujar() {

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        camera.update();
        renderizador.setView(camera);
        renderizador.render();

        dibujarCortables();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        dibujarPuerta();
        dibujarPalanca();
        dibujarPeligros();
        dibujarLianas();
        dibujarPersonaje();

        batch.end();

        hud.dibujar();
        menuPausa.dibujar();
        menuGameOver.dibujar();
        menuVictoria.dibujar();
    }

    private void dibujarCortables() {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (ObjetoCortable cortable : elementos.getCortables()) {
            if (cortable.estaCortado()) continue;
            if ("liana".equals(cortable.getTipo())) continue;

            if ("cuerda".equals(cortable.getTipo())) {
                shapeRenderer.setColor(0.8f, 0.7f, 0.4f, 1f);
            } else {
                shapeRenderer.setColor(0.55f, 0.35f, 0.15f, 1f);
            }

            Rectangle z = cortable.getZona();
            shapeRenderer.rect(z.x, z.y, z.width, z.height);
        }

        shapeRenderer.end();
    }

    private void dibujarPuerta() {

        Puerta puerta = elementos.getPuerta();
        if (puerta == null) return;

        Rectangle z = puerta.getZona();

        Texture textura = puerta.estaAbierta() ? texturaAbierta : texturaCerrada;

        float ancho = 96f;
        float alto = 128f;

        batch.draw(textura, z.x + (z.width - ancho) / 2f, z.y, ancho, alto);
    }

    private void dibujarPalanca() {

        Palanca palanca = elementos.getPalanca();
        if (palanca == null) return;
        Rectangle z = palanca.getZona();
        Texture textura = palanca.estaActivada() ? texturaPalancaOn : texturaPalancaOff;
        float ancho = 48f;
        float alto = 56f;

        batch.draw(textura, z.x + (z.width - ancho) / 2f, z.y, ancho, alto);
    }

    private void dibujarPeligros() {

        for (Rectangle peligro : elementos.getPeligros()) {
            batch.draw(texturaPinchos, peligro.x, peligro.y, peligro.width, peligro.width / 2f);
        }

    }

    private void dibujarLianas() {

        for (ObjetoCortable cortable : elementos.getCortables()) {
            if (cortable.estaCortado()) continue;
            if (!"liana".equals(cortable.getTipo())) continue;

            Rectangle z = cortable.getZona();
            float altoSegmento = 192f;

            for (float y = z.y; y < z.y + z.height; y += altoSegmento) {
                batch.draw(texturaLiana, z.x, y, z.width, altoSegmento);
            }
        }
    }

    private void dibujarPersonaje() {

        TextureRegion frame = animaciones.getFrameActual();

        float tam = 96f;
        float x = cappuccino.getX() - (tam - cappuccino.getAncho()) / 2f;
        float y = cappuccino.getY();

        if (cappuccino.isMirandoDerecha()) {
            batch.draw(frame, x, y, tam, tam);
        } else {
            batch.draw(frame, x + tam, y, -tam, tam);
        }
    }

    public void dispose() {
        renderizador.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        texturaAbierta.dispose();
        texturaCerrada.dispose();
        texturaPalancaOff.dispose();
        texturaPalancaOn.dispose();
        texturaPinchos.dispose();
        texturaLiana.dispose();
    }
}
