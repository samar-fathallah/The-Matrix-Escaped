package code.matrix.general;

import java.util.ArrayList;

import code.generalsearchproblem.*;
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
    
    public void visualizeMatrix() {
        Object[][] grid = ((MatrixState) this.initialState).grid;
        for (int i = 0; i < grid.length; i++) {
            System.out.print("[  ");
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print((grid[i][j] != null ? grid[i][j].toString().split("@")[0].substring(20, 22) : "NA") + "  ");
            }
            System.out.println("]");
        }
    }
    
    @Override
    public State getNextState(State state, Operator operator) {
        MatrixState matrixState = (MatrixState) state;
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        if (matrixState.neo.damage >= 100) {
            return null;
        }

        boolean isValid = true;
        switch (matrixOperator) {
            case UP: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.x == 0) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x - 1][neoPosition.y];
                    if (object instanceof Agent) {
                        isValid = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent) {
                            isValid = false;
                        }
                    }
                }
                break;
            }
            case DOWN: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.x == matrixState.n - 1) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x + 1][neoPosition.y];
                    if (object instanceof Agent) {
                        isValid = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent) {
                            isValid = false;
                        }
                    }
                }
                break;
            } 
            case LEFT: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.y == 0) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x][neoPosition.y - 1];
                    if (object instanceof Agent) {
                        isValid = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent) {
                            isValid = false;
                        }
                    }
                }
                break;
            }
            case RIGHT: {
                Position neoPosition = matrixState.neo.position;
                if (neoPosition.y == matrixState.m - 1) {
                    isValid = false;
                }
                else {
                    Object object = matrixState.grid[neoPosition.x][neoPosition.y + 1];
                    if (object instanceof Agent) {
                        isValid = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent) {
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
                else if (neo.carriedHostages.size() == neo.carryCapacity) {
                    isValid = false;
                }
                break;
            }
            case DROP: {
                Neo neo = matrixState.neo;
                if (!neo.position.equals(matrixState.telephoneBooth.position) || neo.carriedHostages.size() == 0) {
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
                Object object = neoPosition.x == 0 ? null : matrixState.grid[neoPosition.x-1][neoPosition.y];
                if (object instanceof Agent) {
                    break;
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent) {
                        break;
                    }
                }
                // below cell
                object = neoPosition.x == matrixState.n-1 ? null : matrixState.grid[neoPosition.x+1][neoPosition.y];
                if (object instanceof Agent) {
                    break;
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent) {
                        break;
                    }
                }
                // left cell
                object = neoPosition.y == 0 ? null : matrixState.grid[neoPosition.x][neoPosition.y-1];
                if (object instanceof Agent) {
                    break;
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent) {
                        break;
                    }
                }
                // right cell
                object = neoPosition.y == matrixState.m-1 ? null : matrixState.grid[neoPosition.x][neoPosition.y+1];
                if (object instanceof Agent) {
                    break;
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent) {
                        break;
                    }
                }
                isValid = false;
                break;
            }
            default:
                break;
        }

        if (!isValid) {
            return null;
        }
        
        matrixState.updateState(matrixOperator);
        return matrixState;
    }

    @Override
    public boolean goalTest(State state) {
        MatrixState matrixState = (MatrixState) state;
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
