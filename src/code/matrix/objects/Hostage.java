package code.matrix.objects;

import java.util.ArrayList;
import code.matrix.helpers.Position;

public class Hostage {

    public Position position;
    public int damage;
    public boolean isAgent;
    public boolean isKilled;
    public boolean isCarried;

    public Hostage(int x, int y, int damage, boolean isAgent, boolean isKilled, boolean isCarried) {
        this.position = new Position(x, y);
        this.damage = damage;
        this.isAgent = isAgent;
        this.isKilled = isKilled;
        this.isCarried = isCarried;
    }

    // returns true if the hostage was dropped at the telephone booth
    public boolean isDropped(TelephoneBooth telephoneBooth) {
        return this.position.equals(telephoneBooth.position) && !this.isCarried;
    }

    // returns true if the hostage is not in the matrix anymore (killed or dropped)
    public boolean isDisappeared(TelephoneBooth telephoneBooth) {
        return (!this.isAgent && this.isDropped(telephoneBooth)) || (this.isAgent && this.isKilled);
    }

    // returns true if the hostage is not yet handled by neo (not carried, dropped, or killed)
    public boolean isRemaining(TelephoneBooth telephoneBooth) {
        return (!this.isAgent && !this.isCarried && !this.position.equals(telephoneBooth.position)) || (this.isAgent && !this.isKilled);
    }

    public static ArrayList<Hostage> createHostages(String hostagesInfo) {
        String[] splitInfo = hostagesInfo.split(",");
        ArrayList<Hostage> hostages = new ArrayList<Hostage>();
        for (int i = 0; i < splitInfo.length; i+=6) {
            int x = Integer.parseInt(splitInfo[i]);
            int y = Integer.parseInt(splitInfo[i+1]);
            int damage = Integer.parseInt(splitInfo[i+2]);
            boolean isAgent = splitInfo[i+3].equals("f") ? false : true;
            boolean isKilled = splitInfo[i+4].equals("f") ? false : true;
            boolean isCarried = splitInfo[i+5].equals("f") ? false : true;
            Hostage newHostage = new Hostage(x, y, damage, isAgent, isKilled, isCarried);
            hostages.add(newHostage);
        }
        return hostages;
    }

}
