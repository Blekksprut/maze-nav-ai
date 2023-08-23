import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;

public class IDASType extends SearchType {
    private int fCurrentMaxCost;
    private int fNextBestCost;

    public IDASType() {
        code = "CUS2";
        longName = "Iterative Deepening A Star Search";
        Frontier = new LinkedList<MazeState>();
        Searched = new LinkedList<MazeState>();
        fSearchTally = 0;
        fFrontierTally = 0;
        fCurrentMaxCost = 0;
        fNextBestCost = 0;
    }
    public int getCurrentMaxCost() {
        return fCurrentMaxCost;
    }
    public void setCurrentMaxCost(int aCurrentCost) {
        this.fCurrentMaxCost = aCurrentCost;
    }
    public int getNextBestCost() {
        return fNextBestCost;
    }
    public void setNextBestCost(int fNextBestCost) {
        this.fNextBestCost = fNextBestCost;
    }

    public MazeState search(MazeState initial) {
        MazeState solution  = null;

        //Loop until a solution is found, setting the max cost higher with each loop
        //This will keep incrementing as long as iterative search returns null
        while (solution == null && fSearchTally < Main.MAX_LOOPS){
            //Clears the frontier and searched with each new iteration
            fFrontierTally += Frontier.size();
            Frontier.clear();
            fSearchTally += Searched.size();
            Searched.clear();
            solution = iterativeSearch(initial);
        }
        fFrontierTally += Frontier.size();
        fSearchTally += Searched.size();
        return solution;
    }

    public MazeState iterativeSearch(MazeState initial){
        int loops = 0;
        ArrayList<MazeState> newStates = new ArrayList<MazeState>();
        setNextBestCost(Integer.MAX_VALUE);

        Frontier.add(initial);
        while(Frontier.size() > 0 && loops < Main.MAX_LOOPS) {
            MazeState state = Frontier.removeLast();

            //Ends iteration by returning null if the current state is somehow above our maximum cost.
            //Or more usually for the first iteration
            if (state.getTotalCost() > getCurrentMaxCost()){
                setCurrentMaxCost(state.getTotalCost());
                return null;
            }

            if(isGoalLocation(state)) {
                return state;
            }
            else {
                //Find valid moves from our current node to add to the frontier
                newStates = state.explore();

                //Add it to the list of searched states, so that it isn't searched again
                Searched.add(state);

                //add newStates (list of things made to be added to frontier) to our existing frontier
                addToFrontier(newStates);

                //Update the status to searched for the printed map
                state.getEnvironment().fFloorMap[state.getCoords().getX()][state.getCoords().getY()] = Status.Searched;
            }
            loops++;
        }
        //Set Max cost for the next iteration
        //Based on best cost found among states not added to the frontier
        setCurrentMaxCost(getNextBestCost());
        return null;
    }

    public  void addToFrontier(ArrayList<MazeState> newStates) {
        //Reversed newstates because we're doing LIFO to make direction precedent consistent
        Collections.reverse(newStates);
        for(MazeState state: newStates){
            //we have to set the cost for each state first to check it
            state.setTotalCost(state.getManCost() + state.getPathCost());
            //Don't add to frontier if we've already visited it
            if(!alreadyExists(state)) {
                if (state.getTotalCost() <= getCurrentMaxCost()) {
                    Frontier.add(state);
                } else if(state.getTotalCost() < getNextBestCost()) {
                    //If the state that isn't being added to the frontier has a total cost less than the
                    //current next best cost, set the next best cost to that total cost
                    setNextBestCost(state.getTotalCost());
                }
            }

        }

    }

}
