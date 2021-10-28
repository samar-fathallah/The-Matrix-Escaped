package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Pill {
    
    public Position position;

    public Pill(int x, int y) {
        this.position = new Position(x, y);
    }

    public static ArrayList<Pill> createPills(String pillsInfo) {
        String[] splitInfo = pillsInfo.split(",");
        ArrayList<Pill> pills = new ArrayList<Pill>();
        for (int i = 0; i < splitInfo.length; i+=2) { 
            Pill newPill = new Pill(Integer.parseInt(splitInfo[i]), Integer.parseInt(splitInfo[i+1]));
            pills.add(newPill);
        }
        return pills;
    }

}
