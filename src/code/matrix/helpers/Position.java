package code.matrix.helpers;

import java.util.ArrayList;
import code.matrix.objects.Pad;

public class Position {

    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Position p = (Position) obj;
        return this.x == p.x && this.y == p.y;
    }
    
    public int getManhattanDistance(Position endPosition) {
        return Math.abs(this.x - endPosition.x) + Math.abs(this.y - endPosition.y);
    }

    public int getMinimumDistance(Position endPosition, ArrayList<Pad> pads) {
        int minDistanceWithoutPad = this.getManhattanDistance(endPosition);
        int minDistanceWithPad = Integer.MAX_VALUE;
        for (Pad pad: pads) {
            int distanceWithPad = this.getManhattanDistance(pad.start) + endPosition.getManhattanDistance(pad.finish) + 1;
            if (distanceWithPad < minDistanceWithPad) {
                minDistanceWithPad = distanceWithPad;
            }
        }
        return Integer.min(minDistanceWithPad, minDistanceWithoutPad);
    }

    public static Position genrateRandomPosition(int m, int n, ArrayList<Position> takenPositions) {
        Position newPosition;
        do {
            newPosition = new Position(HelperMethods.genrateRandomInt(0, m), HelperMethods.genrateRandomInt(0, n));    
        } while (takenPositions.contains(newPosition));
        return newPosition;
    }

}
