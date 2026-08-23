package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.mapa.Nivel;

public class PantallaJuego implements Screen {

    private final CapuccinoLovesBallerinaGame juego;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Nivel nivel;
    private OrthogonalTiledMapRenderer renderizador;
    private MenuPausa menuPausa;

    public PantallaJuego(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = FabricaViewport.crear(camera);

        // Modelo de datos del nivel (sin renderizado)
        nivel = new Nivel("mapas/nivel0.tmx", 64);
        // El renderer es cosa del cliente: se crea aca, no en Nivel
        renderizador = new OrthogonalTiledMapRenderer(nivel.getMapa(), 1f);
        menuPausa = new MenuPausa(juego);

        Gdx.app.log("PantallaJuego",
            "Solidos cargados: " + nivel.getColisiones().getSolidos().size);

        // Camara fija centrada (sin scroll, como Fireboy & Watergirl)
        camera.position.set(Constantes.ANCHO_MUNDO / 2f, Constantes.ALTO_MUNDO / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        actualizar(delta);
        dibujar();
    }

    private void actualizar(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (menuPausa.estaVisible()) menuPausa.ocultar();
            else menuPausa.mostrar();
        }
        if (menuPausa.estaVisible()) menuPausa.actualizar(delta);
    }

    private void dibujar() {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        camera.update();
        renderizador.setView(camera);
        renderizador.render();
        menuPausa.dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuPausa.resize(width, height);
    }

    @Override
    public void dispose() {
        renderizador.dispose();
        nivel.dispose();
        menuPausa.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
