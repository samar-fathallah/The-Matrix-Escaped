package code.matrix;

import java.util.ArrayList;
import code.generalSearchProblem.*;
import code.matrix.general.*;
import code.matrix.helpers.*;
import code.matrix.objects.*;
import code.searchTree.*;


public class Matrix extends GeneralSearchProblem {

    public Matrix(String initialGrid) {
        String initialState = initialGrid;
        // Initialize neo damage to 0
        String[] splitState = initialState.split(";");
        splitState[2] = splitState[2] + ",0";
        initialState = String.join(";", splitState);
        this.initialState = new MatrixState(initialState);
    }
    
    public void visualizeMatrix() {
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

    public static String solve(String initialGrid, String startegy, boolean visualize) {
        Matrix matrix = new Matrix(initialGrid);
        if (visualize) {
            matrix.visualizeMatrix();
        }
        return "";
    }

    @Override
    public String getNextState(State state, Operator operator) {
        MatrixState matrixState = (MatrixState) state;
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        if (matrixState.neo.damage >= 100) {
            return "";
        }

        String nextState = "";
        boolean isValid = true;
        switch (matrixOperator) {
            case UP: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.y == 0) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x][neoPosition.y - 1];
                    if (object instanceof Agent) {
                        Agent agent = (Agent) object;
                        if (!agent.isKilled) {
                            isValid = false;
                        }
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent && !hostage.isKilled) {
                            isValid = false;
                        }
                    }
                }
                break;
            }
            case DOWN: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.y == matrixState.n - 1) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x][neoPosition.y + 1];
                    if (object instanceof Agent) {
                        Agent agent = (Agent) object;
                        if (!agent.isKilled) {
                            isValid = false;
                        }
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent && !hostage.isKilled) {
                            isValid = false;
                        }
                    }
                }
                break;
            } 
            case LEFT: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.x == 0) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x - 1][neoPosition.y];
                    if (object instanceof Agent) {
                        Agent agent = (Agent) object;
                        if (!agent.isKilled) {
                            isValid = false;
                        }
                        else if (object instanceof Hostage) {
                            Hostage hostage = (Hostage) object;
                            if (hostage.isAgent && !hostage.isKilled) {
                                isValid = false;
                            }
                        }
                    }
                }
                break;
            }
            case RIGHT: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.x == matrixState.m - 1) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x + 1][neoPosition.y];
                    if (object instanceof Agent) {
                        Agent agent = (Agent) object;
                        if (!agent.isKilled) {
                            isValid = false;
                        }
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent && !hostage.isKilled) {
                            isValid = false;
                        }
                    }
                }
                break;
            }
            case CARRY: {
                Neo neo = matrixState.neo;
                Object object = matrixState.grid[neo.position.x][neo.position.y];
                if (!(object instanceof Hostage)) {
                    isValid = false;
                }
                else {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isCarried || hostage.isAgent || neo.carriedHostages.size() == neo.carryCapacity) {
                        isValid = false;
                    }
                }
                break;
            }
            case DROP: {
                Neo neo = matrixState.neo;
                if (!neo.position.equals(matrixState.telephoneBooth.position)) {
                    isValid = false;
                }
                if (neo.carriedHostages.size() == 0) {
                    isValid = false;
                }
                break;
            }
            case TAKEPILL: {
                Position neoPosition = matrixState.neo.position;
                Object object = matrixState.grid[neoPosition.x][neoPosition.y];
                if (!(object instanceof Pill)) {
                    isValid = false;
                }
                else {
                    Pill pill = (Pill) object;
                    if (pill.isTaken) {
                        isValid = false;
                    }
                }
                break;
            }
            case FLY: {
                Position neoPosition = matrixState.neo.position;
                Object object = matrixState.grid[neoPosition.x][neoPosition.y];
                if (!(object instanceof Pad)) {
                    isValid = false;
                }
                break;
            }
            case KILL: {
                Position neoPosition = matrixState.neo.position;
                // above cell
                Object object = neoPosition.y == 0 ? null : matrixState.grid[neoPosition.x][neoPosition.y-1];
                if (object instanceof Agent) {
                    Agent agent = (Agent) object;
                    if (!agent.isKilled) {
                        break;
                    }
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent && !hostage.isKilled) {
                        break;
                    }
                }
                // below cell
                object = neoPosition.y == matrixState.n-1 ? null : matrixState.grid[neoPosition.x][neoPosition.y+1];
                if (object instanceof Agent) {
                    Agent agent = (Agent) object;
                    if (!agent.isKilled) {
                        break;
                    }
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent && !hostage.isKilled) {
                        break;
                    }
                }
                // left cell
                object = neoPosition.y == 0 ? null : matrixState.grid[neoPosition.x-1][neoPosition.y];
                if (object instanceof Agent) {
                    Agent agent = (Agent) object;
                    if (!agent.isKilled) {
                        break;
                    }
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent && !hostage.isKilled) {
                        break;
                    }
                }
                // right cell
                object = neoPosition.y == matrixState.m-1 ? null : matrixState.grid[neoPosition.x+1][neoPosition.y];
                if (object instanceof Agent) {
                    Agent agent = (Agent) object;
                    if (!agent.isKilled) {
                        break;
                    }
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent && !hostage.isKilled) {
                        break;
                    }
                }
                isValid = false;
                break;
            }
            default:
                break;
        }

        if (isValid) {
            matrixState.updateState(matrixOperator);
            nextState = matrixState.encode();
        }
        return nextState;
    }

    @Override
    public boolean goalTest(State currentState) {
        MatrixState matrixState = (MatrixState) currentState;
        boolean neoAtBooth = matrixState.neo.position.equals(matrixState.telephoneBooth.position);
        boolean hostagesDisappeared = true;
        for (Hostage hostage : matrixState.hostages) {
            if (!hostage.position.equals(matrixState.telephoneBooth.position)) {
                if (!hostage.isKilled)
                    hostagesDisappeared = false;
                    break;
            }
            else if (hostage.isCarried) {
                hostagesDisappeared = false;
                break;
            }
        }
        return neoAtBooth && hostagesDisappeared;
    }
    
    @Override
    public int pathCost(SearchTreeNode node) {
        // TODO Auto-generated method stub
        return 0;
    }

}
