package code;

import java.util.ArrayList;
import java.util.LinkedList;

import code.searchproblem.general.*;
import code.searchproblem.strategies.SearchStrategy;
import code.matrix.evaluationfunctions.*;
import code.matrix.general.MatrixOperator;
import code.matrix.general.MatrixState;
import code.matrix.helpers.*;
import code.matrix.objects.*;

public class Matrix extends SearchProblem {

    public Matrix(String initialGrid) {
        // Create and populate the array of opeators
        this.operators = new ArrayList<Operator>(9);
        this.operators.add(MatrixOperator.UP);
        this.operators.add(MatrixOperator.DOWN);
        this.operators.add(MatrixOperator.LEFT);
        this.operators.add(MatrixOperator.RIGHT);
        this.operators.add(MatrixOperator.CARRY);
        this.operators.add(MatrixOperator.DROP);
        this.operators.add(MatrixOperator.TAKEPILL);
        this.operators.add(MatrixOperator.FLY);
        this.operators.add(MatrixOperator.KILL);
        
        // Split the state info from the genGrid output
        String[] splitState = initialGrid.split(";");
        
        // Initialize neo damage to 0
        splitState[2] += ",0";
        
        // Initialize agent isKilled to false
        String[] splitAgentsInfo = splitState[4].split(",");
        for (int i = 1; i < splitAgentsInfo.length; i+=2) {
            splitAgentsInfo[i] += ",f";
        }
        splitState[4] = String.join(",", splitAgentsInfo);

        // Initialize pills isTaken to false
        String[] splitPillsInfo = splitState[5].split(",");
        for (int i = 1; i < splitPillsInfo.length; i+=2) {
            splitPillsInfo[i] += ",f";
        }
        splitState[5] = String.join(",", splitPillsInfo);
        
        // Initialize hostages isAgent, isKilled, and isCarried to false
        String[] splitHostagesInfo = splitState[7].split(",");
        for (int i = 2; i < splitHostagesInfo.length; i+=3) {
            splitHostagesInfo[i] += ",f,f,f";
        }
        splitState[7] = String.join(",", splitHostagesInfo);

        // Rejoin the state info after adding the extra info 
        String initialStateString = String.join(";", splitState);

        // Create the initial state object
        this.initialState = new MatrixState(initialStateString);

        // Set the name of the state class and the number of expanded nodes
        this.stateClassName = this.initialState.getClass().getName();
        this.expandedNodes = 0;
    }
    
    @Override
    public State getNextState(State state, Operator operator) {
        MatrixState matrixState = (MatrixState) state;
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        
        if (matrixState.neo.damage >= 100) {
            return null;
        }

        if (matrixState.isValidOperator(operator)) {
            State nextState = matrixState.clone();
            nextState.updateState(matrixOperator);
            return nextState;
        }
        
         return null;
    }

    @Override
    public boolean goalTest(State state) {
        MatrixState matrixState = (MatrixState) state;
        boolean neoAtBooth = matrixState.neo.position.equals(matrixState.telephoneBooth.position);
        boolean hostagesDisappeared = true;
        for (Hostage hostage : matrixState.hostages) {
            if (hostage.isAgent) {
                if (!hostage.isKilled) {
                    hostagesDisappeared = false;
                    break;
                }
            }
            else if (!hostage.position.equals(matrixState.telephoneBooth.position) || hostage.isCarried) {
                hostagesDisappeared = false;
                break;
            }
        }
        return neoAtBooth && hostagesDisappeared;
    }
    
    @Override
    public int pathCost(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);
        int numberOfAgents = state.agents.size();
        int numberOfHostages = state.hostages.size();
        int base = Integer.max(numberOfAgents, numberOfHostages);
        int numberOfDeaths = state.getDeaths();
        int numberOfKills = state.getAgentKills();
        int pathCost = numberOfKills + (base * numberOfDeaths);
        return pathCost;
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

    public static String solve(String initialGrid, String strategy, boolean visualize) {
        Matrix matrix = new Matrix(initialGrid);
        
        SearchTreeNode goalNode = null;
        switch (strategy) {
            case "BF": {
                goalNode = SearchStrategy.breadthFirstSearch(matrix);
                break;
            }
            case "DF": {
                goalNode = SearchStrategy.depthFirstSearch(matrix);
                break;
            }
            case "ID": {
                goalNode = SearchStrategy.iterativeDeepeningSearch(matrix);
                break;
            }
            case "UC": {
                goalNode = SearchStrategy.uniformCostSearch(matrix);
                break;
            }
            case "GR1": {
                goalNode = SearchStrategy.bestFirstSearch(matrix, new MatrixGreedy1());
                break;
            }
            case "GR2": {
                goalNode = SearchStrategy.bestFirstSearch(matrix, new MatrixGreedy2());
                break;
            }
            case "AS1": {
                goalNode = SearchStrategy.bestFirstSearch(matrix, new MatrixAStar1());
                break;
            }
            case "AS2": {
                goalNode = SearchStrategy.bestFirstSearch(matrix, new MatrixAStar2());
                break;
            }
            default:
                break;
        }
        
        if (goalNode == null)
            return "No Solution";
        
        SearchTreeNode currentNode = goalNode;
        String plan = "";
        LinkedList<String> gridVisualize=new LinkedList<String>();
        while (currentNode.parent != null) {
            String currentOperator = currentNode.operator.toString().toLowerCase();
            MatrixState currentState=new MatrixState(currentNode.state);
            gridVisualize.addFirst(currentState.toString());
            if (currentOperator.equals("takepill")) {
                currentOperator = "takePill";
            }
            plan = currentOperator + plan;
            currentNode = currentNode.parent;
            plan = (currentNode.parent == null? "": ",") + plan;
        }
        MatrixState currentState=new MatrixState(currentNode.state);
        gridVisualize.addFirst(currentState.toString());

        if (visualize) {
           for (String currenString : gridVisualize) {
               System.out.println(currenString);
               System.out.println("_________________________________________________________________________"+"\n");
               
               
           }
        }

        MatrixState goalState = new MatrixState(goalNode.state);
        return plan + ";" + goalState.getDeaths() + ";" + goalState.getKills() + ";" + matrix.expandedNodes;
    }
    
}
