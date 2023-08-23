import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.*;

public class ASType extends SearchType {
    public ASType() {
        code = "AS";
        longName = "A Star Search";
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

                //Add newStates (list of things made to be added to frontier) to our existing frontier
                addToFrontier(newStates);

                //Update the status to searched - used in printing the map at the end
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
                state.setTotalCost(state.getManCost() + state.getPathCost());
                //Reward state for finding goal motivating it to continue to the next goal in that path
                for(Boolean Found: state.getFoundGoal()){
                    if(Found) state.rewardState();
                }
                Frontier.add(state);
            }
        }
    }
}



