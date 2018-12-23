package fh.sem.gui.pane;

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
        ChoiceBox<String> chb_tile_solid = new ChoiceBox<>();
        ChoiceBox<String> chb_tile_rotat = new ChoiceBox<>();

        lbl_tile_title.setStyle("-fx-font-weight:bold;");
        lbl_tile_solid.setPadding(new Insets(5f, 0f, 0f, 0f));

        mapTilesPane.selectionChangedProperty().addListener((p, o, n) -> {
            if(n.booleanValue()) {
                Tile tile = mapTilesPane.getSelection().getTile();
                lbl_tile_title.setText(mapTilesPane.selectedTileTitleProperty().get());
                lbl_tile_coords.setText("X: " + tile.getX() + ", Y: " + tile.getY());
                lbl_tile_size.setText("W: " + tile.getWidth() + ", H: " + tile.getHeight());
                chb_tile_solid.getSelectionModel().select(tile.isSolid() ? 0 : 1);
                chb_tile_rotat.getSelectionModel().select(tile.getRotation() + "deg");
                mapTilesPane.setSelectionChanged(false);
            }
        });

        chb_tile_solid.getItems().addAll("True", "False");
        chb_tile_rotat.getItems().addAll("0deg", "90deg", "180deg", "270deg");

        chb_tile_solid.setOnAction(e -> {
            if(mapTilesPane.getSelection() != null) {
                mapTilesPane.getSelection().getTile()
                    .setSolid(chb_tile_solid
                        .getSelectionModel()
                        .getSelectedItem().equals("True"));
            }
        });

        chb_tile_rotat.setOnAction(e -> {
            if(mapTilesPane.getSelection() != null) {
                String rotStr = chb_tile_rotat
                    .getSelectionModel()
                    .getSelectedItem();

                rotStr =  rotStr.substring(0, rotStr.length()-3);
                int rot = Integer.parseInt(rotStr);
                
                mapTilesPane.getSelection().setRotate(rot);
                mapTilesPane.getSelection().getTile().setRotation(rot);
            }
        });

        HBox hbx_solid = new HBox(lbl_tile_solid, chb_tile_solid);
        HBox hbx_rotat = new HBox(lbl_tile_rotat, chb_tile_rotat);
        hbx_solid.setSpacing(5f);

        setFillWidth(true);
        setPadding(new Insets(10f));
        getChildren().addAll(lbl_tile_title, lbl_tile_coords, lbl_tile_size, hbx_solid, hbx_rotat);
    }
}