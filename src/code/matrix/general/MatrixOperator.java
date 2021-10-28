package code.matrix.general;

import code.generalSearchProblem.general.Operator;

public class MatrixOperator extends Operator {

    public MatrixOperator(MatrixOperatorType type, int cost) {
        this.type = type;
        this.cost = cost;
    }

}