package com.capuccinolovesballerina.game.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuego implements Screen {

    private static final float ANCHO_MUNDO = 1280;
    private static final float ALTO_MUNDO = 720;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;


    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(ANCHO_MUNDO, ALTO_MUNDO, camera);
        shapeRenderer = new ShapeRenderer();

        camera.position.set(ANCHO_MUNDO / 2, ALTO_MUNDO / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        actualizar(delta);
        dibujar();
    }

    private void actualizar(float delta) {
    // ACA VA LO QUE SE VA A ACTUALIZAR PERRITOS
        camera.update();

    }

    private void dibujar() {
        //aca lo que se dibuja tipo los sprites y boludeces asi
        ScreenUtils.clear(0.5f, 0.7f, 1f, 1f);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(0, 0, ANCHO_MUNDO, 50);
        shapeRenderer.rect(300, 200, 200, 20);

        shapeRenderer.end();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

}

