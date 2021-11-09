package code.matrix.general;

import java.util.ArrayList;
import code.generalSearchProblem.State;
import code.matrix.objects.Agent;
import code.matrix.objects.Hostage;
import code.matrix.objects.Neo;
import code.matrix.objects.Pad;
import code.matrix.objects.Pill;
import code.matrix.objects.TelephoneBooth;

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

    public MatrixState(String state) {
        // Decode string of the state into state objct
        String[] splitState = state.split(";");
        String[] gridDimensions = splitState[0].split(",");
        this.m = Integer.parseInt(gridDimensions[0]);
        this.n = Integer.parseInt(gridDimensions[1]);
        this.neo = new Neo(splitState[2], splitState[1]);
        this.telephoneBooth = new TelephoneBooth(splitState[3]);
        this.agents = Agent.createAgents(splitState[4]);
        this.pills = Pill.createPills(splitState[5]);
        this.pads = Pad.createPads(splitState[6]);
        this.hostages = Hostage.createHostages(splitState[7]);

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
            stateString += i == this.agents.size()-1 ? ";" : ",";
        }
        for (int i = 0; i < this.pills.size(); i++) {
            Pill pill = this.pills.get(i);
            stateString += pill.position.x + "," + pill.position.y;
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
            stateString += hostage.position.x + "," + hostage.position.y + ",";
            stateString += hostage.damage;
            stateString += i == this.hostages.size()-1 ? "" : ",";
        }
        
        return stateString;
    }

    public void updateState(MatrixOperator operator) {
        for (Hostage hostage : hostages) {
            if (!hostage.position.equals(telephoneBooth.position) || hostage.isCarried) {
                hostage.damage += 2;
                if (hostage.damage >= 100 && operator != MatrixOperator.TAKEPILL) {
                    hostage.damage = 100;
                    if (!hostage.isCarried) {
                        hostage.isAgent = true;
                    }
                }
            }
        }
        switch (operator) {
            case UP: {
                neo.position.y--;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.y--;
                }
                break;
            }
            case DOWN: {
                neo.position.y++;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.y++;
                }
                break;
            }
            case LEFT: {
                neo.position.x--;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.x--;
                }
                break;
            }
            case RIGHT: {
                neo.position.x++;
                for (Hostage hostage : neo.carriedHostages) {
                    hostage.position.x++;
                }
                break;
            }
            case CARRY: {
                Hostage hostage = (Hostage) grid[neo.position.x][neo.position.y];
                hostage.isCarried = true;
                neo.carriedHostages.add(hostage);
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
                Object above = neo.position.y == 0 ? null : grid[neo.position.x][neo.position.y-1];
                Object below = neo.position.y == n-1 ? null : grid[neo.position.x][neo.position.y+1];
                Object left = neo.position.x == 0 ? null : grid[neo.position.x-1][neo.position.y];
                Object right = neo.position.x == m-1 ? null : grid[neo.position.x+1][neo.position.y];
                // above cell
                if (above instanceof Agent) {
                    Agent agent = (Agent) above;
                    agent.isKilled = true;
                }
                else if (above instanceof Hostage) {
                    Hostage hostage = (Hostage) above;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                    }
                }
                // below cell
                if (below instanceof Agent) {
                    Agent agent = (Agent) below;
                    agent.isKilled = true;
                }
                else if (below instanceof Hostage) {
                    Hostage hostage = (Hostage) below;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                    }
                }
                // left cell
                if (left instanceof Agent) {
                    Agent agent = (Agent) left;
                    agent.isKilled = true;
                }
                else if (left instanceof Hostage) {
                    Hostage hostage = (Hostage) left;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                    }
                }
                // right cell
                if (right instanceof Agent) {
                    Agent agent = (Agent) right;
                    agent.isKilled = true;
                }
                else if (right instanceof Hostage) {
                    Hostage hostage = (Hostage) right;
                    if (hostage.isAgent) {
                        hostage.isKilled = true;
                    }
                }
                break;
            }
            default:
                break;
        }
    }

}
