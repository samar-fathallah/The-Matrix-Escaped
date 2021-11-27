package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixAStar1 extends EvaluationFunction {

    @Override
    public long evaluate(SearchTreeNode node) {
        return node.pathCost + MatrixHeuristics.admissableHeuristic1(node);
    }

}
