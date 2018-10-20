package fh.sem.gui.pane;

import fh.sem.gui.view.TileView;
import javafx.scene.layout.*;

public class TileStackPane extends StackPane {
    private TileView primary;
    private TileView secondary;

    public TileStackPane(TileView primary, TileView secondary) {
        setPrimary(primary);
        setSecondary(secondary);
        setMinSize(0, 0);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public TileStackPane(TileView primary) {
        this(primary, null);
    }

    public TileStackPane() {
        this(null, null);
    }

    public void setPrimary(TileView primary) {                
        if(primary == this.primary) return;
        if(primary == this.secondary)
            removeSecondary();
        getChildren().remove(this.primary);
        this.primary = primary;
        
        if(primary != null) {
            setupTileView(primary);
            primary.setOpacity(1f);
            getChildren().add(0, primary);
        }
    }

    public void setSecondary(TileView secondary) {
        if(secondary == this.secondary) return;
        if(secondary == this.primary) removePrimary();
        getChildren().remove(this.secondary);
        this.secondary = secondary;

        if(secondary != null) {
            setupTileView(secondary);
            secondary.setOpacity(0.8f);
            getChildren().add(secondary);
        }
    }

    public TileView getPrimary() {
        return primary;
    }

    public TileView getSecondary() {
        return secondary;
    }

    public void removePrimary() {
        setPrimary(null);
    }

    public void removeSecondary() {
        setSecondary(null);
    }

    private void setupTileView(TileView tv) {
        if(tv != null) {
            // TODO fix bug -> gaps in GridPane (width/heightProperty doesnt work)
            tv.fitWidthProperty().bind(widthProperty());
            tv.fitHeightProperty().bind(heightProperty());
            tv.setPreserveRatio(false);
        }
    }
}