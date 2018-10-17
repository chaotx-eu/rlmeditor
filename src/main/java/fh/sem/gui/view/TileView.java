package fh.sem.gui.view;

import fh.sem.logic.Tile;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.beans.property.*;

public class TileView extends ImageView {
    private BooleanProperty solid;
    private Tile tile;

    public TileView(Tile tile, Image sheet) {
        setImage(sheet);
        
        // TODO set default image if sheet was not found
        setViewport(new Rectangle2D(
            tile.getX(), tile.getY(),
            tile.getWidth(), tile.getHeigth()));
        solid = new SimpleBooleanProperty(tile.isSolid());
        tile.addObserver((o, a) -> solid.set(tile.isSolid()));
        this.tile = tile;
    }
    
    public Tile getTile() {
        return tile;
    }
    
    public boolean isSolid() {
        return solid.get();
    }

    public BooleanProperty solidProperty() {
        return solid;
    }
}