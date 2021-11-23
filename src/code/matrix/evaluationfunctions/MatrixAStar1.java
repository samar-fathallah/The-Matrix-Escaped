package code.matrix.evaluationfunctions;

import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixAStar1 extends EvaluationFunction {

    @Override
    public int evaluate(SearchTreeNode node) {
        return node.pathCost + MatrixHeuristics.h1(node);
    }

}
