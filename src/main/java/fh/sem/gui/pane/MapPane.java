package fh.sem.gui.pane;

import java.util.Random;

import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;

import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.beans.property.*;
import javafx.geometry.Pos;

public class MapPane extends VBox {
    private IntegerProperty  tileX;
    private IntegerProperty  tileY;
    private DoubleProperty  tileWidth;
    private DoubleProperty  tileHeight;
    private BooleanProperty tileSolid;
    private StringProperty  tileTitle;

    private boolean selecting;
    private int selectedX;
    private int selectedY;

    public MapPane(TileMap tileMap, MapTilesPane tilesPane) {
        GridPane gdp_map = new GridPane();
        Image sheet = new Image(tileMap.getSpriteSheet(), true);
        TileStackPane[][] tileStacks = new TileStackPane[tileMap.getHeight()][];
        Random rng = new Random();

        tileX = new SimpleIntegerProperty();
        tileY = new SimpleIntegerProperty();
        tileWidth = new SimpleDoubleProperty();
        tileHeight = new SimpleDoubleProperty();
        tileSolid = new SimpleBooleanProperty();
        tileTitle = new SimpleStringProperty();
        
        widthProperty().addListener((p, o, n) -> {
            if(n.doubleValue() < getHeight()) {
                tileWidth.bind(widthProperty().divide(tileMap.getWidth()));
                tileHeight.bind(tileWidth);
            }
        });

        heightProperty().addListener((p, o, n) -> {
            if(n.doubleValue() < getWidth()) {
                tileHeight.bind(heightProperty().divide(tileMap.getHeight()));
                tileWidth.bind(tileHeight);
            }
        });

        for(int y = 0, x; y < tileMap.getHeight(); ++y) {
            tileStacks[y] = new TileStackPane[tileMap.getWidth()];

            for(x = 0; x < tileMap.getWidth(); ++x) {
                TileStackPane tsp = new TileStackPane();
                
                tileStacks[y][x] = tsp;
                int r = rng.nextInt(255);
                int g = rng.nextInt(255);
                int b = rng.nextInt(255);

                tsp.setBackground(new Background(new BackgroundFill(
                    Color.rgb(r, g, b), null, null)));

                tsp.prefWidthProperty().bind(tileWidth);
                tsp.prefHeightProperty().bind(tileHeight);

                int fx = x, fy = y;
                tsp.setOnMouseEntered(e -> {
                    if(selecting) {
                        int lx = fx < selectedX ? fx : selectedX;
                        int ty = fy < selectedY ? fy : selectedY;
                        int rx = lx == fx ? selectedX : fx;
                        int by = ty == fy ? selectedY : fy;

                        for(int sy = 0, sx; sy < tileMap.getHeight(); ++sy) {
                            for(sx = 0; sx < tileMap.getWidth(); ++sx) {
                                if(sx >= lx && sx <= rx && sy >= ty && sy <= by) {
                                    tileStacks[sy][sx].setSecondary(new TileView(
                                        tilesPane.getSelection().getTile(), sheet));
                                } else tileStacks[sy][sx].removeSecondary();
                            }
                        }
                    }

                    Tile tile = tileStacks[fy][fx].getPrimary() != null
                        ? tileStacks[fy][fx].getPrimary().getTile() : null;

                    tileX.set(fx);
                    tileY.set(fy);

                    if(tile != null) {
                        tileSolid.set(tile.isSolid());
                        tileTitle.set(tilesPane.getTileSet()
                            .getTileTitles().get(tile));
                    } else {
                        tileSolid.set(true);
                        tileTitle.set("~");
                    }
                });

                tsp.setOnMouseClicked(e -> {
                    if(tilesPane.getSelection() == null) return;
                    if(e.getButton() == MouseButton.PRIMARY) {
                        if(selecting) {
                            int lx = fx < selectedX ? fx : selectedX;
                            int ty = fy < selectedY ? fy : selectedY;
                            int rx = lx == fx ? selectedX : fx;
                            int by = ty == fy ? selectedY : fy;

                            for(int vx; ty <= by; ++ty) for(vx = lx; vx <= rx; ++vx)
                                tileStacks[ty][vx].setPrimary(tileStacks[ty][vx]
                                    .getSecondary());

                            selecting = false;
                        } else tsp.setPrimary(new TileView(
                            tilesPane.getSelection().getTile(), sheet));
                    } else if(e.getButton() == MouseButton.SECONDARY) {
                        if(selecting) {
                            for(TileStackPane[] tiles : tileStacks)
                                for(TileStackPane tile : tiles)
                                    tile.removeSecondary();

                            selecting = false;
                        } else {
                            selectedX = fx;
                            selectedY = fy;
                            selecting = true;
                            tsp.setSecondary(new TileView(
                                tilesPane.getSelection().getTile(), sheet));
                        }
                    }
                });

                gdp_map.add(tsp, x, y);
            }
        }

        HBox hbx = new HBox(gdp_map);
        hbx.setAlignment(Pos.CENTER);

        setFillWidth(true);
        getChildren().add(hbx);
    }

    public IntegerProperty tileXProperty() {
        return tileX;
    }

    public IntegerProperty tileYProperty() {
        return tileY;
    }

    public DoubleProperty tileWidthProperty() {
        return tileWidth;
    }

    public DoubleProperty tileHeightProperty() {
        return tileHeight;
    }

    public BooleanProperty tileSolidProperty() {
        return tileSolid;
    }

    public StringProperty tileTitleProperty() {
        return tileTitle;
    }

}