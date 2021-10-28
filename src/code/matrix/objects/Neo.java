package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Neo {
    
    public Position position;
    public int damage;
    public int c;
    public ArrayList<Hostage> carriedHostages;
    public int numberOfAllKills;
    public int numberOfHostageKills;
    public int numberOfDeaths;



    public Neo(String position, String c) {
        String[] splitPositions = position.split(",");
        this.position = new Position(Integer.parseInt(splitPositions[0]), Integer.parseInt(splitPositions[1]));
        this.damage = 0;
        this.c = Integer.parseInt(c);
        this.carriedHostages = new ArrayList<Hostage>(this.c);
    }

}
