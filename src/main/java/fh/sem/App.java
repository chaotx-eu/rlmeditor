package fh.sem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import fh.sem.gui.stage.FileExplorerStage;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.util.MapManager;
import fh.sem.util.ProjectManager;
import fh.sem.util.ResourceManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * RougeLike Map Editor
 * by HDainester
 */
public class App extends Application {
    public static PrintStream log;
    public static final String APP_TITLE = "RLMEditor"; // TODO new title "TAOS" (The art of square)
    public static final MapManager mapManager = MapManager.instance();
    public static final ProjectManager projectManager = ProjectManager.instance();
    public static final ResourceManager resourceManager = ResourceManager.instance();

    public static final String APP_DIR = (System.getProperty("user.home") + File.separator + ".RLMEditor");
    public static final String IMG_DIR = (APP_DIR + File.separator + "images" + File.separator + "maps");
    public static final String TLS_DIR = (APP_DIR + File.separator + "tilesets");
    public static final String PROJ_DIR = (APP_DIR + File.separator + "projects");
    public static final String LOGFILE = (APP_DIR + File.separator + "app.log");

    public static final String[] DEMO_TILESETS = {
        "/tilesets/demo0_tileset.xml",
        "/tilesets/demo1_tileset.xml"
    };
    
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
        try {
            log = new PrintStream(new File(LOGFILE));
        } catch(FileNotFoundException e) {
            log = System.out;
            e.printStackTrace();
        }

        FileExplorerStage.initDefault(f -> f.isDirectory() && !f.isHidden());
        new MapEditorStage().show();
    }

    @Override
    public void stop() {
        log.flush();
        log.close();
    }
}
