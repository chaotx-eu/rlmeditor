package fh.sem.gui.pane;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.gui.stage.MapExportStage;
import javafx.beans.binding.Bindings;
import javafx.geometry.*;

public class MapInfoPane extends HBox {
    public MapInfoPane(MapPane mapPane) {
        Label lbl_tile_title = new Label();
        Label lbl_tile_coords = new Label();
        Label lbl_tile_solid = new Label("Solid:");
        HBox hbx_lbls = new HBox(lbl_tile_title, lbl_tile_coords, lbl_tile_solid);

        hbx_lbls.setAlignment(Pos.CENTER_LEFT);
        hbx_lbls.setSpacing(5f);

        lbl_tile_title.setStyle("-fx-font-family:monospace; -fx-font-weight:bold;");
        lbl_tile_coords.setStyle("-fx-font-family:monospace;");
        lbl_tile_solid.setStyle("-fx-font-family:monospace;");

        lbl_tile_title.textProperty().bind(Bindings.format("%-12s", mapPane.tileTitleProperty()));
        lbl_tile_solid.textProperty().bind(Bindings.concat("Solid: ",
            mapPane.tileSolidProperty()));            
        lbl_tile_coords.textProperty().bind(mapPane
            .tileXProperty().asString("X: %-4d").concat(mapPane
            .tileYProperty().asString(" Y: %-4d")));

        Button btn_import = new Button("Import");
        Button btn_export = new Button("Export");
        TilePane tlp_btns = new TilePane(btn_import, btn_export);

        btn_import.setMaxWidth(Double.MAX_VALUE);
        btn_export.setMaxWidth(Double.MAX_VALUE);

        tlp_btns.setAlignment(Pos.CENTER_RIGHT);
        tlp_btns.setPrefColumns(2);
        tlp_btns.setHgap(5f);

        btn_import.setDisable(true);
        btn_import.setOnAction(e -> {
            // TODO
        });

        btn_export.setOnAction(e -> new MapExportStage(
            (MapEditorStage)getScene()
            .getWindow()).show());

        VBox vbx_space = new VBox();
        HBox.setHgrow(vbx_space, Priority.ALWAYS);

        setSpacing(5f);
        setPadding(new Insets(0, 10f, 0, 10f));
        getChildren().setAll(hbx_lbls, vbx_space,
            new Separator(Orientation.VERTICAL), tlp_btns);
    }
}