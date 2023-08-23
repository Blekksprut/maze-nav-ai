import java.util.*;

public class DFSType extends SearchType {
    public DFSType() {
        code = "DFS";
        longName = "Depth First Search";
        Frontier = new LinkedList<MazeState>();
        Searched = new LinkedList<MazeState>();
    }

    public MazeState search(MazeState initial) {
        Frontier.add(initial);
        ArrayList<MazeState> newStates = new ArrayList<MazeState>();

        while(Frontier.size() > 0 && Searched.size() < Main.MAX_LOOPS) {
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

                //Update the status to searched - used in printing the map at the end
                state.getEnvironment().fFloorMap[state.getCoords().getX()][state.getCoords().getY()] = Status.Searched;
            }
        }
        return null;
    }

    public void addToFrontier(ArrayList<MazeState> newStates) {
        //Reversed newstates because we're doing LIFO to make direction precedent consistent
        Collections.reverse(newStates);
        for(MazeState state: newStates){
            //Don't add to frontier if we've already visited it
            if(!alreadyExists(state)) {
                Frontier.add(state);
            }
        }
    }
}
