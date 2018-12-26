package fh.sem.logic;

import java.io.Serializable;

public class TileMap implements Serializable {
    public static final long serialVersionUID = 0;

    private Tile[][] map;

    public TileMap(int width, int height) {
        if(width < 1) throw new IllegalArgumentException("width must be greater than 0");
        if(height < 1) throw new IllegalArgumentException("height must be greater than 0");

        map = new Tile[height][];
        for(--height; height >= 0; --height)
            map[height] = new Tile[width];
    }

    public void setTile(int x, int y, Tile tile) {
        map[y][x] = tile;
    }

    public Tile getTile(int x, int y) {
        return map[y][x];
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }
}