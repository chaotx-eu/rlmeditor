package fh.sem.gui.pane;

import java.util.HashMap;
import java.util.Map;

import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import fh.sem.logic.TileSet;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.geometry.*;
import javafx.beans.property.*;

public class MapTilesPane extends VBox {
    public static final float TILE_OPACITY = 0.3f;

    private TileSet tileSet;
    private TileView selection;
    private TextField txf_search = new TextField();
    private Map<Tile, TileView> tileViewMap = new HashMap<>();
    private StringProperty selectedTileTitle = new SimpleStringProperty();
    private BooleanProperty selectionChanged = new SimpleBooleanProperty();
    
    private Map<String, IntegerProperty> catCountMap = new HashMap<>();
    private Map<String, Map<String, IntegerProperty>> subCountMap = new HashMap<>();

    public MapTilesPane(TileSet tileSet) {
        this.tileSet = tileSet;
        
        VBox vbx_main = new VBox();
        ScrollPane scr_tiles = new ScrollPane(vbx_main);
        Label lbl_title = new Label(tileSet.getTitle());
        
        scr_tiles.setFitToWidth(true);
        scr_tiles.setFitToHeight(true);
        scr_tiles.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        VBox.setVgrow(scr_tiles, Priority.ALWAYS);
        
        for(String category : tileSet.getCategories().keySet()) {
            ToggleButton btn_cat = new ToggleButton(category);
            VBox hbx_btn_cat = new VBox(btn_cat, new Separator());
            VBox vbx_cat_con = new VBox();
            VBox vbx_cat_non = new VBox();

            vbx_main.getChildren().add(hbx_btn_cat);
            vbx_main.getChildren().add(vbx_cat_non);
            int i = vbx_main.getChildren().size()-1;
            
            btn_cat.setTextAlignment(TextAlignment.LEFT);
            btn_cat.prefWidthProperty().bind(vbx_main.widthProperty());
            btn_cat.setOnAction(e -> vbx_main.getChildren().set(
                i, btn_cat.isSelected() ? vbx_cat_con : vbx_cat_non));

            IntegerProperty catCount = new SimpleIntegerProperty();
            subCountMap.put(category.toLowerCase(), new HashMap<>());
            catCountMap.put(category.toLowerCase(), catCount);
            catCount.addListener((p,o, n) -> {
                if(n.intValue() == 0)
                    hbx_btn_cat.getChildren().clear();
                else if(hbx_btn_cat.getChildren().isEmpty())
                    hbx_btn_cat.getChildren().setAll(btn_cat, new Separator());
            });

            for(String subCategory : tileSet.getSubCategories().get(category).keySet()) {
                ToggleButton btn_sub = new ToggleButton(subCategory);
                VBox hbx_btn_sub = new VBox(btn_sub, new Separator());
                VBox vbx_sub_con = new VBox();
                VBox vbx_sub_non = new VBox();

                vbx_cat_con.getChildren().add(hbx_btn_sub);
                vbx_cat_con.getChildren().add(vbx_sub_non);
                int j = vbx_cat_con.getChildren().size()-1;

                hbx_btn_sub.setAlignment(Pos.CENTER);
                btn_sub.prefWidthProperty().bind(vbx_main.widthProperty().multiply(0.8f));
                btn_sub.setOnAction(e -> vbx_cat_con.getChildren().set(
                    j, btn_sub.isSelected() ? vbx_sub_con : vbx_sub_non));

                IntegerProperty subCount = new SimpleIntegerProperty();
                subCountMap.get(category.toLowerCase()).put(subCategory.toLowerCase(), subCount);
                subCount.addListener((p, o, n) -> {
                    if(n.intValue() == 0)
                        hbx_btn_sub.getChildren().clear();
                    else if(hbx_btn_sub.getChildren().isEmpty())
                        hbx_btn_sub.getChildren().setAll(btn_sub, new Separator());
                });

                for(Tile tile : tileSet.getSubCategories().get(category).get(subCategory)) {
                    vbx_sub_con.getChildren().add(buildTileView(tile, tileSet));

                    catCount.set(catCount.add(1).get());
                    subCount.set(subCount.add(1).get());
                }
            }

            for(Tile tile : tileSet.getCategories().get(category)) {
                vbx_cat_con.getChildren().add(buildTileView(tile, tileSet));

                catCount.set(catCount.add(1).get());
            }
        }

        for(Tile tile : tileSet.getTopLevelTiles())
            vbx_main.getChildren().add(buildTileView(tile, tileSet));

        txf_search.setPromptText("Search");
        getChildren().setAll(lbl_title, txf_search, scr_tiles);
    }

    public TileView getSelection() {
        return selection;
    }

    public StringProperty selectedTileTitleProperty() {
        return selectedTileTitle;
    }

    public BooleanProperty selectionChangedProperty() {
        return selectionChanged;
    }

    public void setSelectionChanged(boolean changed) {
        selectionChanged.set(changed);
    }

    public boolean isSelectionChanged() {
        return selectionChanged.get();
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public void select(Tile tile) {
        if(selection != null) {
            selection.setOpacity(TILE_OPACITY);
            selection.setScale(1);

        }

        TileView tv;
        if(tile == null || (tv = tileViewMap.get(tile)) == null)
            selection = null;
        else {
            tv.setScale(1.1f);
            tv.setOpacity(1.0f);
            selection = tv;
            selectedTileTitle.set(tile.getTitle());
            setSelectionChanged(true);
        }
    }

    private Pane buildTileView(Tile tile, TileSet tileSet) {
        TileView tv = new TileView(tile);
        tileViewMap.put(tile, tv);

        tv.setOpacity(TILE_OPACITY);
        tv.setPreserveRatio(true);
        tv.fitWidthProperty().bind(
            widthProperty().multiply(2/3f)
            .multiply(tv.scaleProperty()));
        tv.setOnMouseClicked(e -> select(tile));
        Tooltip.install(tv, new Tooltip(tile.getTitle()));

        VBox vbx = new VBox(tv, new Separator());
        vbx.setAlignment(Pos.CENTER);

        txf_search.textProperty().addListener((p, o, n) -> {
            n = n.toLowerCase();
            String tile_title = tile.getTitle().toLowerCase();
            String tile_category = tileSet.getCategory(tile) != null
                ? tileSet.getCategory(tile).toLowerCase() : "";
            String tile_subcategory = tileSet.getSubCategory(tile) != null
                ? tileSet.getSubCategory(tile).toLowerCase() :"";

            String[] names = {tile_title, tile_category, tile_subcategory};
            IntegerProperty catCount = catCountMap.get(tile_category);
            IntegerProperty subCount = subCountMap.get(tile_category) != null
                ? subCountMap.get(tile_category).get(tile_subcategory) : null;

            for(String name : names) {
                if(name.contains(n)) {
                    if(!vbx.getChildren().contains(tv)) {
                        vbx.getChildren().setAll(tv, new Separator());
                        if(catCount != null) catCount.set(catCount.add(1).get());
                        if(subCount != null) subCount.set(subCount.add(1).get());
                    }

                    return;
                }
            }

            if(vbx.getChildren().contains(tv)) {
                vbx.getChildren().clear();
                if(catCount != null) catCount.set(catCount.subtract(1).get());
                if(subCount != null) subCount.set(subCount.subtract(1).get());
            }
        });

        return vbx;
    }
}