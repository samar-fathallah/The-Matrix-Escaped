package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Neo {
    
    public Position position;
    public int damage;
    public int carryCapacity;
    public ArrayList<Hostage> carriedHostages;

    public Neo(String neoInfo, String carryCapacity) {
        String[] splitInfo = neoInfo.split(",");
        this.position = new Position(Integer.parseInt(splitInfo[0]), Integer.parseInt(splitInfo[1]));
        this.damage = Integer.parseInt(splitInfo[2]);
        this.carryCapacity = Integer.parseInt(carryCapacity);
        this.carriedHostages = new ArrayList<Hostage>(this.carryCapacity);
    }
    
}
