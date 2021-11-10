package tests;

import code.matrix.general.Matrix;

public class App {

    public static void main(String[] args) {
        String initialGrid = Matrix.genGrid();
        String solution = Matrix.solve(initialGrid, "", true);
        System.out.println(solution);
    }
    
}
