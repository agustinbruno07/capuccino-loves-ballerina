package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private HUD hud;

    private ControladorEntradaJugador controladorEntrada;
    private CapuccinoAssassino cappuccino;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private AnimacionesCappuccino animaciones;
    private Palanca palanca;
    private Puerta puerta;

    private Texture texturaCerrada;
    private Texture texturaAbierta;
    private Texture texturaPalancaOff;
    private Texture texturaPalancaOn;
    private Texture texturaPinchos;
    private Texture texturaLiana;

    private final Array<ObjetoCortable> cortables = new Array<>();
    private final Array<Rectangle> peligros = new Array<>();

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
        cargarPeligros();

        menuPausa = new MenuPausa(juego);
        menuGameOver = new MenuGameOver(juego, () -> reintentar());
        menuVictoria = new MenuVictoria(juego);
        hud = new HUD();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        animaciones = new AnimacionesCappuccino();
        cargarTexturas();
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
        String[] tipos = {"cuerda", "liana", "barril"};
        for (String tipo : tipos) {
            for (RectangleMapObject obj : nivel.buscarObjetos(tipo)) {
                cortables.add(new ObjetoCortable(tipo, new Rectangle(obj.getRectangle())));
            }
        }
    }

    private void cargarPeligros() {
        peligros.clear();
        for (RectangleMapObject obj : nivel.buscarObjetos("peligro")) {
            peligros.add(new Rectangle(obj.getRectangle()));
        }
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
        hud.reiniciar();
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 1f / 30f);
        actualizar(dt);
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

        hud.actualizar(delta);

        Array<Rectangle> solidos = new Array<>(nivel.getColisiones().getSolidos());

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

        for (Rectangle peligro : peligros) {
            if (cappuccino.getCaja().overlaps(peligro)) {
                controladorEntrada.soltarTodo();
                menuGameOver.mostrar();
                return;
            }
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

        for (ObjetoCortable cortable : cortables) {
            if (cortable.estaCortado()) continue;
            if ("liana".equals(cortable.getTipo())) continue; // ahora es sprite

            if ("cuerda".equals(cortable.getTipo())) {
                shapeRenderer.setColor(0.8f, 0.7f, 0.4f, 1f);
            } else {
                shapeRenderer.setColor(0.55f, 0.35f, 0.15f, 1f);
            }

            Rectangle z = cortable.getZona();
            shapeRenderer.rect(z.x, z.y, z.width, z.height);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (puerta != null) {
            Rectangle z = puerta.getZona();
            Texture tex = puerta.estaAbierta() ? texturaAbierta : texturaCerrada;
            float ancho = 96f;
            float alto = 128f;
            batch.draw(tex, z.x + (z.width - ancho) / 2f, z.y, ancho, alto);
        }

        if (palanca != null) {
            Rectangle z = palanca.getZona();
            Texture tex = palanca.estaActivada() ? texturaPalancaOn : texturaPalancaOff;
            float ancho = 48f;
            float alto = 56f;
            batch.draw(tex, z.x + (z.width - ancho) / 2f, z.y, ancho, alto);
        }

        for (Rectangle peligro : peligros) {
            batch.draw(texturaPinchos, peligro.x, peligro.y, peligro.width, peligro.width / 2f);
        }

        for (ObjetoCortable cortable : cortables) {
            if (cortable.estaCortado()) continue;
            if ("liana".equals(cortable.getTipo())) {
                Rectangle z = cortable.getZona();
                float altoSegmento = 192f;
                for (float y = z.y; y < z.y + z.height; y += altoSegmento) {
                    batch.draw(texturaLiana, z.x, y, z.width, altoSegmento);
                }
            }
        }

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

        hud.dibujar();
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
        hud.resize(width, height);
    }

    @Override
    public void dispose() {
        renderizador.dispose();
        nivel.dispose();
        menuPausa.dispose();
        menuGameOver.dispose();
        menuVictoria.dispose();
        hud.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        animaciones.dispose();
        texturaAbierta.dispose();
        texturaCerrada.dispose();
        texturaPalancaOff.dispose();
        texturaPalancaOn.dispose();
        texturaPinchos.dispose();
        texturaLiana.dispose();

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
