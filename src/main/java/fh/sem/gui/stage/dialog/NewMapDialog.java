package fh.sem.gui.stage.dialog;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import fh.sem.App;
import fh.sem.gui.stage.MapEditorStage;
import fh.sem.logic.TileMap;
import fh.sem.logic.TileSet;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class NewMapDialog extends Dialog {
    public NewMapDialog(MapEditorStage parent) {
        this(parent, parent.getTitle());
    }

    public NewMapDialog(MapEditorStage parent, String parent_title) {
        super(parent, "Choose a TileSet", parent_title);
        ListView<TileSet> lsv_tiles = new ListView<>();
        BorderPane bdp_main = new BorderPane(lsv_tiles);
        Button btn_cnf = new Button("Confirm");
        TextField txf_wdth = new TextField();
        TextField txf_hght = new TextField();

        btn_cnf.disableProperty().bind(lsv_tiles
            .getSelectionModel().selectedItemProperty().isNull()
            .or(txf_wdth.textProperty().isEmpty())
            .or(txf_hght.textProperty().isEmpty()));

        txf_wdth.textProperty().addListener((p, o, n) -> {
            if(!n.matches("\\d*"))
                txf_wdth.setText(n.replaceAll("[^\\d]", ""));
        });

        txf_hght.textProperty().addListener((p, o, n) -> {
            if(!n.matches("\\d*"))
                txf_hght.setText(n.replaceAll("[^\\d]", ""));
        });

        txf_wdth.setPromptText("Width");
        txf_hght.setPromptText("Height");
        txf_wdth.setPrefColumnCount(4);
        txf_hght.setPrefColumnCount(4);

        setOnShowing(e -> {
            lsv_tiles.getItems().clear();
            try {
                App.mapManager.loadTileSets()
                    .forEach(lsv_tiles.getItems()::add);
            } catch(IOException | ParserConfigurationException | SAXException ex) {
                ex.printStackTrace();
                // TODO set feedback
            }
        });

        btn_cnf.setOnAction(e -> {
            TileSet tileSet = lsv_tiles.getSelectionModel().getSelectedItem();
            TileMap tileMap = new TileMap(tileSet.getSheet(),
                Integer.parseInt(txf_wdth.getText()),
                Integer.parseInt(txf_hght.getText()));
            parent.init(tileMap, tileSet);
            
            App.projectManager.setActiveProject(null);
            close();
        });

        lsv_tiles.setOnMouseClicked(e -> {
            if(e.getClickCount() > 1)
                btn_cnf.fire();
        });
        
        HBox hbx_spc = new HBox();
        HBox hbx_txf = new HBox(txf_wdth, txf_hght);
        HBox hbx_bot = new HBox(hbx_txf, hbx_spc, btn_cnf);

        HBox.setHgrow(hbx_spc, Priority.ALWAYS);
        hbx_bot.setPadding(new Insets(10f, 0, 0, 0));
        hbx_txf.setSpacing(5f);
        bdp_main.setPadding(new Insets(10f));
        bdp_main.setBottom(hbx_bot);

        setScene(new Scene(bdp_main));
        bdp_main.setId("dialog-pane");
    }

    public void setFeedback(String message, long time) {
        // TODO
    }
}