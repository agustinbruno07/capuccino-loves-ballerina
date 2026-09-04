package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.Audio;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;

public class PantallaMenu implements Screen {
    private Image fondo;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton botonJugar;
    private TextButton botonOpciones;
    private TextButton botonSalir;
    private final CapuccinoLovesBallerinaGame juego;

    public PantallaMenu(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        stage = new Stage(FabricaViewport.crear());
        skin = Recursos.getSkin();
        fondo = new Image(Recursos.getImagen("interfaz/fondo_menu.png"));
        fondo.setFillParent(true);
        stage.addActor(fondo);
        crearBotones();
        agregarBotones();
        escucharBotones();
        Audio.reproducirMusica("sonidos/musica_menu.mp3");

    }

    private void crearBotones(){
        botonJugar = new TextButton("JUGAR", skin);
        botonOpciones = new TextButton("OPCIONES", skin);
        botonSalir = new TextButton("SALIR", skin);
    }

    private void agregarBotones(){

        Table tabla = new Table();
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
                juego.setScreen(new PantallaJuego(juego));
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
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
