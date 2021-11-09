package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Hostage {

    public Position position;
    public int damage;
    public boolean isAgent;
    public boolean isKilled;
    public boolean isCarried;

    public Hostage(int x, int y, int damage, boolean isAgent, boolean isKilled, boolean isCarried) {
        this.position = new Position(x, y);
        this.damage = damage;
        this.isAgent = false;
        this.isKilled = false;
        this.isCarried = false;
    }

    public static ArrayList<Hostage> createHostages(String hostagesInfo) {
        String[] splitInfo = hostagesInfo.split(",");
        ArrayList<Hostage> hostages = new ArrayList<Hostage>();
        for (int i = 0; i < splitInfo.length; i+=6) {
            int x = Integer.parseInt(splitInfo[i]);
            int y = Integer.parseInt(splitInfo[i+1]);
            int damage = Integer.parseInt(splitInfo[i+2]);
            boolean isAgent = splitInfo[i+3] == "f" ? false : true;
            boolean isKilled = splitInfo[i+4] == "f" ? false : true;
            boolean isCarried = splitInfo[i+5] == "f" ? false : true;
            Hostage newHostage = new Hostage(x, y, damage, isAgent, isKilled, isCarried);
            hostages.add(newHostage);
        }
        return hostages;
    }
    
}
