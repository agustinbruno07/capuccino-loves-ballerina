package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.Utilidades.ControladorEntradaJugador;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.graficos.AnimacionesCappuccino;
import com.capuccinolovesballerina.game.mapa.Nivel;
import com.capuccinolovesballerina.game.objetos.ObjetoCortable;
import com.capuccinolovesballerina.game.objetos.Palanca;
import com.capuccinolovesballerina.game.objetos.Puerta;

public class PantallaJuego implements Screen {

    private final CapuccinoLovesBallerinaGame juego;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Nivel nivel;
    private OrthogonalTiledMapRenderer renderizador;
    private MenuPausa menuPausa;
    private MenuGameOver menuGameOver;
    private MenuVictoria menuVictoria;

    private ControladorEntradaJugador controladorEntrada;
    private CapuccinoAssassino cappuccino;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private AnimacionesCappuccino animaciones;

    private Palanca palanca;
    private Puerta puerta;
    private final Array<ObjetoCortable> cortables = new Array<>();

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

        Rectangle zonaPuerta = nivel.buscarObjeto("puerta");
        if (zonaPuerta != null) {
            puerta = new Puerta(zonaPuerta);
        }

        Rectangle zonaPalanca = nivel.buscarObjeto("palanca");
        if (zonaPalanca != null) {
            palanca = new Palanca(zonaPalanca);
        }

        cargarCortables();

        menuPausa = new MenuPausa(juego);
        menuGameOver = new MenuGameOver(juego, () -> reintentar());
        menuVictoria = new MenuVictoria(juego, () -> reintentar());

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        animaciones = new AnimacionesCappuccino();

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

    private void cargarCortables() {
        cortables.clear();
        String[] tipos = {"cuerda", "enredadera", "barril"};
        for (String tipo : tipos) {
            for (RectangleMapObject obj : nivel.buscarObjetos(tipo)) {
                cortables.add(new ObjetoCortable(tipo, new Rectangle(obj.getRectangle())));
            }
        }
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

        if (puerta != null) {
            puerta.setAbierta(false);
        }

        if (palanca != null && palanca.estaActivada()) {
            palanca.alternar();
        }

        for (ObjetoCortable cortable : cortables) {
            cortable.setCortado(false);
        }

        controladorEntrada.soltarTodo();
        animaciones.reiniciar();
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
            } else if (!menuGameOver.estaVisible() && !menuVictoria.estaVisible()) {
                menuPausa.mostrar();
                controladorEntrada.soltarTodo();
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

        if (menuVictoria.estaVisible()) {
            menuVictoria.actualizar(delta);
            return;
        }

        if (Gdx.input.getInputProcessor() != controladorEntrada) {
            Gdx.input.setInputProcessor(controladorEntrada);
        }

        Array<Rectangle> solidos = new Array<>(nivel.getColisiones().getSolidos());

        if (puerta != null && puerta.bloquea()) {
            solidos.add(puerta.getZona());
        }

        for (ObjetoCortable cortable : cortables) {
            if (cortable.bloquea()) {
                solidos.add(cortable.getZona());
            }
        }

        cappuccino.actualizar(
            delta,
            controladorEntrada.isIzquierda(),
            controladorEntrada.isDerecha(),
            controladorEntrada.isSaltoPresionado(),
            solidos
        );

        if (controladorEntrada.isHabilidadPresionada()) {
            animaciones.setEstado(AnimacionesCappuccino.Estado.ATTACK);
            cortar();
        } else if (!cappuccino.isEnSuelo()) {
            animaciones.setEstado(AnimacionesCappuccino.Estado.JUMP);
        } else if (controladorEntrada.isIzquierda() || controladorEntrada.isDerecha()) {
            animaciones.setEstado(AnimacionesCappuccino.Estado.WALK);
        } else {
            animaciones.setEstado(AnimacionesCappuccino.Estado.IDLE);
        }

        animaciones.actualizar(delta);

        if (palanca != null && controladorEntrada.isInteractuarPresionado()) {
            Rectangle caja = cappuccino.getCaja();
            Rectangle alcance = new Rectangle(caja.x - 12, caja.y - 12, caja.width + 24, caja.height + 24);
            if (alcance.overlaps(palanca.getZona())) {
                boolean vaACerrar = palanca.estaActivada();
                boolean jugadorEnLaPuerta = puerta != null && caja.overlaps(puerta.getZona());
                if (!vaACerrar || !jugadorEnLaPuerta) {
                    palanca.alternar();
                    if (puerta != null) {
                        puerta.setAbierta(palanca.estaActivada());
                    }
                }
            }
        }

        if (puerta != null && puerta.estaAbierta()
            && controladorEntrada.isInteractuarPresionado()
            && cappuccino.getCaja().overlaps(puerta.getZona())) {
            controladorEntrada.soltarTodo();
            menuVictoria.mostrar();
            return;
        }

        if (cappuccino.getY() < -cappuccino.getAlto()) {
            controladorEntrada.soltarTodo();
            menuGameOver.mostrar();
            return;
        }

        controladorEntrada.limpiarEventos();
    }

