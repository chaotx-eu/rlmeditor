package fh.sem.gui.pane.bar;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.property.*;

public class StageBar extends HBox {
    private MenuBar menuBar = new MenuBar();
    private BooleanProperty maximized = new SimpleBooleanProperty();

    public StageBar(Stage parent, Menu... menus) {
        Button btn_minimize = new Button();
        Button btn_maximize = new Button();
        Button btn_exit = new Button();
        Label lbl_title = new Label("Some Title - Some Project");

        btn_minimize.setFocusTraversable(false);
        btn_maximize.setFocusTraversable(false);
        btn_exit.setFocusTraversable(false);

        btn_minimize.setId("button-minimize");
        btn_maximize.setId("button-maximize");
        btn_exit.setId("button-exit");

        btn_minimize.setOnAction(e -> parent.setIconified(true));
        btn_maximize.setOnAction(e -> {
            parent.setMaximized(maximized.get());
            maximized.set(!maximized.get());
        });
        btn_exit.setOnAction(e -> parent.close());

        // TODO drage event and other fixes

        HBox hbx_mnus = new HBox(menuBar);
        HBox hbx_btns = new HBox(
            btn_minimize,
            btn_maximize,
            btn_exit
        );

        hbx_mnus.setAlignment(Pos.CENTER_LEFT);
        hbx_btns.setAlignment(Pos.CENTER_RIGHT);

        for(Menu menu : menus) addMenu(menu);
        HBox.setHgrow(hbx_mnus, Priority.ALWAYS);
        HBox.setHgrow(hbx_btns, Priority.ALWAYS);
        parent.initStyle(StageStyle.UNDECORATED);
        getChildren().setAll(hbx_mnus, lbl_title, hbx_btns);
        setId("stage-bar");
    }

    public void addMenu(Menu menu) {
        menuBar.getMenus().add(menu);
    }

    public void removeMenu(Menu menu) {
        menuBar.getMenus().remove(menu);
    }
}