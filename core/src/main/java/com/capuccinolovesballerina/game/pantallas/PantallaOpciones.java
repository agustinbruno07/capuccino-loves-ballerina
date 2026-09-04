package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;


public class PantallaOpciones implements Screen {

    private final CapuccinoLovesBallerinaGame juego;
    private Stage stage;

    public PantallaOpciones(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        stage = new Stage(FabricaViewport.crear());
        Skin skin = Recursos.getSkin();

        Label titulo = new Label("OPCIONES", skin);
        titulo.setFontScale(2f);

        Label proximo = new Label("PROXIMAMENTE", skin);
        proximo.setAlignment(Align.center);

        TextButton botonVolver = new TextButton("VOLVER", skin);
        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.add(titulo).padBottom(30).row();
        tabla.add(proximo).padBottom(50).row();
        tabla.add(botonVolver).size(200, 60);
        stage.addActor(tabla);

        Gdx.input.setInputProcessor(stage);
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
