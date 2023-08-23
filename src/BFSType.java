import java.util.*;
public class BFSType extends SearchType {
    public BFSType() {
        code = "BFS";
        longName = "Breadth First Search";
        Frontier = new LinkedList<MazeState>();
        Searched = new LinkedList<MazeState>();
    }

    public MazeState search(MazeState initial) {
        Frontier.add(initial);
        ArrayList<MazeState> newStates = new ArrayList<MazeState>();

        while(Frontier.size() > 0 && Searched.size() < Main.MAX_LOOPS) {
            MazeState state = Frontier.pop();

            if(isGoalLocation(state)) {
                return state;
            }
            else {
                //Find valid moves from our current node to add to the frontier
                newStates = state.explore();

                //Add it to the list of searched states, so that it isn't searched again
                Searched.add(state);

                //Add newStates (list of things made to be added to frontier) to our existing frontier
                addToFrontier(newStates);

                //Update the status to searched - used in printing the map at the end
                state.getEnvironment().fFloorMap[state.getCoords().getX()][state.getCoords().getY()] = Status.Searched;
            }
        }
        return null;
    }

    public void addToFrontier(ArrayList<MazeState> newStates) {

        for(int i = 0; i < newStates.size(); i++) {
            MazeState newState = newStates.get(i);
            //Don't add to frontier if we've already visited it
            if(!alreadyExists(newState)) {
                Frontier.add(newState);
            }
        }
    }
}


