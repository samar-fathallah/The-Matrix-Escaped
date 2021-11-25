package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixGreedy2 extends EvaluationFunction {

    @Override
    public int evaluate(SearchTreeNode node) {
        return MatrixHeuristics.heuristic2(node);
    }

}
