package tests;

import code.matrix.general.*;

public class App {

    public static void main(String[] args) {
        String initialGrid = "3,3;1;0,0;2,2;0,1,2,1;1,1;0,2,2,0,2,0,0,2;1,2,90";
        // initialGrid = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
        String solution = Matrix.solve(initialGrid, "BF", true);
        System.out.println(solution);
    }
    
}
