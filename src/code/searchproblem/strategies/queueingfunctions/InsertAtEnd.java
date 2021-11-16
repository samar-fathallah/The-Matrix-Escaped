package code.searchproblem.strategies.queueingfunctions;

import java.util.LinkedList;
import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.QueueingFunction;

public class InsertAtEnd extends QueueingFunction {
    
    public void enqueue(LinkedList<SearchTreeNode> queue, SearchTreeNode newNode) {
        queue.addLast(newNode);
    }

}
