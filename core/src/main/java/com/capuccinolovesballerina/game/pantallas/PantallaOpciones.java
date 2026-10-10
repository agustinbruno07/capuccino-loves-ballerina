package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;

import com.capuccinolovesballerina.game.CapuccinoLovesBallerinaGame;
import com.capuccinolovesballerina.game.Utilidades.Audio;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;

public class PantallaOpciones implements Screen {

    private final CapuccinoLovesBallerinaGame juego;

    private Stage stage;

    private Slider volumenMusica;
    private Label etiquetaVolumen;
    private TextButton botonSilenciar;

    private boolean silenciado;

    private static float ultimoVolumen = 0.07f;

    public PantallaOpciones(CapuccinoLovesBallerinaGame juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        stage = new Stage(FabricaViewport.crear());

        Skin skin = Recursos.getSkin();

        if (Audio.getVolumen() > 0f) {
            ultimoVolumen = Audio.getVolumen();
            silenciado = false;
        } else {
            silenciado = true;
        }

        Image fondo = new Image(
            Recursos.getImagen("interfaz/fondo_menu.png")
        );

        fondo.setFillParent(true);
        stage.addActor(fondo);

        Label titulo = new Label("OPCIONES", skin);
        titulo.setFontScale(2f);

        etiquetaVolumen = new Label("", skin);

        volumenMusica = new Slider(0f, 1f, 0.01f, false, skin);

        volumenMusica.setValue(ultimoVolumen);

        actualizarEtiquetaVolumen();

        volumenMusica.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                float nuevoVolumen = volumenMusica.getValue();

                if (nuevoVolumen <= 0f) {

                    silenciado = true;
                    Audio.setVolumen(0f);

                } else {

                    ultimoVolumen = nuevoVolumen;
                    silenciado = false;

                    Audio.setVolumen(nuevoVolumen);
                }

                actualizarEtiquetaVolumen();
                actualizarTextoBoton();
            }
        });
        botonSilenciar = new TextButton("", skin);

        actualizarTextoBoton();

        botonSilenciar.addListener(new ClickListener() {
            @Override
            public void clicked(
                InputEvent event, float x, float y
            ) {
                alternarSilencio();
            }
        });

        TextButton botonVolver = new TextButton("VOLVER", skin);

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
        tabla.center();

        tabla.add(titulo).padBottom(35).row();

        tabla.add(etiquetaVolumen).padBottom(10).row();

        tabla.add(volumenMusica).width(350).padBottom(25).row();

        tabla.add(botonSilenciar).size(260, 60).padBottom(15).row();

        tabla.add(botonVolver).size(200, 60);

        stage.addActor(tabla);

        Gdx.input.setInputProcessor(stage);
    }

    private void alternarSilencio() {

        if (silenciado) {

            silenciado = false;

            Audio.setVolumen(ultimoVolumen);

            volumenMusica.setValue(ultimoVolumen);

        } else {

            if (Audio.getVolumen() > 0f) {
                ultimoVolumen = Audio.getVolumen();
            }

            silenciado = true;

            Audio.setVolumen(0f);
        }

        actualizarEtiquetaVolumen();
        actualizarTextoBoton();
    }

    private void actualizarEtiquetaVolumen() {

        int porcentaje = Math.round(
            volumenMusica.getValue() * 100
        );

        etiquetaVolumen.setText(
            "Volumen de musica: " + porcentaje + "%"
        );
    }

    private void actualizarTextoBoton() {

        if (silenciado) {
            botonSilenciar.setText("ACTIVAR MUSICA");
        } else {
            botonSilenciar.setText("SILENCIAR MUSICA");
        }
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

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
    }

    @Override
    public void dispose() {

        if (stage != null) {
            stage.dispose();
        }
    }
}
