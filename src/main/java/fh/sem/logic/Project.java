package fh.sem.logic;

import java.io.Serializable;

public class Project implements Serializable {
    public static final long serialVersionUID = 0;
    
    private String title;
    private TileSet tileSet;
    private TileMap tileMap;

    public Project(String title, TileSet tileSet, TileMap tileMap) {
        this.title = title;
        this.tileSet = tileSet;
        this.tileMap = tileMap;
    }

    public String getTitle() {
        return title;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}