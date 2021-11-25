package code.matrix.helpers;

import java.util.Random;

public class HelperMethods {
    
    public static int genrateRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
