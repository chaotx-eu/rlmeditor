package fh.sem.logic;

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

    public Tile(int x, int y, int width, int height, int rotation, boolean solid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.solid = solid;
    }

    public Tile(int x, int y, int width, int height, boolean solid) {
        this(x, y, width, height, 0, solid);
    }

    public Tile(int x, int y, int width, int height) {
        this(x, y, width, height, false);
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

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
        setChanged();
        notifyObservers();
    }
}