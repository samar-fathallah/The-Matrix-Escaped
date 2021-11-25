package code.searchproblem.strategies.queueingfunctions;

import java.util.LinkedList;
import java.util.ListIterator;
import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;
import code.searchproblem.strategies.QueueingFunction;

public class OrderedInsert extends QueueingFunction {
    
    private boolean isBestFirst;
    private EvaluationFunction evaluationFunction;

    public OrderedInsert() {
        this.isBestFirst = false;
        this.evaluationFunction = null;
    }

    public OrderedInsert(EvaluationFunction evaluationFunction) {
        this.isBestFirst = true;
        this.evaluationFunction = evaluationFunction;
    }

    public void enqueue(LinkedList<SearchTreeNode> queue, SearchTreeNode newNode) {
        if (!isBestFirst) {
            ListIterator<SearchTreeNode> iterator = queue.listIterator();
            while (iterator.hasNext()) {
                SearchTreeNode currentNode = iterator.next();
                if (newNode.pathCost < currentNode.pathCost) {
                    iterator.previous();
                    iterator.add(newNode);
                    return;
                }
            }
            queue.addLast(newNode);
        }
        else {
            newNode.fValue = evaluationFunction.evaluate(newNode);
            ListIterator<SearchTreeNode> iterator = queue.listIterator();
            while (iterator.hasNext()) {
                SearchTreeNode currentNode = iterator.next();
                if (newNode.fValue < currentNode.fValue) {
                    iterator.previous();
                    iterator.add(newNode);
                    return;
                }
            }
            queue.addLast(newNode);
        }
    }

}
