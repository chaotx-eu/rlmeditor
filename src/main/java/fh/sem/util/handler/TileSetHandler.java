package fh.sem.util.handler;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fh.sem.App;
import fh.sem.logic.Tile;
import fh.sem.logic.TileSet;

public class TileSetHandler extends DefaultHandler {
    private TileSet tileSet;

    public TileSetHandler(TileSet ts) {
        super();
        tileSet = ts;
    }

    @Override
    public void startDocument() {
        // nothing to do here (yet)
    }

    @Override
    public void endDocument() {
        // nothing here (yet)
    }

    private String value = "", category, subcategory;
    private int anonymous_tile = 0;

    @Override
    public void startElement(String ns, String localName, String qName, Attributes atts) {
        if(qName.equals("tileSet")) tileSet.setTitle(atts.getValue("title"));
        if(qName.equals("category")) category = atts.getValue("title");
        if(qName.equals("subcategory")) subcategory = atts.getValue("title");
        if(qName.equals("tile")) {
            Integer x = Integer.parseInt(atts.getValue("x")); // not optional
            Integer y = Integer.parseInt(atts.getValue("y")); // not optional
            Integer w = Integer.parseInt(atts.getValue("w")); // not optional
            Integer h = atts.getValue("h") != null ? Integer.parseInt(atts.getValue("h")) : 0;
            Integer r = atts.getValue("e") != null ? Integer.parseInt(atts.getValue("r")) : 0;
            Boolean s = atts.getValue("solid") != null ? Boolean.parseBoolean(atts.getValue(("solid"))) : false;
            String title;
            
            if(atts.getValue("title") == null)
                title = "Tile_" + anonymous_tile++;
            else
                title = atts.getValue("title");

            Tile tile = new Tile(
                x, y, w,
                h == null ? w : h,
                r == null ? 0 : r,
                s == null ? false : s
            );

            if(category == null)
                tileSet.addTile(title, tile);
            else if(subcategory == null)
                tileSet.addTile(title, category, tile);
            else
                tileSet.addTile(title, category, subcategory, tile);
        }
    }

    @Override
    public void endElement(String ns, String localName, String qName) throws SAXException {
        if(qName.equals("sheet")) {
            String img = value.trim();
            String intern = "/images/" + img;
            String extern = App.IMG_DIR + File.separator + img;

            tileSet.setSheet(!(new File(extern).exists())
                && getClass().getResource(intern) != null
                ? intern : extern);
        }

        if(qName.equals("category")) category = null;
        if(qName.equals("subcategory")) subcategory = null;
        value = "";
    }

    @Override
    public void characters(char[] chars, int s, int l) {
        for(int i = s; i < s+l; ++i)
            value += chars[i];
    }
}