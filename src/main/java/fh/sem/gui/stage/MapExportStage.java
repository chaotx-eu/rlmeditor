package fh.sem.gui.stage;


import java.io.File;

import fh.sem.App;
import fh.sem.gui.stage.dialog.ConfirmDialog;
import fh.sem.gui.stage.dialog.Dialog;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.application.Platform;
import javafx.geometry.*;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class MapExportStage extends Dialog {
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent content = new ClipboardContent();
    private int sem = 0;

    public MapExportStage(MapEditorStage parent) {
        super(parent, "Export Map");
        
        Label lbl_fed = new Label();
        Text txt_map = new Text(App.mapManager.parseMap(
            parent.getTileMap(), parent.getTileSet().getTitle()));

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
                        if(file.exists())
                            new ConfirmDialog(this, "File '" + file.getAbsolutePath()
                                + "' already exists.\nOverwrite?", () -> {
                                    App.mapManager.exportMap(file, txt_map.getText());
                                    // showFeedback("File overwritten: '" + file.getAbsolutePath() + "'", lbl_fed, 10000);
                                    close();
                                }
                            ).show();
                        else {
                            App.mapManager.exportMap(file, txt_map.getText());
                            // showFeedback("File saved: '" + file.getAbsolutePath() + "'", lbl_fed, 10000);
                            close();
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

        setScene(new Scene(vbx_main));
        vbx_main.setId("dialog-pane");
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