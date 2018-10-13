package fh.sem.gui.pane;

import javafx.scene.layout.*;
import fh.sem.gui.view.TileView;
import javafx.scene.image.*;

public class TileStackPane extends StackPane {
    private TileView primary;
    private TileView secondary;

    public TileStackPane(TileView primary, TileView secondary) {
        setPrimary(primary);
        setSecondary(secondary);
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
            setupImageView(primary);
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
            setupImageView(secondary);
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

    private void setupImageView(ImageView tv) {
        if(tv != null) {
            tv.fitWidthProperty().bind(widthProperty());
            tv.fitHeightProperty().bind(heightProperty());
        }
    }
}