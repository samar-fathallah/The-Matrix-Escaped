package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Pill {
    
    public Position position;
    public boolean isTaken;

    public Pill(int x, int y, boolean isTaken) {
        this.position = new Position(x, y);
        this.isTaken = isTaken;
    }

    public static ArrayList<Pill> createPills(String pillsInfo) {
        String[] splitInfo = pillsInfo.split(",");
        ArrayList<Pill> pills = new ArrayList<Pill>();
        for (int i = 0; i < splitInfo.length; i+=3) { 
            int x = Integer.parseInt(splitInfo[i]);
            int y = Integer.parseInt(splitInfo[i+1]);
            boolean isTaken = splitInfo[i+2] == "f" ? false : true;
            Pill newPill = new Pill(x, y, isTaken);
            pills.add(newPill);
        }
        return pills;
    }

}
