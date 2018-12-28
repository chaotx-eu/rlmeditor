package fh.sem.gui.pane;

import java.util.HashMap;
import java.util.Map;

import fh.sem.gui.view.TileView;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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
    private DoubleProperty zoom;
    private IntegerProperty layer;

    private TileStackPane[][] tileStacks;
    private boolean selecting;
    private int selectedX;
    private int selectedY;

    private static final double MIN_ZOOM = 0.3f;
    private static final double MAX_ZOOM = 3f;
    private static final double MIN_TILE_SIZE = 2f;

    public MapPane(TileMap tileMap, MapTilesPane tilesPane) {
        tileX = new SimpleIntegerProperty();
        tileY = new SimpleIntegerProperty();
        tileZ = new SimpleIntegerProperty();
        tileRot = new SimpleIntegerProperty();
        tileSize = new SimpleDoubleProperty();
        tileSolid = new SimpleBooleanProperty();
        tileTitle = new SimpleStringProperty();
        selectionModeName = new SimpleStringProperty();
        zoom = new SimpleDoubleProperty(1f);

        tileSize.bind(Bindings.max(MIN_TILE_SIZE, Bindings.min(
            widthProperty().divide(tileMap.getWidth()),
            heightProperty().divide(tileMap.getHeight()))
            .multiply(zoom)));

        ScrollBar scb_zoom = new ScrollBar();
        Tooltip tlp_zoom = new Tooltip();
        scb_zoom.setMax(MAX_ZOOM*100f);
        scb_zoom.setMin(MIN_ZOOM*100f);
        scb_zoom.setValue(100f);
        scb_zoom.setTooltip(tlp_zoom);

        zoom.bind(scb_zoom.valueProperty().divide(100f));
        tlp_zoom.textProperty().bind(scb_zoom.valueProperty()
            .asString("Zoom: %.0f%%"));

        setOnZoom(e -> {
            double z = zoom.get() + (1f - e.getZoomFactor());
            scb_zoom.setValue((z < MIN_ZOOM ? MIN_ZOOM
                : z > MAX_ZOOM ? MAX_ZOOM : z)*100f);
        });

        // TODO (prototype)
        layer = new SimpleIntegerProperty();
        Text txt_layer = new Text();
        Button btn_layer_plus = new Button("+");
        Button btn_layer_min = new Button("-");
        CheckBox chb_layer_show = new CheckBox("Show");

        chb_layer_show.setSelected(true);
        txt_layer.textProperty().bind(layer.asString("Layer: %3d"));
        btn_layer_plus.setOnAction((e) -> layer.set(layer.get()+1));
        btn_layer_min.setOnAction((event) -> layer.set(Math.max(0, layer.get()-1)));

        HBox hbx_layer = new HBox(btn_layer_min, txt_layer, btn_layer_plus, chb_layer_show);
        hbx_layer.setAlignment(Pos.CENTER);
        hbx_layer.setSpacing(5);
        ///////////////////

        GridPane gdp_out = new GridPane();
        GridPane gdp_map = initGrid(tileMap, tilesPane);
        RowConstraints rc = new RowConstraints();
        ColumnConstraints cc = new ColumnConstraints();
        rc.setValignment(VPos.CENTER);
        cc.setHalignment(HPos.CENTER);
        rc.setPercentHeight(100);
        cc.setPercentWidth(100);
        rc.setFillHeight(false);
        cc.setFillWidth(false);

        gdp_out.getRowConstraints().add(rc);
        gdp_out.getColumnConstraints().add(cc);
        gdp_out.add(gdp_map, 0, 0);

        ScrollPane scr_map = new ScrollPane(gdp_out);
        VBox.setVgrow(scr_map, Priority.ALWAYS);

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

        gdp_map.setOnContextMenuRequested(e -> {
            if(!selecting)
                cmu_map.show(scr_map, e.getScreenX(), e.getScreenY());

            selecting = false;
        });

        scr_map.setOnMouseClicked(e -> cmu_map.hide());

        setFillWidth(true);
        setAlignment(Pos.CENTER);
        getChildren().addAll(scb_zoom, scr_map, hbx_layer);
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
    
    public DoubleProperty tileSize() {
        return tileSize;
    }

    private GridPane initGrid(TileMap tileMap, MapTilesPane tilesPane) {
        tileStacks = new TileStackPane[tileMap.getHeight()][];
        GridPane gdp_map = new GridPane();
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
                        // int n = -1;
                        // int z = layer.get();

                        // for(Tile tile = null; tile == null; z += n) {
                        //     tile = tileMap.getTile(fx, fy, z);

                        //     if(tile != null) {
                        //         tileSolid.set(tile.isSolid());
                        //         tileTitle.set(tile.getTitle());
                        //         tileZ.set(tile.getLayer());
                        //         tileRot.set(tile.getRotation());
                        //     } else if(z == 0) {
                        //         z = layer.get();
                        //         n = 1;
                        //     }
                        // }

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
                    if(tile != null)
                        tsp.setPrimary(new TileView(tile), tile.getLayer());
                });
                
                gdp_map.add(tsp, x, y);
            }
        }

        return gdp_map;
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