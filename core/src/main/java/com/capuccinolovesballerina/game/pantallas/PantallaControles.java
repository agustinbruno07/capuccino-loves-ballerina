package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.Game;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;

public class PantallaControles implements Screen {

    private final Game juego;
    private Stage stage;

    public PantallaControles(Game juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        stage = new Stage(FabricaViewport.crear());

        Skin skin = Recursos.getSkin();

        Image fondo = new Image(
            Recursos.getImagen("interfaz/controles.png")
        );

        fondo.setFillParent(true);
        fondo.setScaling(Scaling.fit);

        stage.addActor(fondo);

        TextButton botonVolver = new TextButton(
            "VOLVER AL MENU", skin
        );

        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(
                InputEvent event, float x, float y
            ) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.bottom().right();

        tabla.add(botonVolver)
            .size(210, 55)
            .padRight(25)
            .padBottom(20);

        stage.addActor(tabla);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.05f, 0.04f, 0.06f, 1f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
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

    @Override
    public void dispose() {

        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }
}
