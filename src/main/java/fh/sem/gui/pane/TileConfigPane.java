package fh.sem.gui.pane;

import fh.sem.gui.control.Selector;
import fh.sem.logic.Tile;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


public class TileConfigPane extends VBox {
    public TileConfigPane(MapTilesPane mapTilesPane) {
        Label lbl_tile_title = new Label();
        Label lbl_tile_coords = new Label();
        Label lbl_tile_size = new Label();
        Label lbl_tile_solid = new Label("Solid:");
        Label lbl_tile_rotat = new Label("Rotation:");

        CheckBox chb_tile_solid = new CheckBox();
        Selector sel_tile_rotat = new Selector(true, "  0\u00b0", " 90\u00b0", "180\u00b0", "270\u00b0");

        lbl_tile_title.setStyle("-fx-font-weight:bold;");
        lbl_tile_solid.setPadding(new Insets(5f, 0f, 0f, 0f));

        mapTilesPane.selectionChangedProperty().addListener((p, o, n) -> {
            if(n.booleanValue()) {
                Tile tile = mapTilesPane.getSelection().getTile();
                lbl_tile_title.setText(mapTilesPane.selectedTileTitleProperty().get());
                lbl_tile_coords.setText("X: " + tile.getX() + ", Y: " + tile.getY());
                lbl_tile_size.setText("W: " + tile.getWidth() + ", H: " + tile.getHeight());

                chb_tile_solid.setSelected(tile.isSolid());
                sel_tile_rotat.select(tile.getRotation() + "\u00b0");
                mapTilesPane.setSelectionChanged(false);
            }
        });

        chb_tile_solid.selectedProperty().addListener((p, o, n) -> {
            if(mapTilesPane.getSelection() != null)
                mapTilesPane.getSelection().getTile().setSolid(n);
        });

        sel_tile_rotat.textProperty().addListener((p, o, n) -> {
            if(mapTilesPane.getSelection() != null) {
                String rotStr = n.substring(0, n.length()-1).trim();
                int rot = Integer.parseInt(rotStr);
                
                mapTilesPane.getSelection().setRotate(rot);
                mapTilesPane.getSelection().getTile().setRotation(rot);
            }
        });

        HBox hbx_solid_cb = new HBox(chb_tile_solid);
        HBox hbx_rotat_sl = new HBox(sel_tile_rotat);
        HBox hbx_rotat_lb = new HBox(lbl_tile_rotat);
        HBox hbx_solid = new HBox(lbl_tile_solid, hbx_solid_cb);
        HBox hbx_rotat = new HBox(hbx_rotat_lb, hbx_rotat_sl);
        hbx_solid.setSpacing(5f);

        HBox.setHgrow(hbx_solid_cb, Priority.ALWAYS);
        HBox.setHgrow(hbx_rotat_sl, Priority.ALWAYS);
        hbx_solid_cb.setAlignment(Pos.CENTER_RIGHT);
        hbx_rotat_sl.setAlignment(Pos.CENTER_RIGHT);
        hbx_rotat_lb.setAlignment(Pos.CENTER_LEFT);

        setSpacing(5f);
        setFillWidth(true);
        setPadding(new Insets(10f));
        getChildren().addAll(lbl_tile_title, lbl_tile_coords, lbl_tile_size,
            /* new Separator() ,*/ hbx_solid, hbx_rotat); // TODO style separator
    }
}