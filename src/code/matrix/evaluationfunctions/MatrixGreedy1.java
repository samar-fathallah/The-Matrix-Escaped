package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixGreedy1 extends EvaluationFunction {

    @Override
    public long evaluate(SearchTreeNode node) {
        return MatrixHeuristics.heuristic1(node);
    }

}
