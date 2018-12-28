package fh.sem.gui.pane;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.gui.stage.MapExportStage;
import javafx.beans.binding.Bindings;
import javafx.geometry.*;

public class MapInfoPane extends HBox {
    public MapInfoPane(MapPane mapPane) {
        Label lbl_coordx = new Label("X:");
        Label lbl_coordy = new Label("Y:");
        Label lbl_coordz = new Label("Z:");
        Label lbl_rot = new Label("Rotation:");
        Label lbl_solid = new Label("Solid:");
        Label lbl_mode = new Label("Draw Mode:");

        Label lbl_title_v = new Label();
        Label lbl_coordx_v = new Label();
        Label lbl_coordy_v = new Label();
        Label lbl_coordz_v = new Label();
        Label lbl_rot_v = new Label();
        Label lbl_solid_v = new Label();
        Label lbl_mode_v = new Label();

        HBox hbx_lbls = new HBox(
            new HBox(lbl_title_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_coordx, lbl_coordx_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_coordy, lbl_coordy_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_coordz, lbl_coordz_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_rot, lbl_rot_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_solid, lbl_solid_v) {{ setAlignment(Pos.CENTER); }},
            new HBox(lbl_mode, lbl_mode_v) {{ setAlignment(Pos.CENTER); }}
        );

        HBox.setHgrow(hbx_lbls, Priority.ALWAYS);
        hbx_lbls.setAlignment(Pos.CENTER_LEFT);
        hbx_lbls.setSpacing(20f);

        lbl_coordx.setStyle("-fx-font-weight:bold");
        lbl_coordy.setStyle("-fx-font-weight:bold");
        lbl_coordz.setStyle("-fx-font-weight:bold");
        lbl_rot.setStyle("-fx-font-weight:bold");
        lbl_solid.setStyle("-fx-font-weight:bold");
        lbl_mode.setStyle("-fx-font-weight:bold");
        lbl_title_v.setStyle("-fx-font-weight:bold");
        
        lbl_title_v.textProperty().bind(Bindings.format("%-13s", mapPane.tileTitleProperty()));
        lbl_solid_v.textProperty().bind(Bindings.format("%-5s", mapPane.tileSolidProperty()));
        lbl_mode_v.textProperty().bind(Bindings.format("%-12s", mapPane.selectionModeNameProperty()));
        lbl_coordx_v.textProperty().bind(mapPane.tileXProperty().asString("%-3d"));
        lbl_coordy_v.textProperty().bind(mapPane.tileYProperty().asString("%-3d"));
        lbl_coordz_v.textProperty().bind(mapPane.tileZProperty().asString("%-3d"));
        lbl_rot_v.textProperty().bind(mapPane.tileRotationProperty().asString("%3d\u00b0"));
        
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

        setSpacing(5f);
        setPadding(new Insets(0, 10f, 0, 10f));
        getChildren().setAll(hbx_lbls,
            new Separator(Orientation.VERTICAL), tlp_btns);
    }
}