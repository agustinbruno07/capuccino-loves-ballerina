package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.Utilidades.ControladorEntradaJugador;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.mapa.Nivel;

public class PantallaJuego implements Screen {

    private final CapuccinoLovesBallerinaGame juego;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Nivel nivel;
    private OrthogonalTiledMapRenderer renderizador;
    private MenuPausa menuPausa;
    private MenuGameOver menuGameOver;

    private ControladorEntradaJugador controladorEntrada;
    private CapuccinoAssassino cappuccino;
    private ShapeRenderer shapeRenderer;

    private float spawnX;
    private float spawnY;

    public PantallaJuego(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = FabricaViewport.crear(camera);

        nivel = new Nivel("mapas/nivel0.tmx", 64);
        renderizador = new OrthogonalTiledMapRenderer(nivel.getMapa(), 1f);

        menuPausa = new MenuPausa(juego);
        menuGameOver = new MenuGameOver(juego, () -> reintentar());

        shapeRenderer = new ShapeRenderer();

        controladorEntrada = new ControladorEntradaJugador();
        Gdx.input.setInputProcessor(controladorEntrada);

        crearCappuccino();

        camera.position.set(
            Constantes.ANCHO_MUNDO / 2f,
            Constantes.ALTO_MUNDO / 2f,
            0
        );
        camera.update();
    }

    private void crearCappuccino() {

        Rectangle spawn = nivel.buscarObjeto("spawn_cappuccino");

        if (spawn != null) {
            spawnX = spawn.x + (spawn.getWidth() - Constantes.ANCHO_PERSONAJE) / 2f;
            spawnY = spawn.y;
        } else {
            spawnX = 320;
            spawnY = 192;
        }

        cappuccino = new CapuccinoAssassino(spawnX, spawnY);
    }

    private void reintentar() {
        cappuccino.reaparecer(spawnX, spawnY);
        controladorEntrada.limpiarEventos();
    }

    @Override
    public void render(float delta) {
        actualizar(delta);
        dibujar();
    }

    private void actualizar(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (menuPausa.estaVisible()) {
                menuPausa.ocultar();
            } else if (!menuGameOver.estaVisible()) {
                menuPausa.mostrar();
                controladorEntrada.limpiarEventos();
            }
        }

        if (menuPausa.estaVisible()) {
            menuPausa.actualizar(delta);
            return;
        }

        if (menuGameOver.estaVisible()) {
            menuGameOver.actualizar(delta);
            return;
        }

        if (Gdx.input.getInputProcessor() != controladorEntrada) {
            Gdx.input.setInputProcessor(controladorEntrada);
        }

        cappuccino.actualizar(
            delta,
            controladorEntrada.isIzquierda(),
            controladorEntrada.isDerecha(),
            controladorEntrada.isSaltoPresionado(),
            nivel.getColisiones().getSolidos()
        );

        if (cappuccino.getY() < -cappuccino.getAlto()) {
            menuGameOver.mostrar();
            return;
        }

        controladorEntrada.limpiarEventos();
    }

    private void dibujar() {

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        camera.update();

        renderizador.setView(camera);
        renderizador.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.85f, 0.35f, 0.25f, 1f);
        shapeRenderer.rect(
            cappuccino.getX(),
            cappuccino.getY(),
            cappuccino.getAncho(),
            cappuccino.getAlto()
        );
        shapeRenderer.end();

        menuPausa.dibujar();
        menuGameOver.dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuPausa.resize(width, height);
        menuGameOver.resize(width, height);
    }

    @Override
    public void dispose() {
        renderizador.dispose();
        nivel.dispose();
        menuPausa.dispose();
        menuGameOver.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
