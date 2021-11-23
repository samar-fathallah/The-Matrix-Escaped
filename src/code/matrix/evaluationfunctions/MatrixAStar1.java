package code.matrix.evaluationfunctions;

import code.matrix.general.MatrixState;
import code.searchproblem.general.SearchTreeNode;
import code.searchproblem.strategies.EvaluationFunction;

public class MatrixAStar1 extends EvaluationFunction {

    @Override
    public int evaluate(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);
        return MatrixHeuristics.h1(state) + node.pathCost;
    }

}
