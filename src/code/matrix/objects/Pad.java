package code.matrix.objects;

import java.util.ArrayList;

import code.matrix.helpers.Position;

public class Pad {

    public Position start;
    public Position finish;

    public Pad (int x1, int y1, int x2, int y2) {
        this.start = new Position(x1, y1);
        this.finish = new Position(x2, y2);
    }

    public static ArrayList<Pad> createPads(String padsInfo) {
        String[] splitInfo = padsInfo.split(",");
        ArrayList<Pad> pads = new ArrayList<Pad>();
        for (int i = 0; i < splitInfo.length; i+=4) { 
            Pad newPad = new Pad(Integer.parseInt(splitInfo[i]), Integer.parseInt(splitInfo[i+1]), Integer.parseInt(splitInfo[i+2]), Integer.parseInt(splitInfo[i+3]));
            pads.add(newPad);
        }
        return pads;
    }
    
}
