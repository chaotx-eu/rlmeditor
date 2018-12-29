package fh.sem.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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

    public static final String MAP_XML_FOOT = (
        "\t</Asset>\n</XnaContent>\n");

    private static MapManager singleton;
    private MapManager() {};

    public static MapManager instance() {
        if(singleton == null)
            singleton = new MapManager();

        return singleton;
    }

    public String parseMap(TileMap map, String title) {
        String sheet;
        StringBuilder sb = new StringBuilder();
        sb.append(MAP_XML_HEAD);
        sb.append("\t\t<Title>" + title + "</Title>\n");
        sb.append("\t\t<Tiles>\n");

        for(int y = 0, x, z; y < map.getHeight(); ++y)
        for(x = 0; x < map.getWidth(); ++x)
        for(Tile tile : map.getTiles(x, y)) {
            z = tile.getLayer();
            sheet = tile.getSheet()
                .replaceFirst(Pattern.quote(App.APP_DIR + File.separator), "")
                .replace(File.separator, "/"); // most common seperator

            sb.append("\t\t\t<Tile>\n");
            sb.append("\t\t\t\t<SpriteSheet>"
                + sheet + "</SpriteSheet>\n");
            sb.append("\t\t\t\t<SpriteRectangle>"
                + tile.getX() + " " + tile.getY() + " "
                + tile.getWidth() + " "
                + tile.getHeight() + "</SpriteRectangle>\n");
            sb.append("\t\t\t\t<Position>"
                + x + " " + y
                + "</Position>\n");
            sb.append("\t\t\t\t<Rotation>"
                + tile.getRotation() + "</Rotation>\n");
            sb.append("\t\t\t\t<Layer>" + z + "</Layer>\n");
            sb.append("\t\t\t\t<Solid>" + tile.isSolid() + "</Solid>\n");
            sb.append("\t\t\t</Tile>\n");
        }
        
        sb.append("\t\t</Tiles>\n");
        sb.append(MAP_XML_FOOT);
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

    // TODO remove 'throws SAXException'
    public List<TileSet> loadTileSets() throws IOException, SAXException, ParserConfigurationException {
        Map<String, TileSet> tilesets = new HashMap<>();

        for(String demo_ts : App.DEMO_TILESETS) {
            try {
                TileSet ts = parseXML(getClass().getResourceAsStream(demo_ts));
                tilesets.put(ts.getTitle(), ts);
            } catch(SAXException e) {
                App.log.printf("%s\n %s\n",
                    e.getMessage(), "in \"" + demo_ts + "\"");
            }
        }

        File[] files = new File(App.TLS_DIR).listFiles();
        if(files != null) for(File tsf : files) {
            try {
                TileSet ts = parseXML(new FileInputStream(tsf));
                tilesets.put(ts.getTitle(), ts);
            } catch(SAXException e) {
                App.log.printf("%s\n %s\n",
                    e.getMessage(), "in \"" + tsf.getAbsolutePath() + "\"");
            }
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