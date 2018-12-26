package fh.sem.util.handler;


public class XMLNode {
    private String id;
    private XMLNode parent;

    public XMLNode(String id) {
        this(id, null);
    }

    public XMLNode(String id, XMLNode parent) {
        this.id = id;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public XMLNode getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !getClass().equals(other.getClass())
        || !(id.equals(((XMLNode)other).getId())))
            return false;

        XMLNode node = (XMLNode)other;
        return parent == null ? node.getParent() == null
            : parent.equals(node.getParent());
    }
}