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

public class PantallaMenu implements Screen {
    private Texture fondo;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton botonJugar;
    private TextButton botonSalir;
    private Table tabla;


    @Override
    public void show() {
        fondo = new Texture("interfaz/fondo_menu.png");
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        botonJugar = new TextButton("JUGAR", skin);
        botonSalir = new TextButton("SALIR", skin);
        tabla = new Table();
        tabla.setFillParent(true);
        tabla.padTop(200);
        tabla.add(botonJugar).width(200).height(60);
        tabla.row();
        tabla.add(botonSalir).width(200).height(60).padTop(20);
        stage.addActor(tabla);
        Gdx.input.setInputProcessor(stage);

        botonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        fondo.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();

    }
}
