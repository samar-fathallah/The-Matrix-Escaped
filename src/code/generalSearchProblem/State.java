package code.generalsearchproblem;

public abstract class State {
    
    public abstract String encode();

    public abstract State clone();
    
    public abstract void updateState(Operator operator);
    
}
