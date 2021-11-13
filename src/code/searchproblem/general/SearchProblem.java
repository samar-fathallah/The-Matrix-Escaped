package code.searchproblem.general;

import java.util.ArrayList;
import code.searchproblem.strategies.SearchStrategy;

public abstract class SearchProblem {

    public ArrayList<Operator> operators;
    public State initialState;
    
    // getNextState function is used to generate the state space
    public abstract State getNextState(State state, Operator operator);
    
    public abstract boolean goalTest(State state);
    
    public abstract int pathCost(SearchTreeNode node);

    public static SearchTreeNode generalSearch(SearchProblem problem, SearchStrategy strategy) {
        return null;
    }

}
