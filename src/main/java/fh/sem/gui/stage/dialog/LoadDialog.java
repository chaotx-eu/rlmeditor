package fh.sem.gui.stage.dialog;

import fh.sem.App;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.logic.Project;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class LoadDialog extends Dialog {
    public LoadDialog(MapEditorStage parent) {
        this(parent, parent.getTitle());
    }

    public LoadDialog(MapEditorStage parent, String parent_title) {
        super(parent, "Load Project", parent_title);
        ListView<Project> lsv_proj = new ListView<>();
        BorderPane bdp_main = new BorderPane(lsv_proj);
        Button btn_cnf = new Button("Confirm");
        Button btn_del = new Button("Delete");

        App.projectManager.load();
        App.projectManager.getProjects().stream()
            .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()))
            .forEach(p -> lsv_proj.getItems().add(p));

        btn_cnf.disableProperty().bind(lsv_proj
            .getSelectionModel().selectedItemProperty().isNull());
        btn_del.disableProperty().bind(btn_cnf.disabledProperty());

        btn_cnf.setOnAction(e -> {
            Project sel = lsv_proj.getSelectionModel().getSelectedItem();
            App.projectManager.setActiveProject(sel);
            parent.init(sel.getTileMap(), sel.getTileSet());
            close();
        });

        btn_del.setOnAction(e -> {
            Project sel = lsv_proj.getSelectionModel().getSelectedItem();
            new ConfirmDialog(this, "Delete Project: '" + sel.getTitle()
                + "' ?\nThis can't be undone!", () -> {

                lsv_proj.getItems().remove(sel);
                App.projectManager.delete(sel);
            }).show();
        });

        lsv_proj.setOnMouseClicked(e -> {
            if(e.getClickCount() > 1)
                btn_cnf.fire();
        });

        HBox hbx_spc = new HBox();
        HBox hbx_bot = new HBox(hbx_spc, btn_del, btn_cnf);

        HBox.setHgrow(hbx_spc, Priority.ALWAYS);
        hbx_bot.setPadding(new Insets(10f, 0, 0, 0));
        hbx_bot.setSpacing(5f);
        bdp_main.setPadding(new Insets(10f));
        bdp_main.setBottom(hbx_bot);
        
        setScene(new Scene(bdp_main));
        bdp_main.setId("dialog-pane");
    }
}