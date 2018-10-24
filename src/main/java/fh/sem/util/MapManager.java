package fh.sem.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import fh.sem.App;
import fh.sem.logic.Tile;
import fh.sem.logic.TileMap;
import fh.sem.logic.TileSet;
import fh.sem.util.handler.TileSetHandler;

public class MapManager {
    public static final String MAP_XML_HEAD = (
          "<?xml version='1.0' encoding='utf-8'?>\n"
        + "<XnaContent xmlns:ns='Microsoft.Xna.Framework'>\n"
        + "\t<Asset Type='TileMap'>\n");

    public static final String MAP_XML_FOOD = (
        "\t</Asset>\n</XnaContent>\n");

    private static MapManager singleton;
    private MapManager() {};

    public static MapManager instance() {
        if(singleton == null)
            singleton = new MapManager();

        return singleton;
    }

    public String parseMap(TileMap map, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append(MAP_XML_HEAD);
        sb.append("\t\t<Title>" + title + "</Title>\n");
        sb.append("\t\t<Tiles>\n");

        for(int y = 0, x; y < map.getHeight(); ++y) {
            for(x = 0; x < map.getWidth(); ++x) {
                Tile tile = map.getTile(x, y);

                if(tile != null) {
                    sb.append("\t\t\t<Tile>\n");
                    sb.append("\t\t\t\t<SpriteSheet>"
                        + map.getSheet() + "</SpriteSheet>\n");
                    sb.append("\t\t\t\t<SpriteRectangle>"
                        + tile.getX() + " " + tile.getY() + " "
                        + tile.getWidth() + " "
                        + tile.getHeight() + "</SpriteRectangle>\n");
                    sb.append("\t\t\t\t<Rotation>"
                        + tile.getRotation() + "</Rotation>\n");
                    sb.append("\t\t\t\t<Position>"
                        + x + " " + y
                        + "</Position>\n");
                    sb.append("\t\t\t\t<Solid>" + tile.isSolid() + "</Solid>\n");
                    sb.append("\t\t\t</Tile>\n");
                }
            }
        }
        
        sb.append("\t\t</Tiles>\n");
        sb.append(MAP_XML_FOOD);
        return sb.toString();
    }

    public void exportMap(File file, String mapData) {
        try(BufferedOutputStream bos = new BufferedOutputStream(
            new FileOutputStream(file))) {

            for(char c : mapData.toCharArray())
                bos.write(c);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String importMap(File file) {
        StringBuilder data = new StringBuilder();

        try(BufferedInputStream bis = new BufferedInputStream(
            new FileInputStream(file))) {

            int d;
            while((d = bis.read()) >= 0)
                data.append((char)d);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return data.toString();
    }

    public List<TileSet> loadTileSets() throws IOException, SAXException, ParserConfigurationException {
        String dir_path = App.TLS_DIR;
        String res_path = "/tilesets/";
        Map<String, TileSet> tilesets = new HashMap<>();

        try(InputStream ins = getClass().getResourceAsStream(res_path)) {
            StringBuilder name = new StringBuilder();
            int c;

            while((c = ins.read()) > 0) {
                if(c == '\n') {
                    TileSet ts = parseXML(getClass().getResourceAsStream(
                            res_path + name.toString()));

                    tilesets.put(ts.getTitle(), ts);
                    name = new StringBuilder();
                } else name.append((char)c);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        File[] files = new File(dir_path).listFiles();
        if(files != null) for(File tsf : files) {
            TileSet ts = parseXML(new FileInputStream(tsf));
            tilesets.put(ts.getTitle(), ts);
        }

        return tilesets.keySet()
            .stream().sorted()
            .map(k -> tilesets.get(k))
            .collect(Collectors.toList());
    }

    public TileSet parseXML(InputStream ins) throws IOException, SAXException, ParserConfigurationException {
        TileSet ts = new TileSet();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(ins, new TileSetHandler(ts));
        return ts;
    }
}