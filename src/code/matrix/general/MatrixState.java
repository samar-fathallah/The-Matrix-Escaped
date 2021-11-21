package code.matrix.general;

import java.util.ArrayList;
import code.searchproblem.general.*;
import code.matrix.helpers.Position;
import code.matrix.objects.*;

public class MatrixState extends State {
    
    public Object[][] grid;
    public int m;
    public int n;
    public Neo neo;
    public TelephoneBooth telephoneBooth;
    public ArrayList<Agent> agents;
    public ArrayList<Pill> pills;
    public ArrayList<Pad> pads;
    public ArrayList<Hostage> hostages;

    public MatrixState(String stateString) {
        // Decode string of the state into state object
        String[] splitStateString = stateString.split(";");
        String[] gridDimensions = splitStateString[0].split(",");
        this.m = Integer.parseInt(gridDimensions[0]);
        this.n = Integer.parseInt(gridDimensions[1]);
        this.neo = new Neo(splitStateString[2], splitStateString[1]);
        this.telephoneBooth = new TelephoneBooth(splitStateString[3]);
        this.agents = Agent.createAgents(splitStateString[4]);
        this.pills = Pill.createPills(splitStateString[5]);
        this.pads = Pad.createPads(splitStateString[6]);
        this.hostages = Hostage.createHostages(splitStateString[7]);
        this.grid = new Object[m][n];

        this.grid[this.telephoneBooth.position.x][this.telephoneBooth.position.y] = this.telephoneBooth;
        for (Agent agent : this.agents) {
            if (!agent.isKilled) {
                this.grid[agent.position.x][agent.position.y] = agent;
            }
        }
        for (Pill pill : this.pills) {
            if (!pill.isTaken) {
                this.grid[pill.position.x][pill.position.y] = pill;
            }
        }
        for (Pad pad : this.pads) {
            this.grid[pad.start.x][pad.start.y] = pad;
        }
        for (Hostage hostage : this.hostages) {
            if (this.grid[hostage.position.x][hostage.position.y] == null && !hostage.isCarried && !hostage.isKilled) {
                this.grid[hostage.position.x][hostage.position.y] = hostage;
            }

            // Update carriedHostages array in neo
            if (hostage.isCarried) {
                this.neo.carriedHostages.add(hostage);
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        Object[][] grid = this.grid;
        for (int i = 0; i < grid.length; i++) {
            result += "[ ";
            for (int j = 0; j < grid[0].length; j++) {
                boolean neoAdded = false;
                Object object = grid[i][j];
                if (i == this.neo.position.x && j == this.neo.position.y) {
                    neoAdded = true;
                    if (object == null) {
                        result += "     N      ";
                    }
                    else {
                        result += "N,";
                    }
                }
                if (object != null) {
                    if (!neoAdded) {
                        result += " ";
                    }

                    if (object instanceof TelephoneBooth) {
                        result += "    TB    ";
                    }
                    else if (object instanceof Agent) {
                        result += "    Ag    ";
                    }
                    if (object instanceof Pill) {
                        result += "    Pi    ";
                    }
                    else if (object instanceof Pad) {
                        Pad pad = (Pad) object;
                        if (pad.finish.x < 10 && pad.finish.y < 10) {
                            result += " Pa (" + pad.finish.x + "," + pad.finish.y + ") ";
                        }
                        else if (pad.finish.x >= 10 && pad.finish.y >= 10) {
                            result += "Pa (" + pad.finish.x + "," + pad.finish.y + ")";
                        }
                        else {
                            result += " Pa (" + pad.finish.x + "," + pad.finish.y + ")";
                        }
                        
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent) {
                            result += "  H (Ag)  ";
                        }
                        else {
                            if (hostage.damage < 10) {
                                result += "  H  (" + hostage.damage + ")  ";
                            }
                            else {
                                result += "  H (" + hostage.damage + ")  ";
                            }
                        }

                    }
                    
                    if (!neoAdded) {
                        result += " ";
                    }
                }
                else if (!(i == this.neo.position.x && j == this.neo.position.y)) {
                    result += "     --     ";
                }
                result += (j == grid[0].length-1) ? "" : " | ";
            }
            result += " ]" + "\n";
        }
        result += "\n" + "Neo Damage: " + this.neo.damage;
        result += "\n" + "Carried Hostages: ";
        if (this.neo.carriedHostages.size() == 0) {
            result += "No Hostages";
        }
        else {
            for (Hostage hostage : this.neo.carriedHostages) {
                result += "H(" + hostage.damage + ") ";
            }
        }
        return result;
    }

    @Override
    public State clone() {
        return new MatrixState(this.encode());
    }

    @Override
    public String encode() {
        String stateString = "";
        stateString += this.m + "," + this.n + ";" + this.neo.carryCapacity + ";";
        stateString += this.neo.position.x + "," + this.neo.position.y + "," + this.neo.damage + ";";
        stateString += this.telephoneBooth.position.x + "," + this.telephoneBooth.position.y + ";";
        for (int i = 0; i < this.agents.size(); i++) {
            Agent agent = this.agents.get(i);
            stateString += agent.position.x + "," + agent.position.y;
            stateString += "," + (agent.isKilled ? "t" : "f");
            stateString += i == this.agents.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < this.pills.size(); i++) {
            Pill pill = this.pills.get(i);
            stateString += pill.position.x + "," + pill.position.y;
            stateString += "," + (pill.isTaken ? "t" : "f");
            stateString += i == this.pills.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < this.pads.size(); i++) {
            Pad pad = this.pads.get(i);
            stateString += pad.start.x + "," + pad.start.y + ",";
            stateString += pad.finish.x + "," + pad.finish.y;
            stateString += i == this.pads.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < this.hostages.size(); i++) {
            Hostage hostage = this.hostages.get(i);
            stateString += hostage.position.x + "," + hostage.position.y;
            stateString += "," + hostage.damage;
            stateString += "," + (hostage.isAgent ? "t" : "f");
            stateString += "," + (hostage.isKilled ? "t" : "f");
            stateString += "," + (hostage.isCarried ? "t" : "f");
            stateString += i == this.hostages.size()-1 ? "" : ",";
        }
        
        return stateString;
    }

    @Override
    public String hash() {
        String stateString = this.encode();
        String[] splitStateString = stateString.split(";");
        String stateHash = "";
        stateHash += splitStateString[2] + ";";
        stateHash += splitStateString[4] + ";";
        stateHash += splitStateString[5] + ";";
        String[] splitHostagesInfo = splitStateString[7].split(",");
        splitStateString[7] = "";
        for (int i = 0; i < splitHostagesInfo.length; i++) {
            if ((i-2) % 6 != 0) {
                splitStateString[7] += splitHostagesInfo[i];
                splitStateString[7] += i == splitHostagesInfo.length-1? "": ",";
            }
        }
        stateHash += splitStateString[7];
        return stateHash;
    }

    @Override
    public boolean isValidOperator(Operator operator) {
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        boolean isValidOperator = true;
        
        switch (matrixOperator) {
            case UP: {
                Position neoPosition = this.neo.position;
                if (neoPosition.x == 0) {
                    isValidOperator = false;
                }
                else {
                    Object object = this.grid[neoPosition.x - 1][neoPosition.y];
                    if (object instanceof Agent) {
                        isValidOperator = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent || hostage.damage >= 98) {
                            isValidOperator = false;
                        }
                    }
                }
                break;
            }
            case DOWN: {
                Position neoPosition = this.neo.position;
                if (neoPosition.x == this.n - 1) {
                    isValidOperator = false;
                }
                else {
                    Object object = this.grid[neoPosition.x + 1][neoPosition.y];
                    if (object instanceof Agent) {
                        isValidOperator = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent || hostage.damage >= 98) {
                            isValidOperator = false;
                        }
                    }
                }
                break;
            } 
            case LEFT: {
                Position neoPosition = this.neo.position;
                if (neoPosition.y == 0) {
                    isValidOperator = false;
                }
                else {
                    Object object = this.grid[neoPosition.x][neoPosition.y - 1];
                    if (object instanceof Agent) {
                        isValidOperator = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent || hostage.damage >= 98) {
                            isValidOperator = false;
                        }
                    }
                }
                break;
            }
            case RIGHT: {
                Position neoPosition = this.neo.position;
                if (neoPosition.y == this.m - 1) {
                    isValidOperator = false;
                }
                else {
                    Object object = this.grid[neoPosition.x][neoPosition.y + 1];
                    if (object instanceof Agent) {
                        isValidOperator = false;
                    }
                    else if (object instanceof Hostage) {
                        Hostage hostage = (Hostage) object;
                        if (hostage.isAgent || hostage.damage >= 98) {
                            isValidOperator = false;
                        }
                    }
                }
                break;
            }
            case CARRY: {
                Neo neo = this.neo;
                Object object = this.grid[neo.position.x][neo.position.y];
                if (!(object instanceof Hostage)) {
                    isValidOperator = false;
                }
                else if (neo.carriedHostages.size() == neo.carryCapacity) {
                    isValidOperator = false;
                }
                break;
            }
            case DROP: {
                Neo neo = this.neo;
                if (!neo.position.equals(this.telephoneBooth.position) || neo.carriedHostages.size() == 0) {
                    isValidOperator = false;
                }
                break;
            }
            case TAKEPILL: {
                Position neoPosition = this.neo.position;
                Object object = this.grid[neoPosition.x][neoPosition.y];
                if (!(object instanceof Pill)) {
                    isValidOperator = false;
                }
                break;
            }
            case FLY: {
                Position neoPosition = this.neo.position;
                Object object = this.grid[neoPosition.x][neoPosition.y];
                if (!(object instanceof Pad)) {
                    isValidOperator = false;
                }
                break;
            }
            case KILL: {
                if (neo.damage >= 80) {
                    isValidOperator = false;
                    break;
                }
                Position neoPosition = this.neo.position;
                // current cell
                Object object = this.grid[neoPosition.x][neoPosition.y];
                if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.damage >= 98) {
                        isValidOperator = false;
                        break;
                    }
                }
                // above cell
                object = neoPosition.x == 0 ? null : this.grid[neoPosition.x-1][neoPosition.y];
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
                object = neoPosition.x == this.n-1 ? null : this.grid[neoPosition.x+1][neoPosition.y];
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
                object = neoPosition.y == 0 ? null : this.grid[neoPosition.x][neoPosition.y-1];
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
                object = neoPosition.y == this.m-1 ? null : this.grid[neoPosition.x][neoPosition.y+1];
                if (object instanceof Agent) {
                    break;
                }
                else if (object instanceof Hostage) {
                    Hostage hostage = (Hostage) object;
                    if (hostage.isAgent) {
                        break;
                    }
                }
                isValidOperator = false;
                break;
            }
            default:
                break;
        }

        return isValidOperator;
    }

    @Override
    public void updateState(Operator operator) {
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        switch (matrixOperator) {
            case UP: {
                neo.position.x--;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.x--;
                }
                break;
            }
            case DOWN: {
                neo.position.x++;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.x++;
                }
                break;
            }
            case LEFT: {
                neo.position.y--;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.y--;
                }
                break;
            }
            case RIGHT: {
                neo.position.y++;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.y++;
                }
                break;
            }
            case CARRY: {
                Hostage hostage = (Hostage) grid[neo.position.x][neo.position.y];
                hostage.isCarried = true;
                neo.carriedHostages.add(hostage);
                grid[neo.position.x][neo.position.y] = null;
                break;
            }
            case DROP: {
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.isCarried = false;
                }
                neo.carriedHostages.clear();
                break;
            }
            case TAKEPILL: {
                Pill pill = (Pill) grid[neo.position.x][neo.position.y];
                grid[neo.position.x][neo.position.y] = null;
                pill.isTaken = true;
                neo.damage -= 20;
                if (neo.damage < 0) {
                    neo.damage = 0;
                }
                for (Hostage hostage : hostages) {
                    if (!hostage.isAgent && (!hostage.position.equals(telephoneBooth.position) || hostage.isCarried)) {
                        hostage.damage -= 20;
                        if (hostage.damage < 0) {
                            hostage.damage = 0;
                        }
                    }
                }
                break;
            }
            case FLY: {
                Pad pad = (Pad) grid[neo.position.x][neo.position.y];
                neo.position.x = pad.finish.x;
                neo.position.y = pad.finish.y;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.x = pad.finish.x;
                    hostage.position.y = pad.finish.y;
                }
                break;
            }
            case KILL: {
                neo.damage += 20;
                Object above = neo.position.x == 0 ? null : grid[neo.position.x-1][neo.position.y];
                Object below = neo.position.x == n-1 ? null : grid[neo.position.x+1][neo.position.y];
                Object left = neo.position.y == 0 ? null : grid[neo.position.x][neo.position.y-1];
                Object right = neo.position.y == m-1 ? null : grid[neo.position.x][neo.position.y+1];
                // above cell
                if (above instanceof Agent) {
                    Agent agent = (Agent) above;
                    agent.isKilled = true;
                    grid[neo.position.x-1][neo.position.y] = null;
                }
                else if (above instanceof Hostage) {
                    Hostage hostage = (Hostage) above;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                        grid[neo.position.x-1][neo.position.y] = null;
                    }
                }
                // below cell
                if (below instanceof Agent) {
                    Agent agent = (Agent) below;
                    agent.isKilled = true;
                    grid[neo.position.x+1][neo.position.y] = null;
                }
                else if (below instanceof Hostage) {
                    Hostage hostage = (Hostage) below;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                        grid[neo.position.x+1][neo.position.y] = null;
                    }
                }
                // left cell
                if (left instanceof Agent) {
                    Agent agent = (Agent) left;
                    agent.isKilled = true;
                    grid[neo.position.x][neo.position.y-1] = null;
                }
                else if (left instanceof Hostage) {
                    Hostage hostage = (Hostage) left;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                        grid[neo.position.x][neo.position.y-1] = null;
                    }
                }
                // right cell
                if (right instanceof Agent) {
                    Agent agent = (Agent) right;
                    agent.isKilled = true;
                    grid[neo.position.x][neo.position.y+1] = null;
                }
                else if (right instanceof Hostage) {
                    Hostage hostage = (Hostage) right;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                        grid[neo.position.x][neo.position.y+1] = null;
                    }
                }
                break;
            }
            default:
                break;
        }

        if (matrixOperator != MatrixOperator.TAKEPILL) {
            for (Hostage hostage : hostages) {
                if (!hostage.isAgent && (!hostage.position.equals(telephoneBooth.position) || hostage.isCarried)) {
                    hostage.damage += 2;
                    if (hostage.damage >= 100) {
                        hostage.damage = 100;
                        if (!hostage.isCarried) {
                            hostage.isAgent = true;
                        }
                    }
                }
            }
        }
    }
    
    public int getDeaths() {
        int deaths = 0;
        for (Hostage hostage : hostages) {
            if (hostage.damage == 100) {
                deaths++;
            }
        }
        return deaths;
    }
    
    public int getAgentKills() {
        int kills = 0;
        for (Agent agent : agents) {
            if (agent.isKilled) {
                kills++;
            }
        }
        return kills;
    }

    public int getHostageKills() {
        int kills = 0;
        for (Hostage hostage : hostages) {
            if (hostage.isKilled) {
                kills++;
            }
        }
        return kills;
    }

    public int getKills() {
        int kills = 0;
        kills += this.getAgentKills();
        kills += this.getHostageKills();
        return kills;
    }
    
}
