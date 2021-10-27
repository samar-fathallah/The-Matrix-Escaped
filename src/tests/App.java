package tests;

import code.Matrix;

public class App {
    public static void main(String[] args) {
        String solution = Matrix.solve(Matrix.genGrid(), "", true);
        System.out.println(solution);
    }
}
