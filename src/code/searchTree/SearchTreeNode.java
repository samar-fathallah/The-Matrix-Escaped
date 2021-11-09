package code.searchTree;

import code.generalSearchProblem.Operator;

public class SearchTreeNode {
    
    public String state;
    public SearchTreeNode parent;
    public Operator operator;
    public int depth;
    public int pathCost;

    // Constructor For Root Node Creation
    public SearchTreeNode(String state) {
        this.state = state;
    }
    
    // Constructor For Other Nodes
    public SearchTreeNode(String state, SearchTreeNode parent, Operator operator, int pathCost) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.depth = parent.depth + 1;
        this.pathCost = pathCost;
    }
}

