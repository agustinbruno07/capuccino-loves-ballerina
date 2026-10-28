package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private TextButton botonModoPantalla;

    private Label etiquetaResolucion;
    private SelectBox<String> selectorResolucion;

    private boolean silenciado;

    private static float ultimoVolumen = 0.07f;

    private static String resolucionVentana = "1280 x 720";

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

        etiquetaResolucion = new Label(
            "Resolucion de ventana", skin
        );

        selectorResolucion = new SelectBox<>(skin);

        selectorResolucion.setItems("1280 x 720", "1024 x 768", "800 x 600");

        selectorResolucion.setSelected(resolucionVentana);

        selectorResolucion.setDisabled(Gdx.graphics.isFullscreen());

        selectorResolucion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!Gdx.graphics.isFullscreen()) {
                    resolucionVentana = selectorResolucion.getSelected();
                    aplicarResolucionVentana(resolucionVentana);
                }
            }
        });

        botonModoPantalla = new TextButton("", skin);

        actualizarTextoBotonModoPantalla();

        botonModoPantalla.addListener(new ClickListener() {
            @Override
            public void clicked(
                InputEvent event, float x, float y
            ) {
                alternarModoPantalla();
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

        tabla.add(titulo).padBottom(30).row();
        tabla.add(etiquetaVolumen).padBottom(10).row();
        tabla.add(volumenMusica).width(350).padBottom(20).row();
        tabla.add(botonSilenciar).size(260, 55).padBottom(12).row();
        tabla.add(etiquetaResolucion).padBottom(8).row();
        tabla.add(selectorResolucion).width(260).height(45).padBottom(15).row();
        tabla.add(botonModoPantalla).size(260, 55).padBottom(12).row();
        tabla.add(botonVolver).size(200, 55);

        stage.addActor(tabla);

        Gdx.input.setInputProcessor(stage);
    }

    private void prepararResolucionVentana() {

        if (Gdx.graphics.isFullscreen()) {
            return;
        }

        String resolucionActual = Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight();

        if (esResolucionPermitida(resolucionActual)) {
            resolucionVentana = resolucionActual;
        } else {
            resolucionVentana = "1280 x 720";
            aplicarResolucionVentana(resolucionVentana);
        }
    }

    private boolean esResolucionPermitida(String resolucion) {
        String normalizada = resolucion.replace(" ", "");
        return normalizada.equals("1280x720") || normalizada.equals("1024x768") || normalizada.equals("800x600");
    }

    private void aplicarResolucionVentana(String resolucion) {

        String[] dimensiones = resolucion
            .replace(" ", "")
            .split("x");

        int ancho = Integer.parseInt(dimensiones[0]);
        int alto = Integer.parseInt(dimensiones[1]);

        Gdx.graphics.setWindowedMode(ancho, alto);
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
        int porcentaje = Math.round(volumenMusica.getValue() * 100);
        etiquetaVolumen.setText("Volumen de musica: " + porcentaje + "%");
    }

    private void actualizarTextoBoton() {
        if (silenciado) {
            botonSilenciar.setText("ACTIVAR MUSICA");
        } else {
            botonSilenciar.setText("SILENCIAR MUSICA");
        }
    }

    private void alternarModoPantalla() {

        if (Gdx.graphics.isFullscreen()) {
            aplicarResolucionVentana(resolucionVentana);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        selectorResolucion.setDisabled(Gdx.graphics.isFullscreen());
        actualizarTextoBotonModoPantalla();
    }

    private void actualizarTextoBotonModoPantalla() {

        if (Gdx.graphics.isFullscreen()) {
            botonModoPantalla.setText("VENTANA");
        } else {
            botonModoPantalla.setText("PANTALLA COMPLETA");
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
