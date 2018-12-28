package fh.sem.gui.pane;

import java.util.HashMap;
import java.util.Map;

import fh.sem.gui.pane.bar.ToolBar;
import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.*;

public class MapPane extends VBox {
    public enum SelectionMode {None, Single, Rectangular}
    private SelectionMode selectionMode;

    private IntegerProperty tileX;
    private IntegerProperty tileY;
    private IntegerProperty tileZ;
    private IntegerProperty tileRot;
    private BooleanProperty tileSolid;
    private StringProperty tileTitle;
    private DoubleProperty tileSize;

    private StringProperty selectionModeName;
    private IntegerProperty minLayer;
    private IntegerProperty maxLayer;
    private IntegerProperty layer;

    private TileStackPane[][] tileStacks;
    private boolean selecting;
    private int selectedX;
    private int selectedY;

    public static final int MAX_LAYER = 99;
    private static final double MIN_ZOOM = 0.5f;
    private static final double MAX_ZOOM = 3f;

    private TileMap tileMap;
    private MapTilesPane tilesPane;
    private GridPane mapGrid;

    public MapPane(TileMap tileMap, MapTilesPane tilesPane) {
        this.tileMap = tileMap;
        this.tilesPane = tilesPane;
        mapGrid = new GridPane();
        tileX = new SimpleIntegerProperty();
        tileY = new SimpleIntegerProperty();
        tileZ = new SimpleIntegerProperty();
        tileRot = new SimpleIntegerProperty();
        tileSize = new SimpleDoubleProperty();
        tileSolid = new SimpleBooleanProperty();
        tileTitle = new SimpleStringProperty();
        selectionModeName = new SimpleStringProperty();
        minLayer = new SimpleIntegerProperty();
        maxLayer = new SimpleIntegerProperty();
        layer = new SimpleIntegerProperty();

        tileSize.bind(Bindings.min(
            widthProperty().divide(tileMap.getWidth()),
            heightProperty().divide(tileMap.getHeight())
        ));

        GridPane gdp_out = new GridPane();
        RowConstraints rc = new RowConstraints();
        ColumnConstraints cc = new ColumnConstraints();
        rc.setValignment(VPos.CENTER);
        cc.setHalignment(HPos.CENTER);
        rc.setPercentHeight(100);
        cc.setPercentWidth(100);
        rc.setFillHeight(false);
        cc.setFillWidth(false);

        mapGrid.setCursor(Cursor.CROSSHAIR);
        gdp_out.getRowConstraints().add(rc);
        gdp_out.getColumnConstraints().add(cc);
        gdp_out.add(mapGrid, 0, 0);

        ZoomableScrollPane scr_map = new ZoomableScrollPane(gdp_out);
        VBox.setVgrow(scr_map, Priority.ALWAYS);
        scr_map.setMinZoom(MIN_ZOOM);
        scr_map.setMaxZoom(Math.max(MIN_ZOOM, Math.max(
            tileMap.getWidth(), tileMap.getHeight())/MAX_ZOOM));

        gdp_out.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
            scr_map.getViewportBounds().getWidth(), scr_map.viewportBoundsProperty()));

        gdp_out.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
            scr_map.getViewportBounds().getHeight(), scr_map.viewportBoundsProperty()));

        MenuItem mni_copy = new MenuItem("Select");
        Menu mnu_mode = new Menu("Draw Mode");
            RadioMenuItem mni_none = new RadioMenuItem("None");
            RadioMenuItem mni_single = new RadioMenuItem("Single");
            RadioMenuItem mni_rectan = new RadioMenuItem("Rectengular");
            mnu_mode.getItems().addAll(mni_none, mni_single, mni_rectan);
        Menu mnu_edit = new Menu("Edit"); // currently unused
            MenuItem mni_redo = new MenuItem("Redo");
            MenuItem mni_undo = new MenuItem("Undo");
            mnu_edit.getItems().addAll(mni_redo, mni_undo);
        ContextMenu cmu_map = new ContextMenu(mni_copy, mnu_mode);

        ToggleGroup tgroup_mode = new ToggleGroup();
        mni_none.setToggleGroup(tgroup_mode);
        mni_single.setToggleGroup(tgroup_mode);
        mni_rectan.setToggleGroup(tgroup_mode);

        mni_copy.setOnAction(e
            -> tilesPane.select(getTile(
                tileMap, tileX.get(),
                tileY.get(), tileZ.get())
        ));

        Map<Toggle, SelectionMode> tggModeMap = new HashMap<>();
        tggModeMap.put(mni_none, SelectionMode.None);
        tggModeMap.put(mni_single, SelectionMode.Single);
        tggModeMap.put(mni_rectan, SelectionMode.Rectangular);
        tgroup_mode.selectedToggleProperty().addListener((p, o, n) -> {
            if(n != null) {
                selectionMode = tggModeMap.get(n);
                selectionModeName.set(selectionMode.name());
            }
        });

        tgroup_mode.selectToggle(mni_single);

        mapGrid.setOnContextMenuRequested(e -> {
            if(!selecting)
                cmu_map.show(scr_map, e.getScreenX(), e.getScreenY());

            selecting = false;
        });

        scr_map.setOnMouseClicked(e -> cmu_map.hide());

        initGrid();
        setFillWidth(true);
        setAlignment(Pos.CENTER);
        getChildren().addAll(new ToolBar(this), scr_map);
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public IntegerProperty tileXProperty() {
        return tileX;
    }

    public IntegerProperty tileYProperty() {
        return tileY;
    }

    public IntegerProperty tileZProperty() {
        return tileZ;
    }

    public IntegerProperty tileRotationProperty() {
        return tileRot;
    }

    public BooleanProperty tileSolidProperty() {
        return tileSolid;
    }

    public StringProperty tileTitleProperty() {
        return tileTitle;
    }

    public StringProperty selectionModeNameProperty() {
        return selectionModeName;
    }

    public IntegerProperty minLayerProperty() {
        return minLayer;
    }

    public IntegerProperty maxLayerProperty() {
        return maxLayer;
    }

    public IntegerProperty layerProperty() {
        return layer;
    }
    
    public DoubleProperty tileSize() {
        return tileSize;
    }

    public int getMinLayer() {
        return minLayer.get();
    }

    public int getMaxLayer() {
        return maxLayer.get();
    }

    public int getLayer() {
        return layer.get();
    }

    public void setMinLayer(int layer) {
        minLayer.set(layer);
    }

    public void setMaxLayer(int layer) {
        maxLayer.set(layer);
    }

    public void setLayer(int layer) {
        this.layer.set(layer);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public TileStackPane[][] getTileStackPanes() {
        return tileStacks;
    }

    public void initGrid() {
        mapGrid.getChildren().clear();
        tileStacks = new TileStackPane[tileMap.getHeight()][];
        int chb_rid = 0, chb_cid = 1;
        
        for(int y = 0, x; y < tileMap.getHeight(); ++y) {
            tileStacks[y] = new TileStackPane[tileMap.getWidth()];
            chb_cid = chb_cid == 0 ? 1 : 0;
            chb_rid = chb_cid;

            for(x = 0; x < tileMap.getWidth(); ++x) {
                TileStackPane tsp = new TileStackPane();
                tsp.layerProperty().bind(layer);
                tsp.setId("check-board-" + (x > 0 ? chb_rid : chb_cid));
                tsp.prefWidthProperty().bind(tileSize);
                tsp.prefHeightProperty().bind(tileSize);
                tileStacks[y][x] = tsp;

                chb_rid = chb_rid == 0 ? 1 : 0;
                int fx = x, fy = y;
                
                tsp.setOnMouseEntered(e -> {
                    if(selectionMode == SelectionMode.Rectangular && selecting) {
                        int lx = fx < selectedX ? fx : selectedX;
                        int ty = fy < selectedY ? fy : selectedY;
                        int rx = lx == fx ? selectedX : fx;
                        int by = ty == fy ? selectedY : fy;

                        for(int sy = 0, sx; sy < tileMap.getHeight(); ++sy) {
                            for(sx = 0; sx < tileMap.getWidth(); ++sx) {
                                if(sx >= lx && sx <= rx && sy >= ty && sy <= by) {
                                    tileStacks[sy][sx].setSecondary(
                                        new TileView(tilesPane
                                            .getSelection()
                                            .getTile()));
                                } else tileStacks[sy][sx].removeSecondary();
                            }
                        }
                    }

                    tileX.set(fx);
                    tileY.set(fy);
                    
                    if(tileMap.getTiles(fx, fy).isEmpty()) {
                        tileSolid.set(false);
                        tileTitle.set("~");
                        tileZ.set(0);
                        tileRot.set(0);
                    } else {
                        Tile tile = getTile(tileMap, fx, fy, layer.get());
                        if(tile != null) {
                            tileSolid.set(tile.isSolid());
                            tileTitle.set(tile.getTitle());
                            tileZ.set(tile.getLayer());
                            tileRot.set(tile.getRotation());
                        }
                    }
                });

                tsp.setOnMouseClicked(e -> {
                    if(tilesPane.getSelection() == null) return;
                    if(e.getButton() == MouseButton.PRIMARY) {
                        if(selectionMode == SelectionMode.Rectangular) {
                            if(selecting) {
                                int lx = fx < selectedX ? fx : selectedX;
                                int ty = fy < selectedY ? fy : selectedY;
                                int rx = lx == fx ? selectedX : fx;
                                int by = ty == fy ? selectedY : fy;

                                for(int vx; ty <= by; ++ty) for(vx = lx; vx <= rx; ++vx) {
                                    tileStacks[ty][vx]
                                        .setPrimary(tileStacks[ty][vx]
                                        .getSecondary());

                                    tileMap.setTile(vx, ty, layer.get(), tileStacks[ty][vx].getPrimary() == null ?
                                        null : tileStacks[ty][vx].getPrimary().getTile().copy());
                                }

                                selecting = false;
                            } else {
                                selectedX = fx;
                                selectedY = fy;
                                selecting = true;

                                if(tilesPane.getSelection() != null) {
                                    tsp.setSecondary(new TileView(tilesPane
                                        .getSelection()
                                        .getTile()));
                                }
                            }
                        } else if(selectionMode == SelectionMode.Single) {
                            tsp.setPrimary(new TileView(tilesPane.getSelection().getTile()));
                            tileMap.setTile(fx, fy, layer.get(), tsp.getPrimary().getTile().copy());
                        }
                    } else if(e.getButton() == MouseButton.SECONDARY) {
                        if(selecting)
                            for(TileStackPane[] tiles : tileStacks)
                                for(TileStackPane tile : tiles)
                                    tile.removeSecondary();
                    }
                });

                tileMap.getTiles(x, y).forEach(tile -> {
                    if(tile != null) {
                        tsp.setPrimary(new TileView(tile), tile.getLayer());
                        maxLayer.set(Math.max(maxLayer.get(), tile.getLayer()));
                    }
                });
                
                mapGrid.add(tsp, x, y);
            }
        }
    }

    public void removeTile(Tile tile) {
        int[] pos = tile.getPosition();
        tileStacks[pos[1]][pos[0]].removePrimary(pos[2]);
        tileMap.removeTile(tile);
    }

    private Tile getTile(TileMap tileMap, int x, int y, int z) {
        if(tileMap.getTiles(x, y).size() == 0) return null;
        Tile tile = null;

        for(int n = -1; tile == null; z += n) {
            tile = tileMap.getTile(x, y, z);

            if(tile != null) {
                tileSolid.set(tile.isSolid());
                tileTitle.set(tile.getTitle());
                tileZ.set(tile.getLayer());
                tileRot.set(tile.getRotation());
            } else if(z == 0) {
                z = layer.get();
                n = 1;
            }
        }

        return tile;
    }
}