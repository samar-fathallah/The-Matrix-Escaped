package code.matrix.helpers;

import java.util.ArrayList;

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
    
    public int getManhattanDistance(Position position) {
        return Math.abs(this.x - position.x) + Math.abs(this.y - position.y);
    }

    public static Position genrateRandomPosition(int m, int n, ArrayList<Position> takenPositions) {
        Position newPosition;
        do {
            newPosition = new Position(HelperMethods.genrateRandomInt(0, m), HelperMethods.genrateRandomInt(0, n));    
        } while (takenPositions.contains(newPosition));
        return newPosition;
    }

}
