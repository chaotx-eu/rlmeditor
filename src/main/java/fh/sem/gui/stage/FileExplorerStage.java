package fh.sem.gui.stage;


import fh.sem.gui.helper.FileExplorerAction;
import fh.sem.gui.stage.dialog.Dialog;

import java.io.File;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class FileExplorerStage extends Dialog {
    public FileExplorerStage(Stage parent, String rootPath, FileExplorerAction action, Predicate<File> filter) {
        super(parent, "FileExplorer");
        TreeView<File> trv_files;

        if(rootPath.equals(DEFAULT_ROOT)) {
            while(DEFAULT_TREE == null);
            trv_files = DEFAULT_TREE;
        } else {
            trv_files = new TreeView<>();
            Thread thread = new Thread(() -> 
                trv_files.setRoot(buildTree(new File(rootPath), filter)));
            thread.setDaemon(true);
            thread.start();
        }
        
        trv_files.getRoot().setExpanded(true);
        trv_files.getSelectionModel().select(0);

        TextField txf_pth = new TextField();
        Button btn_cnf = new Button("Save");
        txf_pth.setPromptText("Enter Filename");

        HBox hbx_bot = new HBox(txf_pth, btn_cnf);
        HBox.setHgrow(txf_pth, Priority.ALWAYS);
        hbx_bot.setSpacing(10f);

        VBox vbx_main = new VBox(trv_files, hbx_bot);
        vbx_main.setPadding(new Insets(10f));
        vbx_main.setFillWidth(true);
        vbx_main.setSpacing(5f);

        btn_cnf.disableProperty().bind(trv_files
            .getSelectionModel()
            .selectedItemProperty().isNull()
            .or(txf_pth.textProperty().isEmpty()));

        btn_cnf.setOnAction(e -> {
            action.execute(new File(trv_files.getSelectionModel()
                .getSelectedItem().getValue()
                .getAbsolutePath() + File.separator + txf_pth.getText()));
            close();
        });

        setMinWidth(512);
        setMinHeight(384);
        setWidth(parent.getWidth()*0.8);
        setHeight(parent.getHeight()*0.8);
        setTitle(parent.getTitle() + " - " + "FileExplorer");

        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        setScene(new Scene(vbx_main));
        vbx_main.setId("dialog-pane");
    }

    public FileExplorerStage(Stage parent, String rootPath, FileExplorerAction action) {
        this(parent, rootPath, action, f -> true);
    }

    public FileExplorerStage(Stage parent, String rootPath) {
        this(parent, rootPath, f -> {});
    }


    protected static TreeView<File> DEFAULT_TREE;
    protected static String DEFAULT_ROOT =
        (System.getProperty("user.home") + File.separator + "documents");

    /**
     * Initializes DEFAULT_TREE with a filter.
     * If an instance is initialized with the same root path
     * as DEFAULT_ROOT the filter passed to the instance ignored.
     */
    public static void initDefault(Predicate<File> filter) {
        Thread thread = new Thread(() ->
            DEFAULT_TREE = new TreeView<File>(
                buildTree(new File(DEFAULT_ROOT), filter)));
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Initializes DEFAULT_TREE without a filter.
     * If an instance is initialized with the same root path
     * as DEFAULT_ROOT the filter passed to the instance ignored.
     */
    public static void initDefault() {
        initDefault(f -> true);
    }

    private static TreeItem<File> buildTree(File file, Predicate<File> filter) {
        file = new File(file.getAbsolutePath()) {
            @Override
            public String toString() {
                return getName();
            }
        };

        TreeItem<File> ti = new TreeItem<>(file);
        if(file.isDirectory() && file.canRead() && file.listFiles() != null) {
            Stream.of(file.listFiles())
                .filter(filter).sorted((f1, f2) -> {
                    if(f1.isDirectory() && !f2.isDirectory())
                        return -1;                    
                    if(!f1.isDirectory() && f2.isDirectory())
                        return 1;
                    return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
                }).forEach(f -> ti.getChildren().add(buildTree(f, filter)));
        }

        return ti;
    }
}