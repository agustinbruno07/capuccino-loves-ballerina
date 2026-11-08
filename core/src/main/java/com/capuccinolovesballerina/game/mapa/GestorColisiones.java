package com.capuccinolovesballerina.game.mapa;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class GestorColisiones {

    private final Array<Rectangle> solidos = new Array<>();

    /**
     * @param tamanoTile tamano del tile en pixeles de Tiled (por ejemplo 32)
     * @param escala     factor para pasar de pixeles de Tiled a unidades del mundo (Nivel.ESCALA)
     */
    public GestorColisiones(TiledMap mapa, int tamanoTile, float escala) {
        for (TiledMapTileLayer capa : mapa.getLayers().getByType(TiledMapTileLayer.class)) {
            if (capa.getName().toLowerCase().startsWith("fondo")) continue;
            for (int x = 0; x < capa.getWidth(); x++) {
                for (int y = 0; y < capa.getHeight(); y++) {
                    TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                    if (celda == null) continue; // celda vacia

                    TiledMapTile tile = celda.getTile();

                    for (MapObject obj : tile.getObjects()) {
                        if (obj instanceof RectangleMapObject) {
                            Rectangle r = ((RectangleMapObject) obj).getRectangle();

                            // Se calcula en pixeles de Tiled y despues se escala todo junto.
                            float mundoX = x * tamanoTile + r.x;
                            float mundoY = y * tamanoTile + (tamanoTile - r.y - r.height);
                            solidos.add(new Rectangle(
                                mundoX * escala,
                                mundoY * escala,
                                r.width * escala,
                                r.height * escala
                            ));
                        }
                    }
                }
            }
        }
    }

    public Array<Rectangle> getSolidos() {
        return solidos;
    }

    public boolean colisiona(Rectangle entidad) {
        for (Rectangle r : solidos) {
            if (r.overlaps(entidad)) return true;
        }
        return false;
    }
}
