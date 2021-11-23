package code.matrix.evaluationfunctions;

import code.matrix.general.MatrixState;
import code.matrix.objects.Hostage;
import code.searchproblem.general.SearchTreeNode;

public class MatrixHeuristics {

    public static int h1(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);
        Hostage nearestHostage = null;
        int minDistance = Integer.MAX_VALUE;
        for (Hostage hostage: state.hostages) {
            if ((!hostage.isAgent && !hostage.position.equals(state.telephoneBooth.position) && !hostage.isCarried) || (hostage.isAgent && !hostage.isKilled)) {
                int distance = hostage.position.getManhattanDistance(state.neo.position);
                if (distance < minDistance) {
                    nearestHostage = hostage;
                    minDistance = distance;
                }
            }
        }
        if (nearestHostage == null) {
            if (state.neo.position.equals(state.telephoneBooth.position)) {
                return state.neo.carriedHostages.size() == 0 ? 0 : 1;
            }
            return state.telephoneBooth.position.getManhattanDistance(state.neo.position);
        }
        int boothDistance = state.telephoneBooth.position.getManhattanDistance(nearestHostage.position);
        return 1 + minDistance + boothDistance;
    }

    public static int h2(SearchTreeNode node) {
        return 1;
    }
    
}
