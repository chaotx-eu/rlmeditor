package fh.sem.gui.stage;

import fh.sem.gui.pane.MapInfoPane;
import fh.sem.gui.pane.MapPane;
import fh.sem.gui.pane.MapTilesPane;
import fh.sem.gui.pane.TileConfigPane;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;
import fh.sem.logic.TileSet;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class MapEditorStage extends Stage {
    private TileMap tileMap;

    public MapEditorStage(TileMap tileMap) {
        // demo -> TODO read from xml
        TileSet tileSet = new TileSet();
        tileSet.addTile("title", "Floor", new Tile(0, 0, 16, 16));
        tileSet.addTile("title", "Floor", new Tile(16, 0, 16, 16));
        tileSet.addTile("title", "Floor", new Tile(32, 0, 16, 16));

        tileSet.addTile("title", "Wall", "Vertical", new Tile(0, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "Vertical", new Tile(16, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "Vertical", new Tile(32, 16, 16, 16, true));

        tileSet.addTile("title", "Wall", "Horizontal", new Tile(0, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "Horizontal", new Tile(16, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "Horizontal", new Tile(32, 32, 16, 16, true));

        tileSet.addTile("title", "Wall", "Cross", new Tile(0, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "Cross", new Tile(16, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "Cross", new Tile(32, 48, 16, 16, true));

        tileSet.addTile("title", "Wall", "CornerTL", new Tile(48, 0, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerTL", new Tile(64, 0, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerTL", new Tile(80, 0, 16, 16, true));

        tileSet.addTile("title", "Wall", "CornerBL", new Tile(48, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerBL", new Tile(64, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerBL", new Tile(80, 16, 16, 16, true));

        tileSet.addTile("title", "Wall", "CornerTR", new Tile(48, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerTR", new Tile(64, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerTR", new Tile(80, 32, 16, 16, true));

        tileSet.addTile("title", "Wall", "CornerBR", new Tile(48, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerBR", new Tile(64, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "CornerBR", new Tile(80, 48, 16, 16, true));

        tileSet.addTile("title", "Wall", "TTop", new Tile(96, 0, 16, 16, true));
        tileSet.addTile("title", "Wall", "TTop", new Tile(112, 0, 16, 16, true));
        tileSet.addTile("title", "Wall", "TTop", new Tile(128, 0, 16, 16, true));

        tileSet.addTile("title", "Wall", "TLeft", new Tile(96, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "TLeft", new Tile(112, 16, 16, 16, true));
        tileSet.addTile("title", "Wall", "TLeft", new Tile(128, 16, 16, 16, true));

        tileSet.addTile("title", "Wall", "TBottom", new Tile(96, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "TBottom", new Tile(112, 32, 16, 16, true));
        tileSet.addTile("title", "Wall", "TBottom", new Tile(128, 32, 16, 16, true));

        tileSet.addTile("title", "Wall", "TRight", new Tile(96, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "TRight", new Tile(112, 48, 16, 16, true));
        tileSet.addTile("title", "Wall", "TRight", new Tile(128, 48, 16, 16, true));
        /////

        VBox vbx_left = new VBox();
        VBox vbx_right = new VBox();
        HBox hbx_center = new HBox(vbx_left, vbx_right);

        MapTilesPane mtsPane = new MapTilesPane(new Image(tileMap.getSpriteSheet()), tileSet);
        TileConfigPane tcfPane = new TileConfigPane(mtsPane);
        MapPane mapPane = new MapPane(tileMap, mtsPane);
        MapInfoPane mfoPane = new MapInfoPane(mapPane);

        mapPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        mfoPane.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        mtsPane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        tcfPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));

        mtsPane.prefWidthProperty().bind(widthProperty().multiply(1/7f));
        mtsPane.prefHeightProperty().bind(heightProperty().multiply(7/8f));
        tcfPane.prefWidthProperty().bind(mtsPane.widthProperty());
        tcfPane.prefHeightProperty().bind(heightProperty().multiply(1/8f));

        mapPane.prefWidthProperty().bind(widthProperty().multiply(6/7f));
        mapPane.prefHeightProperty().bind(heightProperty().multiply(11/12f));
        mfoPane.prefWidthProperty().bind(mapPane.widthProperty());
        mfoPane.prefHeightProperty().bind(heightProperty().multiply(1/12f));

        vbx_left.getChildren().addAll(mtsPane, tcfPane);
        vbx_right.getChildren().addAll(mapPane, mfoPane);

        setWidth(960);
        setHeight(720);
        setScene(new Scene(hbx_center));
        this.tileMap = tileMap;
    }

    public TileMap getTileMap() {
        return tileMap;
    }
}