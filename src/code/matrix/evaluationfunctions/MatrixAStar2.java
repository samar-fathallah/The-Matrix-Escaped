package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixAStar2 extends EvaluationFunction {

    @Override
    public long evaluate(SearchTreeNode node) {
        return node.pathCost + MatrixHeuristics.admissableHeuristic2(node);
    }

}
