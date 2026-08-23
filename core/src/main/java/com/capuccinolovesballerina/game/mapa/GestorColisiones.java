package com.capuccinolovesballerina.game.mapa;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Recorre las capas de tiles del mapa y construye la lista de
 * rectangulos solidos a partir de las colisiones (bounding box)
 * definidas en el editor de colisiones de cada tile en Tiled.
 * Es logica pura (sin renderizado): la usara el cliente hoy y
 * el servidor en la etapa de red.
 */
public class GestorColisiones {

    private final Array<Rectangle> solidos = new Array<>();

    public GestorColisiones(TiledMap mapa, int tamanoTile) {
        // Recorre solo las capas de tiles (las de objetos quedan afuera)
        for (TiledMapTileLayer capa : mapa.getLayers().getByType(TiledMapTileLayer.class)) {
            // El fondo no debe colisionar: se saltea sin importar mayusculas
            if ("fondo".equalsIgnoreCase(capa.getName())) continue;

            for (int x = 0; x < capa.getWidth(); x++) {
                for (int y = 0; y < capa.getHeight(); y++) {
                    TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                    if (celda == null) continue; // celda vacia

                    TiledMapTile tile = celda.getTile();
                    // Los objetos llegan como MapObject (superclase); con instanceof
                    // se verifica que sean rectangulos y con el cast se usan como tales
                    for (MapObject obj : tile.getObjects()) {
                        if (obj instanceof RectangleMapObject) {
                            Rectangle r = ((RectangleMapObject) obj).getRectangle();
                            // Posicion del tile en el mundo + caja dentro del tile
                            // (Tiled mide Y hacia abajo dentro del tile; aca se invierte)
                            float mundoX = x * tamanoTile + r.x;
                            float mundoY = y * tamanoTile + (tamanoTile - r.y - r.height);
                            solidos.add(new Rectangle(mundoX, mundoY, r.width, r.height));
                        }
                    }
                }
            }
        }
    }

    public Array<Rectangle> getSolidos() {
        return solidos;
    }

    /** True si el rectangulo dado toca algun solido. */
    public boolean colisiona(Rectangle entidad) {
        for (Rectangle r : solidos) {
            if (r.overlaps(entidad)) return true;
        }
        return false;
    }
}
