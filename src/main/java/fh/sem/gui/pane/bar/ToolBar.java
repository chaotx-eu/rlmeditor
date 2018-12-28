package fh.sem.gui.pane.bar;

import java.util.ArrayList;
import java.util.List;

import fh.sem.gui.pane.MapPane;
import fh.sem.gui.pane.TileStackPane;
import fh.sem.gui.stage.dialog.ConfirmDialog;
import fh.sem.logic.Tile;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.beans.property.*;
import javafx.geometry.*;


public class ToolBar extends HBox {
    private MapPane parent;
    private IntegerProperty layer;
    private List<BooleanProperty> layerStates;

    public ToolBar(MapPane parent) {
        this.parent = parent;
        layer = parent.layerProperty();
        layerStates = new ArrayList<>();

        Text txt_layer = new Text();
        Button btn_layer_plus = new Button("+");
        Button btn_layer_min = new Button("-");
        Button btn_add_layer = new Button("Add");
        Button btn_del_layer = new Button("Del");
        CheckBox chb_layer_show = new CheckBox("Show");

        chb_layer_show.setSelected(true);
        txt_layer.textProperty().bind(layer.asString("Layer: %2d"));

        btn_layer_plus.setOnAction(e -> {
            layerStates.get(layer.get()).unbind();
            layer.set(layer.get()+1);
            chb_layer_show.setSelected(layerStates.get(layer.get()).get());
            layerStates.get(layer.get()).bind(chb_layer_show.selectedProperty());
        });

        btn_layer_min.setOnAction(e -> {
            layerStates.get(layer.get()).unbind();
            layer.set(Math.max(0, layer.get()-1));
            chb_layer_show.setSelected(layerStates.get(layer.get()).get());
            layerStates.get(layer.get()).bind(chb_layer_show.selectedProperty());
        });

        btn_add_layer.setOnAction(e -> {
            parent.setMaxLayer(Math.min(MapPane.MAX_LAYER, parent.getMaxLayer()+1));
            addLayer();
            btn_layer_plus.fire();
        });

        btn_del_layer.setOnAction(e -> { new ConfirmDialog(
            getScene().getWindow(),
            "Delete Layer " + layer.get(),
            "This action will remove all tiles on this layer!\nAre you sure?", () -> {
                for(int z = parent.getLayer(); z <= parent.getMaxLayer(); ++z)
                for(Tile tile : parent.getTileMap().getTiles(z)) {
                    parent.removeTile(tile);

                    if(z > parent.getLayer()) {
                        tile.setLayer(tile.getLayer()-1);
                        parent.getTileMap().addTile(tile);
                    }
                }
                
                parent.setMaxLayer(Math.max(0, parent.getMaxLayer()-1));
                removeLayer();

                chb_layer_show.setSelected(layerStates.get(layer.get()).get());
                layerStates.get(layer.get()).bind(chb_layer_show.selectedProperty());
            }).show();
        });

        btn_layer_plus.disableProperty().bind(parent.layerProperty().isEqualTo(parent.maxLayerProperty()));
        btn_layer_min.disableProperty().bind(parent.layerProperty().isEqualTo(0));
        btn_add_layer.disableProperty().bind(btn_layer_plus.disableProperty().not());
        btn_del_layer.disableProperty().bind(parent.maxLayerProperty().greaterThan(0).not());

        for(int i = 0; i <= parent.getMaxLayer(); ++i) {
            addLayer();
            layerStates.get(layer.get()).bind(chb_layer_show.selectedProperty());
        }

        setSpacing(5);
        setId("toolbar");
        setPadding(new Insets(3f, 10f, 3f, 10f));
        setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(chb_layer_show, btn_layer_min, txt_layer, btn_layer_plus,
            new Separator() {{ setOrientation(Orientation.VERTICAL); }},
            btn_add_layer, btn_del_layer);
    }

    private void addLayer() {
        BooleanProperty layerState = new SimpleBooleanProperty(true);
        layerState.addListener((p, o, n) -> {
            for(TileStackPane[] tsps : parent.getTileStackPanes())
            for(TileStackPane tsp : tsps) tsp.setVisible(n, layer.get());
        });

        layerStates.add(layerState);
    }

    private void removeLayer() {
        layerStates.get(layer.get()).unbind();
        layerStates.remove(layer.get());

        if(parent.getLayer() > parent.getMaxLayer())
            parent.setLayer(parent.getMaxLayer());
    }
}