package fh.sem.gui.stage.dialog;

import fh.sem.App;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.logic.Project;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class SaveDialog extends Dialog {
    public SaveDialog(MapEditorStage parent) {
        this(parent, parent.getTitle());
    }

    public SaveDialog(MapEditorStage parent, String parent_title) {
        super(parent, "Save Project As", parent_title);
        TextField txf_name = new TextField();
        Button btn_cnf = new Button("Confirm");

        txf_name.setPromptText("Enter Project Name");
        HBox.setHgrow(txf_name, Priority.ALWAYS);

        HBox hbx_main = new HBox(txf_name, btn_cnf);
        hbx_main.setPadding(new Insets(10f));
        hbx_main.setSpacing(5f);

        btn_cnf.setOnAction(e -> {
            if(App.projectManager.getProjects().stream()
                .filter(p -> p.getTitle().equals(txf_name.getText()))
                .findAny().isPresent())
            {
                new ConfirmDialog(this, "Project with this name already exists.\nOverwrite?", () ->
                    saveAndClose(txf_name.getText(), parent)).show();
            } else saveAndClose(txf_name.getText(), parent);
        });

        setScene(new Scene(hbx_main));
        hbx_main.setId("dialog-pane");
    }

    private void saveAndClose(String name, MapEditorStage parent) {
        // try {
            Project project = new Project(name,
                parent.getTileSet(),
                parent.getTileMap()
            );

            // File file = new File(App.PROJ_DIR
            //     + File.separator + name);

            // MapIO.saveProject(file, project);
            App.projectManager.save(project);
            App.projectManager.setActiveProject(project);
            close();
        // } catch(IOException ex) {
        //     ex.printStackTrace();
        //     // TODO showFeedback()
        // }
    }
}