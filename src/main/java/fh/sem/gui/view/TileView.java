package fh.sem.gui.view;

import fh.sem.logic.Tile;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.beans.property.*;

public class TileView extends ImageView {
    private Tile tile;
    private DoubleProperty scale;
    private BooleanProperty solid;

    public TileView(Tile tile, Image sheet) {
        setImage(sheet);
        setRotate(tile.getRotation());
        
        // TODO set default image if sheet was not found
        // TODO fix viewport not perfectly encasing the
        //      specified area in the image resulting in
        //      artifacts of adjacent sprites in eachother
        // -> possible cause: fx image scaling algorithm (no point clamp)
        // -> possible cause: image format not optimal (though it works fine in monogame)
        setViewport(new Rectangle2D(
            tile.getX(), tile.getY(),
            tile.getWidth(), tile.getHeight()));

        this.tile = tile;
        scale = new SimpleDoubleProperty(1);
        solid = new SimpleBooleanProperty(tile.isSolid());
        tile.addObserver((o, a) -> solid.set(tile.isSolid()));
    }
    
    public Tile getTile() {
        return tile;
    }
    
    public boolean isSolid() {
        return solid.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public double getScale() {
        return scale.get();
    }

    public DoubleProperty scaleProperty() {
        return scale;
    }

    public BooleanProperty solidProperty() {
        return solid;
    }
}