    private void cortar() {
        Rectangle caja = cappuccino.getCaja();
        float alcance = 40f;
        Rectangle zonaCorte;

        if (cappuccino.isMirandoDerecha()) {
            zonaCorte = new Rectangle(caja.x + caja.width, caja.y, alcance, caja.height);
        } else {
            zonaCorte = new Rectangle(caja.x - alcance, caja.y, alcance, caja.height);
        }

        for (ObjetoCortable cortable : cortables) {
            if (!cortable.estaCortado() && zonaCorte.overlaps(cortable.getZona())) {
                cortable.setCortado(true);
            }
        }
    }

    private void dibujar() {

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        camera.update();

        renderizador.setView(camera);
        renderizador.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (puerta != null && puerta.bloquea()) {
            shapeRenderer.setColor(0.45f, 0.28f, 0.15f, 1f);
            Rectangle z = puerta.getZona();
            shapeRenderer.rect(z.x, z.y, z.width, z.height);
        }

        if (palanca != null) {
            if (palanca.estaActivada()) {
                shapeRenderer.setColor(1f, 0.75f, 0f, 1f);
            } else {
                shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1f);
            }
            Rectangle z = palanca.getZona();
            shapeRenderer.rect(z.x, z.y, z.width, z.height);
        }

        for (ObjetoCortable cortable : cortables) {
            if (cortable.estaCortado()) continue;

            if ("cuerda".equals(cortable.getTipo())) {
                shapeRenderer.setColor(0.8f, 0.7f, 0.4f, 1f);
            } else if ("enredadera".equals(cortable.getTipo())) {
                shapeRenderer.setColor(0.2f, 0.7f, 0.3f, 1f);
            } else {
                shapeRenderer.setColor(0.55f, 0.35f, 0.15f, 1f);
            }

            Rectangle z = cortable.getZona();
            shapeRenderer.rect(z.x, z.y, z.width, z.height);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        TextureRegion frame = animaciones.getFrameActual();
        float tam = 96f;
        float x = cappuccino.getX() - (tam - cappuccino.getAncho()) / 2f;
        float y = cappuccino.getY();
        if (cappuccino.isMirandoDerecha()) {
            batch.draw(frame, x, y, tam, tam);
        } else {
            batch.draw(frame, x + tam, y, -tam, tam);
        }
        batch.end();

        menuPausa.dibujar();
        menuGameOver.dibujar();
        menuVictoria.dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuPausa.resize(width, height);
        menuGameOver.resize(width, height);
        menuVictoria.resize(width, height);
    }

    @Override
    public void dispose() {
        renderizador.dispose();
        nivel.dispose();
        menuPausa.dispose();
        menuGameOver.dispose();
        menuVictoria.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        animaciones.dispose();
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
