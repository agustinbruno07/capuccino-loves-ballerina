package com.capuccinolovesballerina.game.mapa;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Nivel {

    private final TiledMap mapa;
    private final GestorColisiones colisiones;

    public Nivel(String ruta, int tamanoTile) {
        mapa = new TmxMapLoader().load(ruta);
        colisiones = new GestorColisiones(mapa, tamanoTile);
    }

    public TiledMap getMapa() {
        return mapa;
    }

    public GestorColisiones getColisiones() {
        return colisiones;
    }

    public Rectangle buscarObjeto(String tipo) {
        if (mapa.getLayers().get("objetos") == null) return null;
        for (MapObject obj : mapa.getLayers().get("objetos").getObjects()) {
            boolean coincide = tipo.equals(obj.getName())
                || tipo.equals(obj.getProperties().get("type"));
            if (coincide && obj instanceof RectangleMapObject) {
                return ((RectangleMapObject) obj).getRectangle();
            }
        }
        return null;
    }

    public Array<RectangleMapObject> buscarObjetos(String tipo) {
        Array<RectangleMapObject> resultado = new Array<>();
        if (mapa.getLayers().get("objetos") == null) return resultado;
        for (MapObject obj : mapa.getLayers().get("objetos").getObjects()) {
            boolean coincide = tipo.equals(obj.getName())
                || tipo.equals(obj.getProperties().get("type"));
            if (coincide && obj instanceof RectangleMapObject) {
                resultado.add((RectangleMapObject) obj);
            }
        }
        return resultado;
    }

    public void dispose() {
        mapa.dispose();
    }
}
