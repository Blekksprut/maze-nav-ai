import java.util.*;
public class GBFSType extends SearchType {
    public GBFSType() {
        code = "GBFS";
        longName = "Greedy Best First Search";
        Frontier = new LinkedList<MazeState>();
        Searched = new LinkedList<MazeState>();
    }

    public MazeState search(MazeState initial) {
        Frontier.add(initial);
        ArrayList<MazeState> newStates = new ArrayList<MazeState>();

        while(Frontier.size() > 0 && Searched.size() < Main.MAX_LOOPS) {
            MazeState state = Frontier.pollFirst();

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
            Collections.sort(Frontier, new MazeComparator());
        }
        return null;
    }

    public void addToFrontier(ArrayList<MazeState> newStates) {

        for(MazeState state: newStates){
            //Don't add to frontier if we've already visited it
            if(!alreadyExists(state)) {
                //Total cost is manhattan distance only
                state.setTotalCost(state.getManCost());
                //Reward state for finding goal motivating it to continue to the next goal in that path
                for(Boolean Found: state.getFoundGoal()){
                    if(Found) state.rewardState();
                }
                Frontier.add(state);
            }
        }

    }
}


