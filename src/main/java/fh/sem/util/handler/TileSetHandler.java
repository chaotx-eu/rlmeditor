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
    private TileSetValidator validator;
    private XMLNode currentNode;

    private String value = "", sheet, category, subcategory, tileTitle;
    private int tileX, tileY, tileW, tileH, tileRot;
    private int tileCount = 0;
    private boolean tileSolid;

    public TileSetHandler(TileSet ts) {
        super();
        tileSet = ts;

        // xml syntax validator
        XMLNode tileSetNode = new XMLNode("tileSet");
        XMLNode sheetNode = new XMLNode("sheet", tileSetNode);
        XMLNode categoryNode = new XMLNode("category", sheetNode);
        XMLNode subCategoryNode = new XMLNode("subCategory", categoryNode);
        XMLNode tileNode1 = new XMLNode("tile", sheetNode);
        XMLNode tileNode2 = new XMLNode("tile", categoryNode);
        XMLNode tileNode3 = new XMLNode("tile", subCategoryNode);
        XMLNode positionNode1 = new XMLNode("position", tileNode1);
        XMLNode sizeNode1 = new XMLNode("size", tileNode1);
        XMLNode rotationNode1 = new XMLNode("rotation", tileNode1);
        XMLNode solidNode1 = new XMLNode("solid", tileNode1);
        XMLNode positionNode2 = new XMLNode("position", tileNode2);
        XMLNode sizeNode2 = new XMLNode("size", tileNode2);
        XMLNode rotationNode2 = new XMLNode("rotation", tileNode2);
        XMLNode solidNode2 = new XMLNode("solid", tileNode2);
        XMLNode positionNode3 = new XMLNode("position", tileNode3);
        XMLNode sizeNode3 = new XMLNode("size", tileNode3);
        XMLNode rotationNode3 = new XMLNode("rotation", tileNode3);
        XMLNode solidNode3 = new XMLNode("solid", tileNode3);
        // XMLNode layerNode = new XMLNode("layer", tileNode); // TODO
        validator = new TileSetValidator(
            tileNode1, tileNode2, tileNode3, sheetNode,
            tileSetNode, categoryNode, subCategoryNode,
            positionNode1, sizeNode1, rotationNode1, solidNode1,
            positionNode2, sizeNode2, rotationNode2, solidNode2,
            positionNode3, sizeNode3, rotationNode3, solidNode3);

        validator.setMaxCount(tileSetNode, 1);
        validator.setMaxCount(positionNode1, 1);
        validator.setMaxCount(sizeNode1, 1);
        validator.setMaxCount(rotationNode1, 1);
        validator.setMaxCount(solidNode1, 1);
        validator.setMaxCount(positionNode2, 1);
        validator.setMaxCount(sizeNode2, 1);
        validator.setMaxCount(rotationNode2, 1);
        validator.setMaxCount(solidNode2, 1);
        validator.setMaxCount(positionNode3, 1);
        validator.setMaxCount(sizeNode3, 1);
        validator.setMaxCount(rotationNode3, 1);
        validator.setMaxCount(solidNode3, 1);
        // validator.setMaxCount(layerNode, 1);

        validator.setMinCount(tileSetNode, 1);
        validator.setMinCount(sheetNode, 1);
        validator.setMinCount(positionNode1, 1);
        validator.setMinCount(positionNode2, 1);
        validator.setMinCount(positionNode3, 1);
        validator.setMinCount(sizeNode1, 1);
        validator.setMinCount(sizeNode2, 1);
        validator.setMinCount(sizeNode3, 1);
    }

    @Override
    public void startDocument() {
        // nothing to do here (yet)
    }

    @Override
    public void endDocument() throws SAXException {
        // validate root node
        if(!validator.validate())
            throw new SAXException("Malformed xml: bad structure in root node");
    }

    @Override
    public void startElement(String ns, String localName, String qName, Attributes atts) {
        // TODO validate parameters
        if(qName.equals("tileSet"))             tileSet.setTitle(atts.getValue("title"));
        else if(qName.equals("category"))       category = atts.getValue("title");
        else if(qName.equals("subcategory"))    subcategory = atts.getValue("title");
        else if(qName.equals("tile"))           tileTitle = atts.getValue("title");
        else if(qName.equals("sheet")) {
            String img = atts.getValue("file");
            String intern = "/images/maps/" + img;
            String extern = App.IMG_DIR + File.separator + img;
            sheet = !(new File(extern).exists())
                && getClass().getResource(intern) != null
                ? intern : extern;
        }
        
        currentNode = new XMLNode(qName, currentNode);
        value = "";
    }

    @Override
    public void endElement(String ns, String localName, String qName) throws SAXException {
        if(!validator.validate(currentNode)) {
            throw new SAXException("Malformed xml: bad structure in "
                + (currentNode == null ? "root node" : "'" + currentNode.getId() + "'"));
        }

        try {
            if(qName.equals("position")) {
                String[] pos = value.split(" ");
                if(pos.length != 2)
                    throw new IllegalArgumentException();

                tileX = Integer.parseInt(pos[0]);
                tileY = Integer.parseInt(pos[1]);
            } else if(qName.equals("size")) {
                String[] size = value.split(" ");
                if(size.length > 2)
                    throw new IllegalArgumentException();

                tileW = Integer.parseInt(size[0]);
                tileH = size.length == 1 ? tileW
                    : Integer.parseInt(size[1]);
            } else if(qName.equals("rotation")) {
                tileRot = Integer.parseInt(value);
            } else if(qName.equals("solid")) {
                String solid = value.trim().toLowerCase();
                if(solid.equals("true")) tileSolid = true;
                else if(solid.equals("false")) tileSolid = false;
                else throw new IllegalArgumentException();
            } else if(qName.equals("tile")) {
                Tile tile = new Tile(sheet, tileTitle == null ? ("Tile_" + tileCount++)
                    : tileTitle, tileX, tileY, tileW, tileH, tileRot, tileSolid);

                if(category == null)
                    tileSet.addTile(tile);
                else if(subcategory == null)
                    tileSet.addTile(tile, category);
                else {
                    tileSet.addTile(tile, category, subcategory);
                }

                // reset to default
                tileTitle = null;
                tileX = tileY = tileW = -1;
                tileRot = 0;
                tileSolid = false;
            } else if(qName.equals("category")) {
                category = null;
            } else if(qName.equals("subcategory")) {
                subcategory = null;
            }

            currentNode = currentNode.getParent();
        } catch(IllegalArgumentException e) {
            throw new SAXException("Malformed xml: invalid value format in "
                + (currentNode == null ? "root node" : "'" + currentNode.getId() + "'"));
        }
    }

    @Override
    public void characters(char[] chars, int s, int l) {
        for(int i = s; i < s+l; ++i)
            value += chars[i];
    }
}