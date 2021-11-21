package code.matrix.general;

import code.searchproblem.general.Operator;

public enum MatrixOperator implements Operator {
    UP, DOWN, LEFT, RIGHT, CARRY, DROP, TAKEPILL, FLY, KILL;

    public static Operator getOperator(String opeartorString) {
        switch (opeartorString) {
            case "up": return MatrixOperator.UP;
            case "down": return MatrixOperator.DOWN;
            case "left": return MatrixOperator.LEFT;
            case "right": return MatrixOperator.RIGHT;
            case "carry": return MatrixOperator.CARRY;
            case "drop": return MatrixOperator.DROP;
            case "takePill": return MatrixOperator.TAKEPILL;
            case "fly": return MatrixOperator.FLY;
            case "kill": return MatrixOperator.KILL;
            default: return null;
        }
    }
    
}
