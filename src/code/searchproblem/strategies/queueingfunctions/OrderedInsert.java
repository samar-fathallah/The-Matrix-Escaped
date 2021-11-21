package code.searchproblem.strategies.queueingfunctions;

import java.util.LinkedList;
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
            for (int i = 0; i < queue.size(); i++) {
                SearchTreeNode currentNode = queue.get(i);
                if (newNode.pathCost < currentNode.pathCost) {
                    queue.add(i, newNode);
                    return;
                }
            }
            queue.addLast(newNode);
        }
        else {
            newNode.fValue = evaluationFunction.evaluate(newNode);
            for (int i = 0; i < queue.size(); i++) {
                SearchTreeNode currentNode = queue.get(i);
                if (newNode.fValue < currentNode.fValue) {
                    queue.add(i, newNode);
                    return;
                }
            }
            queue.addLast(newNode);
        }
    }

}
