package fh.sem.util.handler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TileSetValidator {
    private List<XMLNode> validNodes;
    private Map<XMLNode, Integer> nodeCounts;
    private Map<XMLNode, Integer> maxNodeCounts;
    private Map<XMLNode, Integer> minNodeCounts;

    public TileSetValidator(XMLNode... nodes) {
        validNodes = new LinkedList<>();
        nodeCounts = new HashMap<>();
        maxNodeCounts = new HashMap<>();
        minNodeCounts = new HashMap<>();
        for(XMLNode node : nodes) validNodes.add(node);
    }

    public void setMaxCount(XMLNode node, int count) {
        if(validNodes.contains(node))
            maxNodeCounts.put(node, count);
    }

    public void setMinCount(XMLNode node, int count) {
        if(validNodes.contains(node))
            minNodeCounts.put(node, count);
    }

    public boolean validate() {
        return validate(null);
    }

    public boolean validate(XMLNode node) {
        if(node == null || validNodes.contains(node)) {
            for(XMLNode child : validNodes)
            if(child.getParent() == null ? node == null : child.getParent().equals(node)) {
                Integer count = nodeCounts.get(child);
                Integer max = maxNodeCounts.get(child);
                Integer min = minNodeCounts.get(child);
                if(count == null) count = 0;
                if(max == null) max = -1;
                if(min == null) min = 0;

                if(count < min || (max >= 0 && count > max))
                    return false;
            }

            resetCounter(node);
            if(node != null) {
                Integer count = nodeCounts.get(node);
                if(count == null) count = 0;
                nodeCounts.put(node, count + 1);
            }

            return true;
        }

        return false;
    }

    protected void resetCounter(XMLNode parent) {
        for(XMLNode node : validNodes)
            if(node.getParent() == null ? parent == null
            : node.getParent().equals(parent))
                nodeCounts.put(node, 0);
    }
}