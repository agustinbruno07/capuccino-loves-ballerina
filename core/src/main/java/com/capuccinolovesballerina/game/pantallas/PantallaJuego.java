package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Game;
import com.capuccinolovesballerina.game.Utilidades.*;
import com.capuccinolovesballerina.game.entidades.BallerinaCapuccina;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.graficos.AnimacionesCappuccino;
import com.capuccinolovesballerina.game.mapa.Nivel;
import com.capuccinolovesballerina.game.objetos.ElementosNivel;

public class PantallaJuego implements Screen {

    private final Game juego;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Nivel nivel;
    private ElementosNivel elementos;

    private MenuPausa menuPausa;
    private MenuGameOver menuGameOver;
    private MenuVictoria menuVictoria;
    private HUD hud;

    private ControladorEntradaJugador controladorEntrada;
    private CapuccinoAssassino cappuccino;
    private BallerinaCapuccina ballerina;
    private AnimacionesCappuccino animaciones;

    private ControladorPartida controladorPartida;
    private RenderizadorJuego renderizadorJuego;

    public PantallaJuego(Game juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = FabricaViewport.crear(camera);

        nivel = new Nivel("mapas/nivel0.tmx", 64);
        elementos = new ElementosNivel(nivel);

        menuPausa = new MenuPausa(juego);
        menuGameOver = new MenuGameOver(juego, () -> controladorPartida.reiniciar());
        menuVictoria = new MenuVictoria(juego);
        hud = new HUD();

        controladorEntrada = new ControladorEntradaJugador();
        animaciones = new AnimacionesCappuccino();

        cappuccino = new CapuccinoAssassino(elementos.getSpawnX(), elementos.getSpawnY());
        ballerina = new BallerinaCapuccina(elementos.getSpawnBallerinaX(), elementos.getSpawnBallerinaY());

        Gdx.input.setInputProcessor(controladorEntrada);

        camera.position.set(Constantes.ANCHO_MUNDO / 2f, Constantes.ALTO_MUNDO / 2f, 0);
        camera.update();

        controladorPartida = new ControladorPartida(
            nivel, elementos, menuPausa, menuGameOver, menuVictoria,
            hud, controladorEntrada, cappuccino, ballerina, animaciones
        );

        renderizadorJuego = new RenderizadorJuego(
            camera, nivel, cappuccino, ballerina, animaciones,
            elementos, hud, menuPausa, menuGameOver, menuVictoria
        );

        Audio.reproducirMusica("sonidos/musica_juego.mp3");
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 1f / 30f);
        controladorPartida.actualizar(dt);
        renderizadorJuego.dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuPausa.resize(width, height);
        menuGameOver.resize(width, height);
        menuVictoria.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void dispose() {
        renderizadorJuego.dispose();
        nivel.dispose();
        menuPausa.dispose();
        menuGameOver.dispose();
        menuVictoria.dispose();
        hud.dispose();
        animaciones.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
