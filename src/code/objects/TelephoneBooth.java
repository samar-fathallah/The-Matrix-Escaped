package code.objects;

import code.helpers.Position;

public class TelephoneBooth {
 
    public Position position;

    public TelephoneBooth(String position) {
        String[] splitPositions = position.split(",");
        this.position = new Position(Integer.parseInt(splitPositions[0]), Integer.parseInt(splitPositions[1]));
    }

}
