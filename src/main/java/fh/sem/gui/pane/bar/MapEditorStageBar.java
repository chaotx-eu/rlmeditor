package fh.sem.gui.pane.bar;

import fh.sem.gui.stage.MapEditorStage;
import fh.sem.gui.stage.dialog.*;
import fh.sem.logic.*;
import fh.sem.App;

import javafx.scene.control.*;

public class MapEditorStageBar extends StageBar {
    public MapEditorStageBar(MapEditorStage parent) {
        super(parent);

        Menu mnu_file = new Menu("File");
        MenuItem mni_new = new MenuItem("New");
        MenuItem mni_load = new MenuItem("Load");
        MenuItem mni_save1 = new MenuItem("Save");
        MenuItem mni_save2 = new MenuItem("SaveAs");

        mni_new.setOnAction(e -> new NewMapDialog(parent, App.APP_TITLE).show());
        mni_load.setOnAction(e -> new LoadDialog(parent, App.APP_TITLE).show());
        mni_save2.setOnAction(e -> new SaveDialog(parent, App.APP_TITLE).show());
        mni_save1.setOnAction(e -> App.projectManager.save(
            App.projectManager.getActiveProject()));

        mni_save1.setDisable(true);
        mni_save2.setDisable(true);
        App.projectManager.addObserver((pm, arg) -> {
            Project p = App.projectManager.getActiveProject();
            mni_save1.setDisable(p == null);
            mni_save2.setDisable(false);
            // TODO setTitle in StageBar
            // setTitle("RMLEditor - " + (p == null
            //     ? "New Project" : p.getTitle()));
        });

        mnu_file.getItems().addAll(mni_new, mni_load, mni_save1, mni_save2);

        super.addMenu(mnu_file);
    }
}