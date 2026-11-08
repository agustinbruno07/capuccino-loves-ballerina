package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.capuccinolovesballerina.game.Utilidades.Audio;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;
import com.capuccinolovesballerina.game.red.Cliente;

public class PantallaMenu implements Screen {

    private Image fondo;

    private Stage stage;
    private Skin skin;
    private TextButton botonJugar;
    private TextButton botonJugarRed;
    private TextButton botonOpciones;
    private TextButton botonControles;
    private TextButton botonSalir;
    private Label estadoRed;
    private final Game juego;

    private Cliente clienteRed;

    public PantallaMenu(Game juego) {
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

    private void crearBotones() {
        botonJugar = new TextButton("JUGAR LOCAL", skin);
        botonJugarRed = new TextButton("JUGAR EN RED", skin);
        botonOpciones = new TextButton("OPCIONES", skin);
        botonControles = new TextButton("CONTROLES", skin);
        botonSalir = new TextButton("SALIR", skin);
        estadoRed = new Label("", skin);
    }

    private void agregarBotones() {

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.bottom();

        tabla.add(estadoRed).size(400, 30).pad(5).row();
        tabla.add(botonJugar).size(200, 60).pad(10).row();
        tabla.add(botonJugarRed).size(200, 60).pad(10).row();
        tabla.add(botonOpciones).size(200, 60).pad(10).row();
        tabla.add(botonControles).size(200, 60).pad(10).row();
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

        botonJugarRed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buscarServidor();
            }
        });

        botonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        botonControles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaControles(juego));
            }
        });

        botonOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaOpciones(juego));
            }
        });
    }

    private void buscarServidor() {
        botonJugarRed.setDisabled(true);
        estadoRed.setText("Buscando servidor en la red...");
        final PantallaMenu self = this;
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                Cliente c = new Cliente();
                final boolean ok = c.conectar();
                final int rol = c.getMiRol();
                final String ip = c.getIpServer();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (ok) {
                            clienteRed = c;
                            String nombre = (rol == 0) ? "Cappuccino" : "Ballerina";
                            estadoRed.setText("Conectado a " + ip + " como " + nombre);
                        } else {
                            c.cerrar();
                            estadoRed.setText("No se encontro servidor. Levantá Servidor.main");
                        }
                        botonJugarRed.setDisabled(false);
                    }
                });
            }
        });
        hilo.start();
    }

    public Cliente getClienteRed() {
        return clienteRed;
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
