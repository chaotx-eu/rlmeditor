package fh.sem.gui.stage.dialog;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog extends Stage {
    public Dialog(Stage parent) {
        this(parent, "");
    }

    public Dialog(Stage parent, String title) {
        this(parent, title, parent.getTitle());
    }

    public Dialog(Stage parent, String title, String parent_title) {
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);
        setTitle(parent_title + " - " + title);
    }
}