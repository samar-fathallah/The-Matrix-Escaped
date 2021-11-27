package code.matrix.evaluationfunctions;

import code.matrix.general.MatrixState;
import code.matrix.objects.Hostage;
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
            if (hostage.isRemaining(state.telephoneBooth)) {
                int hostageDistance = state.neo.position.getMinimumDistance(hostage.position, state.pads);
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
            return state.neo.position.getMinimumDistance(state.telephoneBooth.position, state.pads);
        }

        int hostageDistance = state.neo.position.getMinimumDistance(furthestHostage.position, state.pads);
        int boothDistance = furthestHostage.position.getMinimumDistance(state.telephoneBooth.position, state.pads);
        return hostageDistance + boothDistance - 1;
    }

    public static int admissableHeuristic2(SearchTreeNode node) {
        MatrixState state = new MatrixState(node.state);

        int remainingHostages = 0;
        for (Hostage hostage : state.hostages) {
            if (hostage.isRemaining(state.telephoneBooth)) {
                remainingHostages++;
            }
        }

        return remainingHostages;
    }

}
