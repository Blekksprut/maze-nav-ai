import java.util.*;

public class IDDFSType extends SearchType {
    private int fCurrentDepth;
    //public LinkedList<MazeState> SearchTally;
    public IDDFSType() {
        code = "CUS1";
        longName = "Iterative Deepening Depth First Search";
        Frontier = new LinkedList<MazeState>();
        Searched = new LinkedList<MazeState>();
        fSearchTally = 0;
        fFrontierTally = 0;
        fCurrentDepth = 0;
    }
    public int getCurrentDepth() {
        return fCurrentDepth;
    }
    public void incrementCurrentDepth() { this.fCurrentDepth++; }

    public MazeState search(MazeState initial) {
        MazeState solution  = null;
        //Loop until a solution is found, incrementing the depth with each iteration
        //This will keep incrementing as long as iterative search returns null
        while (solution == null && fSearchTally < Main.MAX_LOOPS){
            //Clears the frontier and searched with each new iteration
            fFrontierTally += Frontier.size();
            Frontier.clear();
            fSearchTally += Searched.size();
            Searched.clear();
            solution = iterativeSearch(initial);
            incrementCurrentDepth();
        }
        fFrontierTally += Frontier.size();
        fSearchTally += Searched.size();
        return solution;
    }

    public MazeState iterativeSearch(MazeState initial){
        Frontier.add(initial);
        ArrayList<MazeState> newStates = new ArrayList<MazeState>();

        int loops = 0;
        while(Frontier.size() > 0 && loops < Main.MAX_LOOPS) {
            MazeState state = Frontier.removeLast();
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
        return null;
    }

    public  void addToFrontier(ArrayList<MazeState> newStates) {
        //Reversed newstates because we're doing LIFO to make direction precedent consistent
        Collections.reverse(newStates);
        for(MazeState state: newStates){
            //checks if the nodes are within the depth previously set and if it does not already exist
            // if so add them to frontier
            if(state.getPathCost() <= getCurrentDepth() && !alreadyExists(state)) {
                Frontier.add(state);
            }
        }
    }

}
