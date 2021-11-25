package code.matrix.evaluationfunctions;

import java.util.ArrayList;
import code.matrix.general.MatrixState;
import code.matrix.helpers.Position;
import code.matrix.objects.Hostage;
import code.matrix.objects.Pad;
import code.matrix.objects.TelephoneBooth;
import code.searchproblem.general.SearchTreeNode;

public class MatrixHeuristics {

    public static int heuristic1(SearchTreeNode node) {
        return admissableHeuristic1(node);
    }

    public static int heuristic2(SearchTreeNode node) {
        return admissableHeuristic2(node);
    }

    public static int admissableHeuristic1(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);

        Hostage furthestHostage = null;
        int furthestHostageDistance = Integer.MIN_VALUE;
        for (Hostage hostage : state.hostages) {
            if (isRemainingHostage(hostage, state.telephoneBooth)) {
                int hostageDistance = getMinimumDistance(state.neo.position, hostage.position, state.pads);
                if (hostageDistance > furthestHostageDistance) {
                    furthestHostage = hostage;
                    furthestHostageDistance = hostageDistance;
                }
            }
        }
       
        if (furthestHostage == null) {
            if (state.neo.position.equals(state.telephoneBooth.position)) {
                return state.neo.carriedHostages.size() == 0 ? 0 : 1;
            }
            return getMinimumDistance(state.neo.position, state.telephoneBooth.position, state.pads);
        }

        int hostageDistance = getMinimumDistance(state.neo.position, furthestHostage.position, state.pads);
        int boothDistance = getMinimumDistance(furthestHostage.position, state.telephoneBooth.position, state.pads);
        return hostageDistance + boothDistance - 1;
    }

    public static int admissableHeuristic2(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);

        int remainingHostages = 0;
        for (Hostage hostage : state.hostages) {
            if (isRemainingHostage(hostage, state.telephoneBooth)) {
                remainingHostages++;
            }
        }
        return remainingHostages;
    }

    public static boolean isRemainingHostage(Hostage hostage, TelephoneBooth telephoneBooth) {
        return (!hostage.isAgent && !hostage.position.equals(telephoneBooth.position) && !hostage.isCarried) || (hostage.isAgent && !hostage.isKilled);
    }

    public static int getMinimumDistance(Position start, Position end, ArrayList<Pad> pads) {
        int minDistanceWithoutPad = start.getManhattanDistance(end);
        int minDistanceWithPad = Integer.MAX_VALUE;
        for (Pad pad: pads) {
            int distanceWithPad = start.getManhattanDistance(pad.start) + end.getManhattanDistance(pad.finish) + 1;
            if (distanceWithPad < minDistanceWithPad) {
                minDistanceWithPad = distanceWithPad;
            }
        }
        return Integer.min(minDistanceWithPad, minDistanceWithoutPad);
    }
    
}
