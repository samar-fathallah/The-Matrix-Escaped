package code.matrix.general;

import java.util.ArrayList;
import code.generalsearchproblem.*;
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
    public State clone() {
        return new MatrixState(this.encode());
    }

    @Override
    public void updateState(Operator operator) {
        MatrixOperator matrixOperator = (MatrixOperator) operator;
        
        for (Hostage hostage : hostages) {
            if (!hostage.position.equals(telephoneBooth.position) || hostage.isCarried) {
                hostage.damage += 2;
                if (hostage.damage >= 100 && matrixOperator != MatrixOperator.TAKEPILL) {
                    hostage.damage = 100;
                    if (!hostage.isCarried) {
                        hostage.isAgent = true;
                    }
                }
            }
        }

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
                for (Hostage hostage : hostages) {
                    if (!hostage.isAgent && !(hostage.position.equals(telephoneBooth.position) && !hostage.isCarried)) {
                        hostage.damage -= 22;
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
    }
    
}
