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
        int x = Integer.parseInt(splitInfo[0]);
        int y = Integer.parseInt(splitInfo[1]);
        this.damage = Integer.parseInt(splitInfo[2]);
        this.position = new Position(x, y);
        this.carryCapacity = Integer.parseInt(carryCapacity);
        this.carriedHostages = new ArrayList<Hostage>(this.carryCapacity);
    }
    
}
