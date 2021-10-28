package code.generalSearchProblem;

import java.util.ArrayList;
import java.util.Set;
import code.generalSearchProblem.general.Operator;
import code.generalSearchProblem.general.State;

public abstract class GeneralSearchProblem {

    public ArrayList<Operator> operators;
    public State initialState;
    public Set<State> stateSpace;

    public abstract boolean goalTest(State currentState);
    public abstract int pathCost(ArrayList<Operator> sequenceOfOperators);
    
}
