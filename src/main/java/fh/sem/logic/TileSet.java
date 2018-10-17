package fh.sem.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TileSet implements Serializable {
    public static final long serialVersionUID = 0;

    private String sheet = "";
    private String title = "";
    private Map<Tile, String> tileTitles = new HashMap<>();
    private List<Tile> topLevelTiles = new ArrayList<>();
    private Map<String, List<Tile>> categories = new HashMap<>();
    private Map<String, Map<String, List<Tile>>> subCategories = new HashMap<>();

    public void addTile(String title, String category, String subCategory, Tile tile) {
        if(!categories.containsKey(category))
            categories.put(category, new ArrayList<>());

        if(!subCategories.containsKey(category))
            subCategories.put(category, new HashMap<>());

        if(!subCategories.get(category).containsKey(subCategory))
            subCategories.get(category).put(subCategory, new ArrayList<>());

        subCategories.get(category).get(subCategory).add(tile);
        tileTitles.put(tile, title);
    }

    public void addTile(String title, String category, Tile tile) {
        if(!categories.containsKey(category)) {
            categories.put(category, new ArrayList<>());
            subCategories.put(category, new HashMap<>());
        }

        categories.get(category).add(tile);
        tileTitles.put(tile, title);
    }

    public void addTile(String title, Tile tile) {
        topLevelTiles.add(tile);
        tileTitles.put(tile, title);
    }

    public void addTile(Tile tile) {
        addTile("Tile_" + topLevelTiles.size(), tile);
    }

    public Map<Tile, String> getTileTitles() {
        return tileTitles;
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

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getSheet() {
        return sheet;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}