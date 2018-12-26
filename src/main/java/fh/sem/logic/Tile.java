package fh.sem.logic;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Observable;

public class Tile extends Observable implements Serializable {
    public static final long serialVersionUID = 0;

    private int x;
    private int y;
    private int width;
    private int height;
    private int rotation;
    private boolean solid;
    private String title, sheet;
    private TileSet tileSet;

    public Tile(String sheet, String title, int x, int y,
    int width, int height, int rotation, boolean solid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.solid = solid;
        this.sheet = sheet;
        this.title = title;
    }
    
    public Tile(String sheet, int x, int y,
    int width, int height, int rotation, boolean solid) {
        this(sheet, "", x, y, width, height, rotation, solid);
    }

    public Tile(String sheet, int x, int y,
    int width, int height, boolean solid) {
        this(sheet, x, y, width, height, 0, solid);
    }

    public Tile(String sheet, int x, int y, int width, int height) {
        this(sheet, x, y, width, height, false);
    }

    public Tile copy() {
        Tile copy = new Tile(sheet, title, x, y, width, height, rotation, solid);
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

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }
}