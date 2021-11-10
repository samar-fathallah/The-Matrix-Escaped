package code.matrix.objects;

import code.matrix.helpers.Position;

public class TelephoneBooth {
 
    public Position position;

    public TelephoneBooth(String position) {
        String[] splitPositions = position.split(",");
        int x = Integer.parseInt(splitPositions[0]);
        int y = Integer.parseInt(splitPositions[1]);
        this.position = new Position(x, y);
    }

}
