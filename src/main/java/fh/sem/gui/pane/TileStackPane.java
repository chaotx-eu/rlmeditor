package fh.sem.gui.pane;

import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import javafx.scene.layout.*;
import javafx.beans.property.*;


public class TileStackPane extends StackPane {
    private TileView primary;
    private TileView secondary;
    private IntegerProperty layer;

    public TileStackPane(TileView primary, TileView secondary) {
        setPrimary(primary);
        setSecondary(secondary);
        setMinSize(0, 0);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        layer = new SimpleIntegerProperty();
    }

    public TileStackPane(TileView primary) {
        this(primary, null);
    }

    public TileStackPane() {
        this(null, null);
    }

    public void setPrimary(TileView primary) {
        setPrimary(primary, -1);
    }

    public void setPrimary(TileView primary, int layer) {
        if(primary == this.primary) return;
        if(primary == this.secondary) removeSecondary();

        if(layer < 0) {
            layer = this.layer.get();
            primary.getTile().setLayer(layer);
        }

        this.primary = primary;
        if(primary != null) {
            setupTileView(primary);
            primary.setOpacity(1f);

            if(getChildren().size() == 0)
                getChildren().add(primary);                
            else {
                int i = 0;
                for( ; i < getChildren().size()-1 && i < layer; ++i);

                Tile tile = ((TileView)getChildren().get(i)).getTile();
                if(tile.getLayer() > layer) getChildren().add(i, primary);
                else if(tile.getLayer() < layer) getChildren().add(i+1, primary);
                else getChildren().set(i, primary);
            }
        } else if(layer >= 0 && layer < getChildren().size())
            getChildren().remove(layer);
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

    public void removePrimary(int layer) {
        setPrimary(null, layer);
    }

    public void removeSecondary() {
        setSecondary(null);
    }

    public IntegerProperty layerProperty() {
        return layer;
    }

    public void setVisible(boolean visible, int layer) {
        getChildren().forEach(child -> {
            TileView tv = (TileView)child;

            if(tv.getTile().getLayer() == layer)
                tv.setVisible(visible);
        });
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