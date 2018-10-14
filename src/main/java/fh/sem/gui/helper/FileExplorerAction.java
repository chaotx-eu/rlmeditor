package fh.sem.gui.helper;

import java.io.File;

import javafx.scene.control.TreeItem;

public interface FileExplorerAction {
    void execute(File selection);
}