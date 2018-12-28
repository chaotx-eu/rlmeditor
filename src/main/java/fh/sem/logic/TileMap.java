package fh.sem.logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileMap implements Serializable {
    public static final long serialVersionUID = 0;

    private Map<String, Tile> tiles;
    private int width;
    private int height;

    public TileMap(int width, int height) {
        if(width < 1) throw new IllegalArgumentException("width must be greater than 0");
        if(height < 1) throw new IllegalArgumentException("height must be greater than 0");

        tiles = new HashMap<>();
        this.width = width;
        this.height = height;
    }

    public void addTile(Tile tile) {
        int[] pos = tile.getPosition();
        setTile(pos[0], pos[1], pos[2], tile);
    }

    public void setTile(int x, int y, Tile tile) {
        setTile(x, y, 0, tile);
    }

    public void setTile(int x, int y, int z, Tile tile) {
        tile.setPosition(x, y, z);
        tiles.put(hash(x, y, z), tile);
    }

    public void removeTile(Tile tile) {
        int[] pos = tile.getPosition();
        tiles.remove(hash(pos[0], pos[1], pos[2]));
    }

    public Tile getTile(int x, int y) {
        return getTile(x, y, 0);
    }
    
    public Tile getTile(int x, int y, int z) {
        return tiles.get(hash(x, y, z));
    }

    public List<Tile> getTiles(int z) {
        return tiles.values().stream()
            .filter(tile -> tile.getPosition()[2] == z)
            .collect(Collectors.toList());
    }

    public List<Tile> getTiles(int x, int y) {
        return tiles.values().stream()
            .filter(tile -> tile.getPosition()[0] == x && tile.getPosition()[1] == y)
            .collect(Collectors.toList());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected String hash(int x, int y, int z) {
        return x + "|" + y + "|" + z;
    }
}