package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Agent {
    
    public Position position;
    public boolean isKilled;

    public Agent(int x, int y, boolean isKilled) {
        this.position = new Position(x, y);
        this.isKilled = isKilled;
    }

    public static ArrayList<Agent> createAgents(String agentsInfo) {
        String[] splitInfo = agentsInfo.split(",");
        ArrayList<Agent> agents = new ArrayList<Agent>();
        for (int i = 0; i < splitInfo.length; i+=3) { 
            int x = Integer.parseInt(splitInfo[i]);
            int y = Integer.parseInt(splitInfo[i+1]);
            boolean isKilled = splitInfo[i+2] == "f" ? false : true;
            Agent newAgent = new Agent(x, y, isKilled);
            agents.add(newAgent);
        }
        return agents;
    }

}
