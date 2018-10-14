package fh.sem;

import fh.sem.gui.stage.FileExplorerStage;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.logic.TileMap;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * RougeLike Map Editor
 * by HDainester
 */
public class App extends Application {
    public static void main(String[]args) {
        launch(args);
    }

    @Override
    public void start(Stage ps) {
        FileExplorerStage.initDefault(f -> f.isDirectory() && !f.isHidden());
        new MapEditorStage(new TileMap(
            "file:resources/image/sheet/dungeon0_sheet.png",
            20, 10)).show();
    }
}
