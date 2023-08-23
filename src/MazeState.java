import java.util.ArrayList;
import java.util.LinkedList;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class MazeState {
    private Environment fEnvironment;
    private MazeState fParent;
    private Location fCoords;
    private Direction fLastMove;
    private boolean[] fFoundGoal;
    private int fManCost;
    private int fPathCost;
    private int fTotalCost;

    //Used for initial state only
    public MazeState(Environment aEnvironment, Location aCoords) {
        fParent = null;
        fEnvironment = aEnvironment;
        fCoords = aCoords;
        //Array of booleans used to track which goals have been found
        this.fFoundGoal = new boolean[aEnvironment.getGoalStates().length];
        setManCost();
        fTotalCost = getManCost();
        fPathCost = 0;
    }

    //Used for every other state
    public MazeState(MazeState aParent, Direction aLastMove) {
        fParent = aParent;
        fEnvironment = aParent.fEnvironment;
        fCoords = new Location(aParent.getCoords().getX(), aParent.getCoords().getY());
        fLastMove = aLastMove;
        //Array of booleans used to track which goals have been found
        fFoundGoal = new boolean[fEnvironment.getGoalStates().length];
        for(int i = 0; i < fFoundGoal.length; i++){
            if(fParent.getFoundGoal()[i]){
                fFoundGoal[i] = true;
            }
        }
        fTotalCost = 0;
        fPathCost = aParent.getPathCost() + 1;
    }

    public Location getCoords() { return fCoords; }
    public MazeState getParent() { return fParent; }
    public Environment getEnvironment() { return fEnvironment; }
    public int getTotalCost() { return fTotalCost; }
    public int getPathCost() { return fPathCost; }
    public Direction getLastMove() { return fLastMove; }
    public boolean[] getFoundGoal() { return fFoundGoal;}
    public void setTotalCost(int value){ fTotalCost = value; }
    public void setPathCost(int aPathCost) { this.fPathCost = aPathCost; }
    public void setFoundGoali(int i) { this.fFoundGoal[i] = true;}
    //Used to reward states when a goal is found
    public void rewardState(){this.fTotalCost -= 5;}
    public int getManCost() { return fManCost; }

    //Calculates the manhattan distance to all goals and sets the state to the lowest
    public void setManCost(){
        int[] manCosts = new int[getEnvironment().getGoalStates().length];
        int i = 0;
        //Populate manCosts with all manhattan distances
        for (Location goal: getEnvironment().getGoalStates()){
            manCosts[i] = (abs(fCoords.getX() - goal.getX()) + (abs(fCoords.getY() - goal.getY())));
            i++;
        }
        int minManCost = Integer.MAX_VALUE;
        //Find the lowest manhattan distance
        for (int j = 0; j < manCosts.length; j++){
            int cost = manCosts[j];
            //Don't use any goals that have already been found
            if(!fFoundGoal[j]) {
                minManCost = min(minManCost, cost);
            }
        }
        this.fManCost = minManCost;
    }

    //Creates a list of states that are to be added to the frontier
    public ArrayList<MazeState> explore() {

        //populate children
        ArrayList<Direction> possibleMoves = getPossibleActions();
        ArrayList<MazeState> children = new ArrayList<MazeState>();
        for (Direction dir: possibleMoves) {
            children.add(move(dir));
        }
        return children;
    }

    //Find moves that are possible from a state and that do not hit walls or run off the map
    private  ArrayList<Direction> getPossibleActions() {

        ArrayList<Direction> result = new ArrayList<Direction>();
        Location coords = fCoords;

        //check if moving up is valid
        if(coords.getY() > 0){
            if(fEnvironment.fFloorMap[coords.getX()][coords.getY()-1] != Status.Wall){
                result.add(Direction.Up);
            }
        }

        //check if moving left is valid
        if(coords.getX() > 0) {
            if(fEnvironment.fFloorMap[coords.getX()-1][coords.getY()] != Status.Wall) {
                result.add(Direction.Left);
            }
        }

        //check if moving down is valid
        if(coords.getY() < fEnvironment.getMapHeight()-1) {
            if(fEnvironment.fFloorMap[coords.getX()][coords.getY()+1] != Status.Wall) {
                result.add(Direction.Down);
            }
        }

        //check if moving right is valid
        if(coords.getX() < fEnvironment.getMapWidth()-1) {
            if(fEnvironment.fFloorMap[coords.getX()+1][coords.getY()] != Status.Wall){
                result.add(Direction.Right);
            }
        }

        return result;
    }

    private MazeState move(Direction dir) {

        MazeState result = new MazeState(this, dir);
        //Amend the childs coordinates based on what direction it came from
        switch(dir) {
            case Up:
                result.getCoords().goUp();
                break;
            case Left:
                result.getCoords().goLeft();
                break;
            case Down:
                result.getCoords().goDown();
                break;
            case Right:
                result.getCoords().goRight();
                break;
        }
        //Set the manCost for the child after its coordinates have been updated
        result.setManCost();
        return result;
    }

    public void printMap(){
        //print map
        Environment env = fEnvironment;
        Location robot = fCoords;
        char wallChar = '\u2588';
        for (int j = 0; j < env.getMapHeight(); j++) {
            for (int i = 0; i < env.getMapWidth(); i++) {
                if (env.fFloorMap[i][j] == Status.Wall) {
                    System.out.print("|" + wallChar + wallChar + wallChar);
                } else if (env.fFloorMap[i][j] == Status.Goal) {
                            System.out.print("| G ");
                } else if (i == robot.getX() && j == robot.getY()) {
                    System.out.print("| x ");
                } else if(env.fFloorMap[i][j] == Status.PathUp){
                    System.out.print("| ^ ");
                } else if(env.fFloorMap[i][j] == Status.PathLeft){
                    System.out.print("| < ");
                } else if(env.fFloorMap[i][j] == Status.PathDown){
                    System.out.print("| v ");
                } else if(env.fFloorMap[i][j] == Status.PathRight){
                    System.out.print("| > ");
                } else if (env.fFloorMap[i][j] == Status.Searched) {
                    System.out.print("| s ");
                } else if (env.fFloorMap[i][j] == Status.Empty) {
                    System.out.print("|   ");
                } else if (env.fFloorMap[i][j] == Status.PathStart) {
                    System.out.print("| o ");
                }
            }
            System.out.println("|");
        }
        System.out.println(" ");
    }

    //Compares two states to see if they are identical based on their coordinates
    //Also checks goal progress, which allows routes from goals to re-explore nodes that otherwise block a valid path
    public boolean compareMazeStates(MazeState searchState) {
        return (this.getCoords().matchingLocation(searchState.getCoords()) && compareGoalProgress(searchState));
    }

    //Compares two states to see if they have the same record of goals founds
    private boolean compareGoalProgress(MazeState searchState) {
        for(int i = 0; i < this.fFoundGoal.length; i++) {
            if (this.fFoundGoal[i] != searchState.getFoundGoal()[i]) {
                return false;
            }
        }
        return true;
    }




}
