package code.matrix;

import java.util.ArrayList;

import code.generalSearchProblem.GeneralSearchProblem;
import code.generalSearchProblem.general.Operator;
import code.generalSearchProblem.general.State;
import code.matrix.general.MatrixState;
import code.matrix.helpers.*;


public class Matrix extends GeneralSearchProblem {

    public Matrix(String grid) {
        this.initialState=new MatrixState(grid);
    }
    public void visualizeGrid() {
        Object[][] grid=((MatrixState)this.initialState).grid;
        for (int i = 0; i < grid.length; i++) {
            System.out.print("[  ");
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print((grid[i][j] != null ? grid[i][j].toString().split("@")[0].substring(13, 15) : "NA") + "  ");
            }
            System.out.println("]");
        }
    }
    
    public static String genGrid() {
        // Generate grid size
        int m = HelperMethods.genrateRandomInt(5, 16);
        int n = HelperMethods.genrateRandomInt(5, 16);
    
        // Generate number of hostages, pills, pads and agents
        int numberOfAvailablePositions = m * n;
        numberOfAvailablePositions -= 2;
        int numberOfHostages = HelperMethods.genrateRandomInt(3, 11);
        numberOfAvailablePositions -= numberOfHostages;
        int numberOfPills = HelperMethods.genrateRandomInt(1, numberOfHostages+1);
        numberOfAvailablePositions -= numberOfPills;
        int numberOfPads = HelperMethods.genrateRandomInt(1, (int) Math.ceil(numberOfAvailablePositions/2.0));
        numberOfAvailablePositions -= numberOfPads * 2;
        int numberOfAgents = HelperMethods.genrateRandomInt(1, numberOfAvailablePositions+1);
        numberOfAvailablePositions -= numberOfAgents;
    
        // Generate maximum number of hostages that can be carried
        int c = HelperMethods.genrateRandomInt(1, 5);
    
        // Populate grid
        ArrayList<Position> taken = new ArrayList<Position>();
    
        Position neoPosition = HelperMethods.genrateRandomPosition(m, n, taken);
        taken.add(neoPosition);
    
        Position telephoneBoothPosition = HelperMethods.genrateRandomPosition(m, n, taken);
        taken.add(telephoneBoothPosition);
    
        ArrayList<Position> hostagesPositions = new ArrayList<Position>();
        ArrayList<Integer> hostagesDamages = new ArrayList<Integer>();
        for (int i = 0; i < numberOfHostages; i++) {
            Position hostagePosition = HelperMethods.genrateRandomPosition(m, n, taken);
            hostagesPositions.add(hostagePosition);
            taken.add(hostagePosition);
            hostagesDamages.add(HelperMethods.genrateRandomInt(1, 100));
        }
    
        ArrayList<Position> pillsPositions = new ArrayList<Position>();
        for (int i = 0; i < numberOfPills; i++) {
            Position pillPosition = HelperMethods.genrateRandomPosition(m, n, taken);
            pillsPositions.add(pillPosition);
            taken.add(pillPosition);
        }
    
        ArrayList<Position> startPadsPositions = new ArrayList<Position>();
        ArrayList<Position> finishPadsPositions = new ArrayList<Position>();
        for (int i = 0; i < numberOfPads; i++) {
            Position startPadPosition = HelperMethods.genrateRandomPosition(m, n, taken);
            taken.add(startPadPosition);
            startPadsPositions.add(startPadPosition);
            Position finishPadPosition = HelperMethods.genrateRandomPosition(m, n, taken);
            taken.add(finishPadPosition);
            finishPadsPositions.add(finishPadPosition);
        }
    
        ArrayList<Position> agentsPositions = new ArrayList<Position>();
        for (int i = 0; i < numberOfAgents; i++) {
            Position agentPosition = HelperMethods.genrateRandomPosition(m, n, taken);
            agentsPositions.add(agentPosition);
            taken.add(agentPosition);
        }
    
        // Encoding the grid in a string
        String grid = "";
        grid += m + "," + n + ";" + c + ";";
        grid += neoPosition.x + "," + neoPosition.y + ";";
        grid += telephoneBoothPosition.x + "," + telephoneBoothPosition.y + ";";
        for (int i = 0; i < agentsPositions.size(); i++) {
            grid += agentsPositions.get(i).x + "," + agentsPositions.get(i).y;
            grid += i == agentsPositions.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < pillsPositions.size(); i++) {
            grid += pillsPositions.get(i).x + "," + pillsPositions.get(i).y;
            grid += i == pillsPositions.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < startPadsPositions.size(); i++) {
            grid += startPadsPositions.get(i).x + "," + startPadsPositions.get(i).y + ",";
            grid += finishPadsPositions.get(i).x + "," + finishPadsPositions.get(i).y + ",";
            grid += finishPadsPositions.get(i).x + "," + finishPadsPositions.get(i).y + ",";
            grid += startPadsPositions.get(i).x + "," + startPadsPositions.get(i).y;
            grid += i == startPadsPositions.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < hostagesPositions.size(); i++) {
            grid += hostagesPositions.get(i).x + "," + hostagesPositions.get(i).y + ",";
            grid += hostagesDamages.get(i);
            grid += i == hostagesPositions.size()-1 ? "" : ",";
        }
    
        return grid;
    }

    public static String solve(String grid, String startegy, boolean visualize) {
        Matrix matrix = new Matrix(grid);
        if (visualize) {
            matrix.visualizeGrid();
        }
        return "";
    }

    @Override
    public boolean goalTest(State currentState) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int pathCost(ArrayList<Operator> sequenceOfOperators) {
        // TODO Auto-generated method stub
        return 0;
    }

}
