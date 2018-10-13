package fh.sem.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileSet {
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

    public Map<String, Map<String, List<Tile>>> getSubCategories() {
        return subCategories;
    }
}