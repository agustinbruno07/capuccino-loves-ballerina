package com.capuccinolovesballerina.game.graficos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimacionesCappuccino {

    public enum Estado { IDLE, WALK, JUMP, ATTACK }

    private static final int FRAME = 128;
    private static final int FRAMES_IDLE = 4;
    private static final int FRAMES_WALK = 9;
    private static final int FRAMES_JUMP = 3;
    private static final int FRAMES_ATTACK = 7;

    private final Texture textura;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> walk;
    private final Animation<TextureRegion> jump;
    private final Animation<TextureRegion> attack;

    private Estado estado = Estado.IDLE;
    private float tiempo = 0f;

    public AnimacionesCappuccino() {
        textura = new Texture("sprites/cappuccino.png");
        // Corta la hoja en una grilla [fila][columna]
        TextureRegion[][] grilla = TextureRegion.split(textura, FRAME, FRAME);

        idle   = new Animation<>(0.2f,  primeros(grilla[0], FRAMES_IDLE));
        idle.setPlayMode(Animation.PlayMode.LOOP);

        walk   = new Animation<>(0.1f,  primeros(grilla[1], FRAMES_WALK));
        walk.setPlayMode(Animation.PlayMode.LOOP);

        jump   = new Animation<>(0.15f, primeros(grilla[2], FRAMES_JUMP));
        jump.setPlayMode(Animation.PlayMode.NORMAL);

        attack = new Animation<>(0.07f, primeros(grilla[3], FRAMES_ATTACK));
        attack.setPlayMode(Animation.PlayMode.NORMAL);
    }


    private TextureRegion[] primeros(TextureRegion[] fila, int n) {
        TextureRegion[] frames = new TextureRegion[n];
        for (int i = 0; i < n; i++) frames[i] = fila[i];
        return frames;
    }


    public void setEstado(Estado nuevo) {
        if (estado == Estado.ATTACK && !attack.isAnimationFinished(tiempo)) return;
        if (nuevo != estado) {
            estado = nuevo;
            tiempo = 0f; // arranca desde el frame 0
        }
    }

    public void actualizar(float delta) { // delta es el tiempo que pasó desde el frame anterior
        tiempo += delta; // sumar delta hace que la animación avance independiente de los FPS.
    }

    public void reiniciar() {
        estado = Estado.IDLE;
        tiempo = 0f;
    }


    public TextureRegion getFrameActual() {
        switch (estado) {
            case WALK:   return walk.getKeyFrame(tiempo);
            case JUMP:   return jump.getKeyFrame(tiempo);
            case ATTACK: return attack.getKeyFrame(tiempo);
            default:     return idle.getKeyFrame(tiempo);
        }
    }

    public void dispose() {
        textura.dispose();
    }
}
