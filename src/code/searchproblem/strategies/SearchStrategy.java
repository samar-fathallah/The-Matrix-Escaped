package code.searchproblem.strategies;

import java.lang.Class;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import code.searchproblem.general.*;
import code.searchproblem.strategies.queueingfunctions.*;

public class SearchStrategy {
    
    public static SearchTreeNode generalSearch(SearchProblem problem, QueueingFunction queueingFunction) {
        LinkedList<SearchTreeNode> queue = new LinkedList<SearchTreeNode>();
        queue.add(new SearchTreeNode(problem.initialState.encode()));
        while (!queue.isEmpty()) {
            SearchTreeNode currentNode = queue.removeFirst();
            State currentState;
            try {
                Class<?> stateClass = Class.forName(problem.stateClassName);
                Constructor<?> constructor = stateClass.getConstructor(String.class);
                currentState = (State) constructor.newInstance(currentNode.state);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            
            if (problem.goalTest(currentState)) {
                return currentNode;
            }
            for (Operator operator : problem.operators) {
                State nextState = problem.getNextState(currentState, operator);
                if (nextState != null) {
                    SearchTreeNode nextNode = new SearchTreeNode(nextState.encode(), currentNode, operator);
                    nextNode.pathCost = problem.pathCost(nextNode);
                    queueingFunction.enqueue(queue, nextNode);
                }
            }
        }
        return null;
    }

    public static SearchTreeNode breadthFirstSearch(SearchProblem problem) {
        return generalSearch(problem, new InsertAtEnd());
    }
    
    public static SearchTreeNode depthFirstSearch(SearchProblem problem) {
        return generalSearch(problem, new InsertAtFront());
    }

    public static SearchTreeNode depthLimitedSearch(SearchProblem problem, int maximumDepth) {
        return generalSearch(problem, new InsertAtFront(maximumDepth));
    }

    public static SearchTreeNode iterativeDeepeningSearch(SearchProblem problem) {
        int i = 0;
        while (true) {
            SearchTreeNode result = depthLimitedSearch(problem, i);
            if (result != null)
                return result;
            i++;
        }
    }
    
    public static SearchTreeNode uniformCostSearch(SearchProblem problem) {
        return generalSearch(problem, new OrderedInsert());
    }

    public static SearchTreeNode bestFirstSearch(SearchProblem problem, EvaluationFunction evaluationFunction) {
        QueueingFunction queueingFunction = new OrderedInsert(evaluationFunction);
        return generalSearch(problem, queueingFunction);
    }
    
}
