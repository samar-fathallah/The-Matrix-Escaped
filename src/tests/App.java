package tests;

import code.generalsearchproblem.*;
import code.matrix.general.*;

public class App {

    public static void main(String[] args) {
        // String initialGrid = Matrix.genGrid();
        // initialGrid = "3,3;1;0,0;2,2;0,1,2,1;1,1;0,2,2,0,2,0,0,2;1,2,90";
        // String solution = Matrix.solve(initialGrid, "", true);
        // System.out.println(solution);

        String initialGrid = "3,3;1;0,0;2,2;0,1,2,1;1,1;0,2,2,0,2,0,0,2;1,2,90";
        SearchProblem matrix = new Matrix(initialGrid);
        State currentState = matrix.initialState;
        
        currentState = matrix.getNextState(currentState, MatrixOperator.DOWN);
        currentState = matrix.getNextState(currentState, MatrixOperator.RIGHT);
        currentState = matrix.getNextState(currentState, MatrixOperator.KILL);
        currentState = matrix.getNextState(currentState, MatrixOperator.TAKEPILL);
        currentState = matrix.getNextState(currentState, MatrixOperator.DOWN);
        currentState = matrix.getNextState(currentState, MatrixOperator.LEFT);
        currentState = matrix.getNextState(currentState, MatrixOperator.FLY);
        currentState = matrix.getNextState(currentState, MatrixOperator.DOWN);
        currentState = matrix.getNextState(currentState, MatrixOperator.CARRY);
        currentState = matrix.getNextState(currentState, MatrixOperator.DOWN);
        currentState = matrix.getNextState(currentState, MatrixOperator.DROP);
        System.out.println(matrix.goalTest(currentState));
    }
    
}
