package fh.sem.gui.stage;


import java.io.File;

import fh.sem.App;
import fh.sem.gui.stage.dialog.ConfirmDialog;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.application.Platform;
import javafx.geometry.*;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class MapExportStage extends Stage {
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent content = new ClipboardContent();
    private int sem = 0;

    public MapExportStage(MapEditorStage parent) {
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);

        Tile tile;
        TileMap tileMap = parent.getTileMap();
        StringBuilder sb = new StringBuilder();

        for(int y = 0, x; y < tileMap.getHeight(); ++y) {
            for(x = 0; x < tileMap.getWidth(); ++x) {
                if((tile = tileMap.getTile(x, y)) != null)
                    sb.append(tile.getX() + "," + tile.getY() + ","
                        + tile.getWidth() + "," + tile.getHeigth() + ","
                        + (tile.isSolid() ? 1 : 0) + ".");
            }

            sb.append(";\n");
        }

        Label lbl_fed = new Label();
        Text txt_map = new Text(sb.toString());
        Button btn_save = new Button("Save to File");
        Button btn_copy = new Button("Copy to Clipboard");
        TilePane tlp_btns = new TilePane(btn_save, btn_copy);
        ScrollPane scr_map = new ScrollPane(txt_map);

        scr_map.setFitToWidth(true);
        scr_map.setFitToHeight(true);
        
        VBox.setVgrow(scr_map, Priority.ALWAYS);
        tlp_btns.setAlignment(Pos.CENTER_RIGHT);
        tlp_btns.setPrefColumns(2);
        tlp_btns.setHgap(5f);

        btn_save.setMaxWidth(Double.MAX_VALUE);
        btn_copy.setMaxWidth(Double.MAX_VALUE);
        lbl_fed.setPadding(new Insets(5f, 0, 0, 0));

        HBox hbx_spc = new HBox();
        HBox hbx_bot = new HBox(lbl_fed, hbx_spc, tlp_btns);
        HBox.setHgrow(hbx_spc, Priority.ALWAYS);

        VBox vbx_main = new VBox(scr_map, hbx_bot);
        vbx_main.setPadding(new Insets(10f));
        vbx_main.setFillWidth(true);
        vbx_main.setSpacing(5f);

        btn_save.setOnAction(e -> {
            if(FileExplorerStage.DEFAULT_TREE == null)
                showFeedback("Scanning Filesystem. Try again in a moment.",
                    lbl_fed, 2500);
            else {
                new FileExplorerStage(this, System.getProperty("user.home")
                    + File.separator + "documents", file -> {
                        String mapData = txt_map.getText().replace("\n", "");

                        if(file.exists())
                            new ConfirmDialog(this, "File '" + file.getAbsolutePath()
                                + "' already exists.\nOverwrite?", () -> {
                                    App.mapManager.exportMap(file, mapData);
                                    showFeedback("File overwritten: '" + file.getAbsolutePath() + "'", lbl_fed, 10000);
                                }
                            ).show();
                        else {
                            App.mapManager.exportMap(file, mapData);
                            showFeedback("File saved: '" + file.getAbsolutePath() + "'", lbl_fed, 10000);
                        }
                    }, f -> f.isDirectory() && ! f.isHidden()
                ).show();
            }
        });

        btn_copy.setOnAction(e -> {
            content.putString(txt_map.getText().replace("\n", ""));
            clipboard.setContent(content);
            showFeedback("Map copied to Clipboad", lbl_fed, 2500);
        });

        setMinWidth(512);
        setMinHeight(384);
        setWidth(parent.getWidth()*0.8);
        setWidth(parent.getHeight()*0.8);
        setScene(new Scene(vbx_main));
    }

    private void showFeedback(String message, Label label, long time) {
        label.setText(message);
        ++sem;

        Thread thread = new Thread(() -> { try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if(--sem == 0)
                Platform.runLater(() -> label.setText(""));
        }});

        thread.setDaemon(true);
        thread.start();
    }
}