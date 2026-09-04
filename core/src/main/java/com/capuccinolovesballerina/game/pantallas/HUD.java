package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.capuccinolovesballerina.game.Utilidades.Constantes;
import com.capuccinolovesballerina.game.Utilidades.FabricaViewport;
import com.capuccinolovesballerina.game.Utilidades.Recursos;


public class HUD {

    private final Stage stage;
    private final Viewport viewport;
    private final Label labelTiempo;
    private final Texture texturaMarco;

    private float tiempo;
    private int ultimoSegundo = -1;

    public HUD() {
        viewport = FabricaViewport.crear();
        stage = new Stage(viewport);
        Skin skin = Recursos.getSkin();
        skin.getFont("default-font").getRegion().getTexture()
            .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        texturaMarco = Recursos.getImagen("interfaz/marco_hud.png");
        Image imagenMarco = new Image(texturaMarco);

        LabelStyle estiloGrande = new LabelStyle(skin.get(LabelStyle.class));
        estiloGrande.font = skin.getFont("default-font");
        labelTiempo = new Label("00:00", estiloGrande);
        labelTiempo.setFontScale(1.5f);
        labelTiempo.setAlignment(Align.center);

        Stack pila = new Stack();
        float anchoMarco = 250f;
        float altoMarco = 80f;
        pila.setSize(anchoMarco, altoMarco);
        pila.add(imagenMarco);
        pila.add(labelTiempo);

        pila.setPosition(
            (Constantes.ANCHO_MUNDO - anchoMarco) / 2f,
            Constantes.ALTO_MUNDO - altoMarco
        );

        stage.addActor(pila);

    }

    public void actualizar(float delta) {
        tiempo += delta;
        int segundo = (int) tiempo;
        if (segundo != ultimoSegundo) {
            ultimoSegundo = segundo;
            labelTiempo.setText(formatear(segundo));
        }
    }

    private String formatear(int totalSegundos) {
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    public void dibujar() {
        stage.draw();
    }

    public void reiniciar() {
        tiempo = 0f;
        ultimoSegundo = -1;
        labelTiempo.setText("00:00");
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
