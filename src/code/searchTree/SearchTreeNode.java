package code.searchTree;


import code.generalSearchProblem.general.Operator;
import code.generalSearchProblem.general.State;

public class SearchTreeNode {
    
    public State state;
    public SearchTreeNode parent;
    public Operator operator;
    public int depth;
    public int pathCost;

    // Constructor For Root Node Creation
    public SearchTreeNode (State currentState) {
        this.state=currentState.cloneState();
    }
    
    // Constructor For Other Nodes
    public SearchTreeNode (State currentState, SearchTreeNode parent, Operator myOperator) {
        this.state=currentState.cloneState();
        this.parent=parent;
        this.operator=myOperator;
        this.depth=parent.depth + 1;
        this.pathCost=parent.pathCost + myOperator.cost;
    }
}

