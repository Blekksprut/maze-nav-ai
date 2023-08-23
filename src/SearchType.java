import java.util.*;
public abstract class SearchType {
    public String code;
    public String longName;
    public LinkedList<MazeState> Frontier;
    public LinkedList<MazeState> Searched;
    public int fSearchTally;
    public int fFrontierTally;
    public abstract MazeState search(MazeState initial);

    public int getSearchTally() { return fSearchTally; }
    public int getFrontierTally() { return fFrontierTally; }

    public abstract void addToFrontier(ArrayList<MazeState> newStates);

    //Manages finding repeat states
    public Boolean alreadyExists(MazeState state){
        for(MazeState searchedState: Searched) {
            //Uses compare methods in MazeState to compare the parameter state to states that have been searched
            if(state.compareMazeStates(searchedState)) {
                return true;
            }
        }
        MazeState parent = state.getParent();

        //Parents is used as a visited list unique to each state, to prevent backtracking or looping in the multigoal searches
        //This is a memory saving strategy to prevent routes that find goals from having to re-explore the whole map
        if(Main.NUM_GOAL_SEARCH > 1) {
            while (parent != null) {
                //check if robot has already visited a location by comparing it to its parents
                //prevents revisiting ANY previously searched states
                if (state.getCoords().matchingLocation(parent.getCoords())) {
                    return true;
                }
                parent = parent.getParent();
            }
        }
        return false;
    }

    public boolean isGoalLocation(MazeState state)
    {
        //Returns true only if the number of goals specified by user has been found
        //Also rewards nodes for finding a goal
        int i = 0;
        int goalsFound = 0;
        for (Location goalLocation : state.getEnvironment().getGoalStates()) {
            if (state.getCoords().matchingLocation(goalLocation)) {
                state.setFoundGoali(i);
                // reward the node and its children by resetting path cost
                state.setPathCost(0);
            }
            i++;
        }
        for(Boolean found: state.getFoundGoal()){
            if(found){
                goalsFound++;
            }
        }
        return (goalsFound >= Main.NUM_GOAL_SEARCH);
    }
}

