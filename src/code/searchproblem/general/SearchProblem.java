package code.searchproblem.general;

import java.util.ArrayList;

public abstract class SearchProblem {

    public String stateClassName;
    public ArrayList<Operator> operators;
    public State initialState;
    
    // getNextState function is used to generate the state space
    public abstract State getNextState(State state, Operator operator);
    
    public abstract boolean goalTest(State state);
    
    public abstract int pathCost(SearchTreeNode node);

}
