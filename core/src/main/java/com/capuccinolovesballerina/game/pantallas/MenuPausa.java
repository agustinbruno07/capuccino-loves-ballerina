package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;

public class MenuPausa {

    private final CapuccinoLovesBallerinaGame juego;

    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private boolean visible;

    public MenuPausa(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;

        viewport = FabricaViewport.crear();
        stage = new Stage(viewport);
        skin = Recursos.getSkin();
        shapeRenderer = new ShapeRenderer();

        TextButton botonReanudar = new TextButton("REANUDAR", skin);
        TextButton botonMenu = new TextButton("VOLVER AL MENU", skin);
        TextButton botonSalir = new TextButton("SALIR", skin);

        botonReanudar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ocultar();
            }
        });

        botonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        botonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.add(botonReanudar).size(250, 60).pad(10).row();
        tabla.add(botonMenu).size(250, 60).pad(10).row();
        tabla.add(botonSalir).size(250, 60).pad(10);
        stage.addActor(tabla);
    }

    public void mostrar() {
        visible = true;

        Gdx.input.setInputProcessor(stage);
    }

    public void ocultar() {
        visible = false;
        Gdx.input.setInputProcessor(null);
    }

    public boolean estaVisible() {
        return visible;
    }

    public void actualizar(float delta) {
        if (visible) {
            stage.act(delta);
        }
    }

    public void dibujar() {
        if (!visible) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();

        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        Recursos.dispose();
        shapeRenderer.dispose();
    }
}
