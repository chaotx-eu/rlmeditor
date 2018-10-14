package fh.sem.gui.pane;

import java.util.HashMap;
import java.util.Map;

import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import fh.sem.logic.TileSet;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.beans.property.*;

public class MapTilesPane extends VBox {
    private TileView selection;
    private TileSet tileSet;
    private StringProperty selectedTileTitle = new SimpleStringProperty();
    private BooleanProperty selectionChanged = new SimpleBooleanProperty();

    public MapTilesPane(Image sheet, TileSet tileSet) {
        VBox vbx_main = new VBox();
        ScrollPane scr_tiles = new ScrollPane(vbx_main);
        scr_tiles.setFitToWidth(true);
        scr_tiles.setFitToHeight(true);
        
        for(String category : tileSet.getCategories().keySet()) {
            ToggleButton btn_cat = new ToggleButton(category);
            HBox hbx_btn_cat = new HBox(btn_cat);
            VBox vbx_cat_con = new VBox();
            VBox vbx_cat_non = new VBox(new Separator());

            vbx_main.getChildren().add(hbx_btn_cat);
            vbx_main.getChildren().add(vbx_cat_non);
            int i = vbx_main.getChildren().size()-1;
            
            btn_cat.setTextAlignment(TextAlignment.LEFT);
            btn_cat.prefWidthProperty().bind(vbx_main.widthProperty());
            btn_cat.setOnAction(e -> vbx_main.getChildren().set(
                i, btn_cat.isSelected() ? vbx_cat_con : vbx_cat_non));

            for(String subCategory : tileSet.getSubCategories().get(category).keySet()) {
                ToggleButton btn_sub = new ToggleButton(subCategory);
                HBox hbx_btn_sub = new HBox(btn_sub);
                VBox vbx_sub_con = new VBox();
                VBox vbx_sub_non = new VBox(new Separator());

                vbx_cat_con.getChildren().add(hbx_btn_sub);
                vbx_cat_con.getChildren().add(vbx_sub_non);
                int j = vbx_cat_con.getChildren().size()-1;

                hbx_btn_sub.setAlignment(Pos.CENTER);
                btn_sub.prefWidthProperty().bind(vbx_main.widthProperty().multiply(0.8f));
                btn_sub.setOnAction(e -> vbx_cat_con.getChildren().set(
                    j, btn_sub.isSelected() ? vbx_sub_con : vbx_sub_non));

                for(Tile tile : tileSet.getSubCategories().get(category).get(subCategory))
                    vbx_sub_con.getChildren().addAll(
                        buildTileView(tile, sheet, tileSet), new Separator());

                vbx_sub_con.getChildren().addAll(new Separator(), new Separator());
            }

            for(Tile tile : tileSet.getCategories().get(category))
                vbx_cat_con.getChildren().addAll(
                    buildTileView(tile, sheet, tileSet), new Separator());

            vbx_cat_con.getChildren().add(new Separator());
        }

        for(Tile tile : tileSet.getTopLevelTiles())
            vbx_main.getChildren().addAll(
                buildTileView(tile, sheet, tileSet), new Separator());

        getChildren().setAll(scr_tiles);
        this.tileSet = tileSet;
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

    Map<Tile, TileView> tileViewMap = new HashMap<>();
    public void select(Tile tile) {
        if(selection != null)
            selection.setOpacity(0.8f);

        TileView tv;
        if(tile == null || (tv = tileViewMap.get(tile)) == null)
            selection = null;
        else {
            tv.setOpacity(1.0f);
            selection = tv;
            selectedTileTitle.set(tileSet
                .getTileTitles()
                .get(tv.getTile()));
            setSelectionChanged(true);
        }
    }

    private HBox buildTileView(Tile tile, Image sheet, TileSet tileSet) {
        TileView tv = new TileView(tile, sheet);
        tileViewMap.put(tile, tv);

        tv.setOpacity(0.8f);
        tv.setPreserveRatio(true);
        tv.fitWidthProperty().bind(widthProperty().multiply(2/3f));
        tv.setOnMouseClicked(e -> select(tile));

        HBox hbx = new HBox(tv);
        hbx.setAlignment(Pos.CENTER);

        return hbx;
    }
}