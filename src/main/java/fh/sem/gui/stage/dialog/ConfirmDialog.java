package fh.sem.gui.stage.dialog;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;

public class ConfirmDialog extends Dialog {
    public static final int DIALOG_WIDTH = 384;
    public static final int DIALOG_HEIGHT = 128;

    public ConfirmDialog(Window parent, String message, Runnable action) {
        this(parent, parent.getClass() == Stage.class
            ? ((Stage)parent).getTitle() : "", message, action);
    }

    public ConfirmDialog(Window parent, String title, String message, Runnable action) {
        super(parent, title, "Confirm");
        Text txt_msg = new Text(message);
        Button btn_cnf = new Button("Confirm");
        Button btn_cnc = new Button("Cancel");
        TilePane tlp_btns = new TilePane(btn_cnc, btn_cnf);
        
        btn_cnf.setMaxWidth(Double.MAX_VALUE);
        btn_cnc.setMaxWidth(Double.MAX_VALUE);
        tlp_btns.setHgap(5f);
        tlp_btns.setAlignment(Pos.CENTER_RIGHT);
        txt_msg.setTextAlignment(TextAlignment.CENTER);
        txt_msg.setId("important-text");

        VBox vbx_main = new VBox(txt_msg, tlp_btns);
        vbx_main.setFillWidth(true);
        vbx_main.setAlignment(Pos.CENTER);
        vbx_main.setPadding(new Insets(10f));
        vbx_main.setSpacing(20f);

        btn_cnc.setOnAction(e -> close());
        btn_cnf.setOnAction(e -> {
            action.run();
            close();
        });
        
        setScene(new Scene(vbx_main));
        vbx_main.setId("dialog-pane");
    }
}