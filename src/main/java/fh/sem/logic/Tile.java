package fh.sem.logic;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Observable;

public class Tile extends Observable implements Serializable {
    public static final long serialVersionUID = 0;

    // sprite attributes
    private int x;
    private int y;
    private int width;
    private int height;
    private String title, sheet;
    private TileSet tileSet;

    // map attributes
    private int mapX;
    private int mapY;
    private int mapZ;
    private int rotation;
    private boolean solid;

    public Tile(String sheet, String title, int x, int y,
    int width, int height, int rotation, int layer, boolean solid) {
        this.x = x;
        this.y = y;
        this.mapZ = layer;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.solid = solid;
        this.sheet = sheet;
        this.title = title;
    }
    
    public Tile(String sheet, int x, int y, int width, int height,
    int rotation, int layer, boolean solid) {
        this(sheet, "", x, y, width, height, rotation, layer, solid);
    }

    public Tile(String sheet, int x, int y,
    int width, int height, int layer, boolean solid) {
        this(sheet, x, y, width, height, 0, layer, solid);
    }

    public Tile(String sheet, int x, int y, int width, int height) {
        this(sheet, x, y, width, height, 0, 0, false);
    }

    public Tile copy() {
        Tile copy = new Tile(sheet, title, x, y, width, height, rotation, mapZ, solid);
        copy.setTileSet(tileSet);
        return copy;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getRotation() {
        return rotation;
    }

    public int[] getPosition() {
        return new int[]{mapX, mapY, mapZ};
    }

    public int getLayer() {
        return mapZ;
    }

    public String getSheet() {
        return sheet;
    }

    public String getTitle() {
        return title;
    }

    public Image getImage() {
        return tileSet != null ?
            tileSet.getImage(sheet) : null;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
        setChanged();
        notifyObservers();
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        setChanged();
        notifyObservers();
    }

    public void setLayer(int layer) {
        mapZ = layer;
        setChanged();
        notifyObservers();
    }

    public void setPosition(int... pos) {
        if(pos.length != 2 && pos.length != 3)
            throw new IllegalArgumentException("2 or 3 arguments required (x, y[, z])");

        mapX = pos[0];
        mapY = pos[1];
        mapZ = pos.length > 2 ? pos[2] : 0;

        setChanged();
        notifyObservers();
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    @Override
    public int hashCode() {
        return (sheet + "_" + x + "|" + y + "|" + width + "|" + height).hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return hashCode() == other.hashCode();
    }
}