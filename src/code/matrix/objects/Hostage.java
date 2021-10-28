package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Hostage {

    public Position position;
    public int damage;
    public boolean isAgent;

    public Hostage(int x, int y, int damage) {
        this.position = new Position(x, y);
        this.damage = damage;
        this.isAgent = false;
    }

    public static ArrayList<Hostage> createHostages(String hostagesInfo) {
        String[] splitInfo = hostagesInfo.split(",");
        ArrayList<Hostage> hostages = new ArrayList<Hostage>();
        for (int i = 0; i < splitInfo.length; i+=3) { 
            Hostage newHostage = new Hostage(Integer.parseInt(splitInfo[i]), Integer.parseInt(splitInfo[i+1]), Integer.parseInt(splitInfo[i+2]));
            hostages.add(newHostage);
        }
        return hostages;
    }
    
}
