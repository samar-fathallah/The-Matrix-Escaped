package code.matrix.general;

import java.util.ArrayList;
import code.generalSearchProblem.general.State;
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

    public MatrixState(String grid) {
        String[] splitGrid = grid.split(";");
        String[] gridDimensions = splitGrid[0].split(",");
        this.m = Integer.parseInt(gridDimensions[0]);
        this.n = Integer.parseInt(gridDimensions[1]);
        this.neo = new Neo(splitGrid[2], splitGrid[1]);
        this.telephoneBooth = new TelephoneBooth(splitGrid[3]);
        this.agents = Agent.createAgents(splitGrid[4]);
        this.pills = Pill.createPills(splitGrid[5]);
        this.pads = Pad.createPads(splitGrid[6]);
        this.hostages = Hostage.createHostages(splitGrid[7]);

        this.grid = new Object[m][n];
        this.grid[this.telephoneBooth.position.x][this.telephoneBooth.position.y] = this.telephoneBooth;
        for (Agent agent : this.agents) {
            this.grid[agent.position.x][agent.position.y] = agent;
        }
        for (Pill pill : this.pills) {
            this.grid[pill.position.x][pill.position.y] = pill;
        }
        for (Pad pad : this.pads) {
            this.grid[pad.start.x][pad.start.y] = pad;
        }
        for (Hostage hostage : this.hostages) {
            this.grid[hostage.position.x][hostage.position.y] = hostage;
        }
    }

    @Override
    public State cloneState() {
        // TODO Auto-generated method stub
        return null;
    }

}
