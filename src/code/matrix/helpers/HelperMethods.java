package code.matrix.helpers;

import java.util.ArrayList;
import java.util.Random;

public class HelperMethods {
    
    public static int genrateRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static Position genrateRandomPosition(int m, int n, ArrayList<Position> takenPositions) {
        Position newPosition;
        do {
            newPosition = new Position(genrateRandomInt(0, m), genrateRandomInt(0, n));    
        } while (takenPositions.contains(newPosition));
        return newPosition;
    }

}
