package code.searchproblem.strategies.queueingfunctions;

import java.util.LinkedList;
import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.QueueingFunction;

public class InsertAtFront extends QueueingFunction {
    
    private boolean isLimited;
    private int limit;

    public InsertAtFront() {
        this.isLimited = false;
        this.limit = Integer.MAX_VALUE;
    }

    public InsertAtFront(int limit) {
        this.isLimited = true;
        this.limit = limit;
    }

    public void enqueue(LinkedList<SearchTreeNode> queue, SearchTreeNode newNode) {
        if (!isLimited || newNode.depth <= limit) {
            queue.addFirst(newNode);
        }
    }

}
