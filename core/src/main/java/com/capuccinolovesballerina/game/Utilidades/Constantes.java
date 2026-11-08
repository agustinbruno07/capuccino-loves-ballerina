
package com.capuccinolovesballerina.game.Utilidades;

public class Constantes {

    public static final float ANCHO_MUNDO = 1280;
    public static final float ALTO_MUNDO = 720;
    public static final String RUTA_SKIN = "skin/uiskin.json";

    public static final float ESCALA_PERSONAJE = 2f / 3f;

    public static final float ANCHO_PERSONAJE = 48f * ESCALA_PERSONAJE;
    public static final float ALTO_PERSONAJE = 64f * ESCALA_PERSONAJE;

    public static final float ESCALA_PALANCA = 0.5f;   // 1f = tamaño original (48x56)
    public static final float ANCHO_PALANCA = 48f * ESCALA_PALANCA;   // 24
    public static final float ALTO_PALANCA = 56f * ESCALA_PALANCA;    // 28

    public static final float TAM_SPRITE_PERSONAJE = 96f * ESCALA_PERSONAJE;
    public static final float VELOCIDAD_MOVIMIENTO = 370 * ESCALA_PERSONAJE;
    public static final float VELOCIDAD_SALTO =  750f * ESCALA_PERSONAJE;
    public static final float GRAVEDAD = 2200f * ESCALA_PERSONAJE;
    public static final float VELOCIDAD_MAX_CAIDA = 1300f * ESCALA_PERSONAJE;

    public static final float ANCHO_ESPUMA = 64f * ESCALA_PERSONAJE;
    public static final float ALTO_ESPUMA = 24f * ESCALA_PERSONAJE;
    public static final float VIDA_ESPUMA = 4f;
    public static final float COOLDOWN_ESPUMA = 2.5f;
    public static final float DISTANCIA_ESPUMA = 8f * ESCALA_PERSONAJE;

    public static final float MARGEN_LATERAL_PELIGRO = 8f;   // se resta a cada lado
    public static final float MARGEN_SUPERIOR_PELIGRO = 8f;  // se resta de arriba

}
