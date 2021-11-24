package code.matrix.evaluationfunctions;

import java.util.ArrayList;

import code.matrix.general.MatrixState;
import code.matrix.helpers.Position;
import code.matrix.objects.Hostage;
import code.matrix.objects.Pad;
import code.searchproblem.general.SearchTreeNode;

public class MatrixHeuristics {

    public static int h1(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);
        Hostage nearestHostage = null;
        int nearestHostageDistance = Integer.MAX_VALUE;
        for (Hostage hostage: state.hostages) {
            if ((!hostage.isAgent && !hostage.position.equals(state.telephoneBooth.position) && !hostage.isCarried) || (hostage.isAgent && !hostage.isKilled)) {
                int distance = getMinimumDistance(state.neo.position, hostage.position, state.pads);
                if (distance < nearestHostageDistance) {
                    nearestHostage = hostage;
                    nearestHostageDistance = distance;
                }
            }
        }
       
        if (nearestHostage == null) {
            if (state.neo.position.equals(state.telephoneBooth.position)) {
                return state.neo.carriedHostages.size() == 0 ? 0 : 1;
            }
            return getMinimumDistance(state.neo.position,state.telephoneBooth.position, state.pads);
        }
        
        int hostageDistance=getMinimumDistance(state.neo.position,nearestHostage.position, state.pads);
        int boothDistance=getMinimumDistance(nearestHostage.position,state.telephoneBooth.position, state.pads);
        return hostageDistance + boothDistance;
    }

    public static int h2(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);
        int remainingHostages = 0;
        for (Hostage hostage: state.hostages) {
            if ((!hostage.isAgent && !hostage.position.equals(state.telephoneBooth.position) && !hostage.isCarried) || (hostage.isAgent && !hostage.isKilled)) {
                remainingHostages++;
            }
        }
        return remainingHostages;
    }

    public static int getMinimumDistance(Position start,Position end, ArrayList<Pad> pads){
        int minDistanceWithoutPad=start.getManhattanDistance(end);
        int minDistanceWithPad=Integer.MAX_VALUE;
        for(Pad pad:pads){
            int DistanceWithPad=start.getManhattanDistance(pad.start)+end.getManhattanDistance(pad.finish)+1;
            if(DistanceWithPad<minDistanceWithPad){
                minDistanceWithPad=DistanceWithPad;
            }
        }
        return Integer.min(minDistanceWithPad, minDistanceWithoutPad);
    }
    
}
