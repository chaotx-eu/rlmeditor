package fh.sem.logic;

import java.io.Serializable;
import java.util.Observable;

public class Tile extends Observable implements Serializable {
    public static final long serialVersionUID = 0;

    private int x;
    private int y;
    private int width;
    private int heigth;
    private boolean solid;

    public Tile(int x, int y, int width, int heigth, boolean solid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
        this.solid = solid;
    }

    public Tile(int x, int y, int width, int heigth) {
        this(x, y, width, heigth, false);
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
    
    public int getHeigth() {
        return heigth;
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