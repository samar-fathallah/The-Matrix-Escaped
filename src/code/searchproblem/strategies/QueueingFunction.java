package code.searchproblem.strategies;

import java.util.LinkedList;
import code.searchproblem.general.SearchTreeNode;

public abstract class QueueingFunction {
    
    public abstract void enqueue(LinkedList<SearchTreeNode> queue, SearchTreeNode newNode);

}
