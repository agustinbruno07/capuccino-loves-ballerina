package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;

public class MenuGameOver {

    private final Stage stage;
    private final Viewport viewport;
    private boolean visible;

    public MenuGameOver(CapuccinoLovesBallerinaGame juego, Runnable accionReintentar) {
        viewport = FabricaViewport.crear();
        stage = new Stage(viewport);
        Skin skin = Recursos.getSkin();

        Image fondo = new Image(Recursos.getImagen("interfaz/game_over.png"));
        fondo.setFillParent(true);
        fondo.setScaling(Scaling.fill);
        stage.addActor(fondo);

        TextButton botonReintentar = new TextButton("REINTENTAR", skin);
        TextButton botonMenu = new TextButton("VOLVER AL MENU", skin);

        botonReintentar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ocultar();
                accionReintentar.run();
            }
        });

        botonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.bottom();
        tabla.add(botonReintentar).size(250, 60).pad(10).row();
        tabla.add(botonMenu).size(250, 60).padBottom(40);
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
        if (visible) {
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
