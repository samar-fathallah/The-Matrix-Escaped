package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixGreedy1 extends EvaluationFunction {

    @Override
    public int evaluate(SearchTreeNode node) {
        return MatrixHeuristics.heuristic1(node);
    }

}
