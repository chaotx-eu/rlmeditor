package fh.sem;

import java.io.File;

import fh.sem.gui.stage.FileExplorerStage;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.util.MapManager;
import fh.sem.util.ProjectManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * RougeLike Map Editor
 * by HDainester
 */
public class App extends Application {
    public static final String APP_TITLE = "RLMEditor";
    public static final MapManager mapManager = MapManager.instance();
    public static final ProjectManager projectManager = ProjectManager.instance();

    public static final String APP_DIR = (System.getProperty("user.home") + File.separator + ".RLMEditor");
    public static final String IMG_DIR = (APP_DIR + File.separator + "images");
    public static final String TLS_DIR = (APP_DIR + File.separator + "tilesets");
    public static final String PROJ_DIR = (APP_DIR + File.separator + "projects");
    
    public static void main(String[]args) {
        new File(APP_DIR).mkdirs();
        new File(IMG_DIR).mkdirs();
        new File(TLS_DIR).mkdirs();
        new File(PROJ_DIR).mkdirs();
        projectManager.load();
        launch(args);
    }

    @Override
    public void start(Stage ps) {
        FileExplorerStage.initDefault(f -> f.isDirectory() && !f.isHidden());
        // new MapEditorStage().show();

        MapEditorStage mes = new MapEditorStage();
        mes.getScene().getStylesheets().add("style.css");
        mes.show();
    }
}
