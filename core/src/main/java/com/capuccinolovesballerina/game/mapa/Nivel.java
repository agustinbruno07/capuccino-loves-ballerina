package com.capuccinolovesballerina.game.mapa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Nivel {

    /**
     * Escala entre "pixeles de Tiled" y "unidades del mundo".
     * 1f = el mundo mide lo mismo que el mapa en Tiled (30x20 tiles de 32 = 960x640).
     * Es como esta disenado NIVEL 0 (puerta 96x128, peligro 64x32, etc.).
     * Si algun dia queres un mundo mas grande sin retocar el mapa, cambia SOLO este numero.
     */
    public static final float ESCALA = 1f;

    /** true = los tiles se dibujan sin suavizar (pixel art). */
    private static final boolean PIXEL_ART = false;

    /** Dejalo en true hasta que todo funcione; despues ponelo en false. */
    private static final boolean DEBUG_OBJETOS = true;

    private final TiledMap mapa;
    private final GestorColisiones colisiones;
    private final Array<MapObject> objetos = new Array<>();
    private final float anchoPx;
    private final float altoPx;

    public Nivel(String ruta) {
        TmxMapLoader.Parameters parametros = new TmxMapLoader.Parameters();
        if (PIXEL_ART) {
            parametros.generateMipMaps = false;
            parametros.textureMinFilter = Texture.TextureFilter.Nearest;
            parametros.textureMagFilter = Texture.TextureFilter.Nearest;
        }
        mapa = new TmxMapLoader().load(ruta, parametros);

        MapProperties props = mapa.getProperties();
        int tileAncho = props.get("tilewidth", Integer.class);
        int tileAlto = props.get("tileheight", Integer.class);
        int tilesX = props.get("width", Integer.class);
        int tilesY = props.get("height", Integer.class);

        anchoPx = tilesX * tileAncho * ESCALA;
        altoPx = tilesY * tileAlto * ESCALA;

        // Se leen los objetos de TODAS las capas de objetos, se llamen como se llamen
        // (Tiled las crea como "Capa de Objetos 1" por defecto).
        for (MapLayer capa : mapa.getLayers()) {
            for (MapObject obj : capa.getObjects()) {
                objetos.add(obj);
            }
        }

        colisiones = new GestorColisiones(mapa, tileAncho, ESCALA);

        if (DEBUG_OBJETOS) {
            logObjetos();
        }
    }

    public TiledMap getMapa() {
        return mapa;
    }

    public GestorColisiones getColisiones() {
        return colisiones;
    }

    /** Ancho total del mapa en unidades del mundo. */
    public float getAnchoPx() {
        return anchoPx;
    }

    /** Alto total del mapa en unidades del mundo. */
    public float getAltoPx() {
        return altoPx;
    }

    /** Primer objeto con ese nombre/type/class, en unidades del mundo (o null). */
    public Rectangle buscarObjeto(String tipo) {
        for (MapObject obj : objetos) {
            if (!coincide(obj, tipo)) continue;
            Rectangle r = rectanguloDe(obj);
            if (r != null) return r;
        }
        if (DEBUG_OBJETOS) {
            Gdx.app.log("NIVEL", "NO se encontro el objeto '" + tipo + "' (revisa name/type en Tiled)");
        }
        return null;
    }

    /** Todos los objetos con ese nombre/type/class, en unidades del mundo. */
    public Array<Rectangle> buscarObjetos(String tipo) {
        Array<Rectangle> resultado = new Array<>();
        for (MapObject obj : objetos) {
            if (!coincide(obj, tipo)) continue;
            Rectangle r = rectanguloDe(obj);
            if (r != null) resultado.add(r);
        }
        return resultado;
    }

    public void dispose() {
        mapa.dispose();
    }

    // ---------------------------------------------------------------- internos

    /** Compara con name, type o class (Tiled 1.9+), ignorando mayusculas y espacios. */
    private static boolean coincide(MapObject obj, String tipo) {
        MapProperties p = obj.getProperties();
        return igual(tipo, obj.getName()) || igual(tipo, p.get("type")) || igual(tipo, p.get("class"));
    }

    private static boolean igual(String tipo, Object valor) {
        return valor != null && tipo.equalsIgnoreCase(valor.toString().trim());
    }

    /** Convierte el objeto de Tiled a un rectangulo en unidades del mundo. */
    private static Rectangle rectanguloDe(MapObject obj) {
        if (obj instanceof RectangleMapObject) {
            Rectangle r = ((RectangleMapObject) obj).getRectangle();
            return new Rectangle(r.x * ESCALA, r.y * ESCALA, r.width * ESCALA, r.height * ESCALA);
        }
        // Objetos puestos con "Insertar tile"
        if (obj instanceof TiledMapTileMapObject) {
            MapProperties p = obj.getProperties();
            return new Rectangle(
                numero(p, "x") * ESCALA,
                numero(p, "y") * ESCALA,
                numero(p, "width") * ESCALA,
                numero(p, "height") * ESCALA
            );
        }
        return null;
    }

    private static float numero(MapProperties p, String clave) {
        Object v = p.get(clave);
        return v instanceof Number ? ((Number) v).floatValue() : 0f;
    }

    private void logObjetos() {
        Gdx.app.log("NIVEL", "Mundo: " + anchoPx + " x " + altoPx + " (escala " + ESCALA + "), "
            + objetos.size + " objetos");
        for (MapObject obj : objetos) {
            MapProperties p = obj.getProperties();
            Gdx.app.log("NIVEL", obj.getClass().getSimpleName()
                + " name=" + obj.getName()
                + " type=" + p.get("type")
                + " x=" + p.get("x") + " y=" + p.get("y")
                + " w=" + p.get("width") + " h=" + p.get("height"));
        }
    }
}
