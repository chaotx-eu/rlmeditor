package fh.sem.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import fh.sem.App;
import javafx.scene.image.Image;

public class TileSet implements Serializable {
    public static final long serialVersionUID = 0;

    private String title = "";
    private List<Tile> topLevelTiles = new ArrayList<>();
    private Map<String, List<Tile>> categories = new HashMap<>();
    private Map<String, Map<String, List<Tile>>> subCategories = new HashMap<>();
    private Map<String, Image> imageMap = new HashMap<>();

    public void addTile(Tile tile, String category, String subCategory) {
        if(!categories.containsKey(category))
        categories.put(category, new ArrayList<>());
        
        if(!subCategories.containsKey(category))
        subCategories.put(category, new HashMap<>());
        
        if(!subCategories.get(category).containsKey(subCategory))
        subCategories.get(category).put(subCategory, new ArrayList<>());
        
        subCategories.get(category).get(subCategory).add(tile);
        initTile(tile);
    }

    public void addTile(Tile tile, String category) {
        if(!categories.containsKey(category)) {
            categories.put(category, new ArrayList<>());
            subCategories.put(category, new HashMap<>());
        }

        categories.get(category).add(tile);
        initTile(tile);
    }

    public void addTile(Tile tile) {
        topLevelTiles.add(tile);
        initTile(tile);
    }

    public List<Tile> getTopLevelTiles() {
        return topLevelTiles;
    }

    public Map<String, List<Tile>> getCategories() {
        return categories;
    }

    public String getCategory(Tile tile) {
        return categories.keySet().stream()
            .filter(k -> categories.get(k).contains(tile))
            .findAny().orElse(getCategory(getSubCategory(tile)));
    }

    public String getCategory(String subCategory) {
        try {
            return categories.keySet().stream()
                .filter(k -> subCategories.get(k)
                    .keySet().contains(subCategory))
                .findAny().get();
        } catch(NoSuchElementException e) { return null; }
    }

    public String getSubCategory(Tile tile) {
            for(Map<String, List<Tile>> m : subCategories.values())
                for(String k : m.keySet())
                    if(m.get(k).contains(tile))
                        return k;

            return null;
    }

    public Map<String, Map<String, List<Tile>>> getSubCategories() {
        return subCategories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Image getImage(String path) {
        return imageMap.get(path);
    }

    @Override
    public String toString() {
        return getTitle();
    }

    protected void initTile(Tile tile) {
        tile.setTileSet(this);
        if(!imageMap.containsKey(tile.getSheet())) {
            imageMap.put(tile.getSheet(), new Image(App
                .resourceManager
                .get(tile.getSheet())));
        }
    }
}