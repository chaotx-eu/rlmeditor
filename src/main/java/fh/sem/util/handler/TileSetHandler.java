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

    @Override
    public void startElement(String ns, String localName, String qName, Attributes atts) {
        if(qName.equals("tileSet")) tileSet.setTitle(atts.getValue("title"));
        if(qName.equals("category")) category = atts.getValue("title");
        if(qName.equals("subcategory")) subcategory = atts.getValue("title");
        if(qName.equals("tile")) {
            Tile tile = new Tile(
                Integer.parseInt(atts.getValue("x")),
                Integer.parseInt(atts.getValue("y")),
                Integer.parseInt(atts.getValue("w")),
                Integer.parseInt(atts.getValue("h")),
                Boolean.parseBoolean(atts.getValue("solid"))
            );

            if(category == null)
                tileSet.addTile(atts.getValue("title"), tile);
            else if(subcategory == null)
                tileSet.addTile(atts.getValue("title"), category, tile);
            else
                tileSet.addTile(atts.getValue("title"), category, subcategory, tile);
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