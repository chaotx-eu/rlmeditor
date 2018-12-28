package fh.sem.gui.stage.dialog;

import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.stage.Stage;


public class Dialog extends Stage {
    public Dialog(Window parent) {
        this(parent, "");
    }

    public Dialog(Window parent, String title) {
        this(parent, title, parent.getClass() == Stage.class
            ? ((Stage)parent).getTitle() : "");
    }

    public Dialog(Window parent, String title, String parent_title) {
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);

        setResizable(false);
        // setMaxWidth(parent.getWidth()*0.8f);
        // setMaxHeight(parent.getHeight()*0.8f);
        setTitle(parent_title + (!title.isEmpty() ? " - " + title : ""));
        sceneProperty().addListener((p, o, n) -> {
            if(o == null && n != null)
                n.getStylesheets().setAll(parent
                    .getScene().getStylesheets());
        });
    }
}