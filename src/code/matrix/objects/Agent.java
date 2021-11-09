package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Agent {
    
    public Position position;
    public boolean isKilled;

    public Agent(int x, int y) {
        this.position = new Position(x, y);
        this.isKilled = false;
    }

    public static ArrayList<Agent> createAgents(String agentsInfo) {
        String[] splitInfo = agentsInfo.split(",");
        ArrayList<Agent> agents = new ArrayList<Agent>();
        for (int i = 0; i < splitInfo.length; i+=2) { 
            Agent newAgent = new Agent(Integer.parseInt(splitInfo[i]), Integer.parseInt(splitInfo[i+1]));
            agents.add(newAgent);
        }
        return agents;
    }

}
