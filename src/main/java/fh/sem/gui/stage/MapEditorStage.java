package fh.sem.gui.stage;

import java.io.File;

import fh.sem.App;
import fh.sem.gui.pane.MapInfoPane;
import fh.sem.gui.pane.MapPane;
import fh.sem.gui.pane.MapTilesPane;
import fh.sem.gui.pane.TileConfigPane;
import fh.sem.gui.pane.bar.MapEditorStageBar;
import fh.sem.gui.stage.dialog.LoadDialog;
import fh.sem.gui.stage.dialog.NewMapDialog;
import fh.sem.gui.stage.dialog.SaveDialog;
import fh.sem.logic.Project;
import fh.sem.logic.TileMap;
import fh.sem.logic.TileSet;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class MapEditorStage extends Stage {
    private TileMap tileMap;
    private TileSet tileSet;
    private Image sheetIMG;
    private VBox vbx_left = new VBox();
    private VBox vbx_right = new VBox();

    public MapEditorStage() {
        Menu mnu_file = new Menu("File");
            MenuItem mni_new = new MenuItem("New");
            MenuItem mni_load = new MenuItem("Load");
            MenuItem mni_save1 = new MenuItem("Save");
            MenuItem mni_save2 = new MenuItem("SaveAs");

        mnu_file.getItems().addAll(mni_new, mni_load, mni_save1, mni_save2);
        MenuBar mnb_main = new MenuBar(mnu_file);
        
        mni_new.setOnAction(e -> new NewMapDialog(this, App.APP_TITLE).show());
        mni_load.setOnAction(e -> new LoadDialog(this, App.APP_TITLE).show());
        mni_save2.setOnAction(e -> new SaveDialog(this, App.APP_TITLE).show());
        mni_save1.setOnAction(e -> App.projectManager
            .save(App.projectManager.getActiveProject()));

        mni_save1.setDisable(true);
        mni_save2.setDisable(true);
        App.projectManager.addObserver((pm, arg) -> {
            Project p = App.projectManager.getActiveProject();
            mni_save1.setDisable(p == null);
            mni_save2.setDisable(false);
            setTitle("RMLEditor - " + (p == null
                ? "New Project" : p.getTitle()));
        });

        HBox hbx_center = new HBox(vbx_left, vbx_right);
        BorderPane bdp = new BorderPane(hbx_center);
        bdp.setTop(mnb_main);

        // unfinished
        // bdp.setTop(new MapEditorStageBar(this));

        setMinWidth(960);
        setMinHeight(720);
        setTitle("RLMEditor - Welcome");
        setScene(new Scene(bdp));
        getScene().getStylesheets().add("style/style.css");
    }

    public void init(TileMap tileMap, TileSet tileSet) {
        try {
            sheetIMG = (new File(tileSet.getSheet())).exists()
                ? new Image("file:" + tileSet.getSheet())
                : new Image(getClass().getResourceAsStream(tileSet.getSheet()));
        } catch(NullPointerException | IllegalArgumentException e) {
            System.out.println("Image File not found");
            // e.printStackTrace();
            // sheetIMG = DEFAULT_IMAGE;
            // TODO showFeedback()
        }

        MapTilesPane mtsPane = new MapTilesPane(sheetIMG, tileSet);
        TileConfigPane tcfPane = new TileConfigPane(mtsPane);
        MapPane mapPane = new MapPane(tileMap, mtsPane);
        MapInfoPane mfoPane = new MapInfoPane(mapPane);

        mtsPane.prefWidthProperty().bind(widthProperty().multiply(1/7f));
        mtsPane.prefHeightProperty().bind(heightProperty().multiply(7/8f));
        tcfPane.prefWidthProperty().bind(mtsPane.widthProperty());
        tcfPane.prefHeightProperty().bind(heightProperty().multiply(1/8f));

        mapPane.prefWidthProperty().bind(widthProperty().multiply(6/7f));
        mapPane.prefHeightProperty().bind(heightProperty().multiply(11/12f));
        mfoPane.prefWidthProperty().bind(mapPane.widthProperty());
        mfoPane.prefHeightProperty().bind(heightProperty().multiply(1/12f));

        vbx_left.getChildren().setAll(mtsPane, tcfPane);
        vbx_right.getChildren().setAll(mapPane, mfoPane);

        this.tileMap = tileMap;
        this.tileSet = tileSet;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public Image getSheetIMG() {
        return sheetIMG;
    }
}