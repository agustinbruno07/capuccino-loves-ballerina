package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;

public class PantallaMenu implements Screen {
    private Texture fondo;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton botonJugar;
    private TextButton botonOpciones;
    private TextButton botonSalir;
    private Table tabla;
    private final CapuccinoLovesBallerinaGame juego;

    public PantallaMenu(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        fondo = new Texture("interfaz/fondo_menu.png");
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        crearBotones();
        agregarBotones();
        escucharBotones();

    }
    private void crearBotones(){
        botonJugar = new TextButton("JUGAR", skin);
        botonOpciones = new TextButton("OPCIONES", skin);
        botonSalir = new TextButton("SALIR", skin);
    }
    private void agregarBotones(){
        tabla = new Table();
        tabla.setFillParent(true);
        tabla.bottom();

        tabla.add(botonJugar).size(200, 60).pad(10).row();
        tabla.add(botonOpciones).size(200, 60).pad(10).row();
        tabla.add(botonSalir).size(200, 60).pad(10);

        stage.addActor(tabla);
        Gdx.input.setInputProcessor(stage);

    }
    private void escucharBotones() {
        botonJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaJuego());
            }
        });
        botonSalir.addListener (new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        botonOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaOpciones());
            }
        });
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        fondo.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
