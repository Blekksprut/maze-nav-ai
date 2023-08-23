import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class Main {
    public static final int NUM_SEARCHES = 6;
    public static SearchType[] lTypes;
    public static final int MAX_LOOPS = 10000;
    public static int NUM_GOAL_SEARCH = 0;
    public static void main(String[] args) {
        /**
         * @param args - [0] The name of the file to read from
         *             - [1] Search type
         */
        //Initialise the first maze state and environment from problem file with no parent or last move
        MazeState initialState = readProblemFile(args[0]);

        //Initialise all search methods
        initMethods(initialState);

        //Create search object based on args
        String searchName = args[1];
        SearchType thisSearch = null;

        //Loop through the search types available until match to searchName is found
        for(int i = 0; i < NUM_SEARCHES; i++) {
            if(lTypes[i].code.compareTo(searchName) == 0) {
                thisSearch = lTypes[i];
            }
        }

        //Throw error if search name is unknown
        if(thisSearch == null) {
            System.out.println("Search type identified by " + searchName + " does not exist. Methods are case sensitive.");
            System.exit(1);
        }

        //Print summary based on initial state
        System.out.println("Robot starting position is cell " + initialState.getCoords().getX() + ", " + initialState.getCoords().getY());
        for(int i = 0; i < initialState.getEnvironment().getGoalStates().length; i++){
            System.out.println("Goal position " + (i+1) + " is cell " + initialState.getEnvironment().getGoalStates()[i].getX() + ", " + initialState.getEnvironment().getGoalStates()[i].getY());
        }
        System.out.println(" ");

        //Ask user for how many goals they would like to reach
        Scanner readLine = new Scanner(System.in);
        System.out.println("Out of the " + initialState.getEnvironment().getGoalStates().length + " goals on the map, how many goals should I visit?");
        //Error handling of input
        while(true){
            try {
                NUM_GOAL_SEARCH = readLine.nextInt();
                if (NUM_GOAL_SEARCH > initialState.getEnvironment().getGoalStates().length || NUM_GOAL_SEARCH < 1){
                    System.out.println("Error: Please enter a valid number between 1 and " + initialState.getEnvironment().getGoalStates().length);
                }else{
                    break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: Please enter a valid number between 1 and " + initialState.getEnvironment().getGoalStates().length);
                readLine.nextLine();
            }
        }
        //Run the search and find the solution
        MazeState solution = thisSearch.search(initialState);

        //Add discovered nodes and frontier nodes together to find total nodes
        int totalNodes = 0;
        if(thisSearch.code.equals("CUS1") || thisSearch.code.equals("CUS2")){
            //Iterative searches reset at each iteration so tally is required
            totalNodes = thisSearch.getFrontierTally() + thisSearch.getSearchTally();
        } else { totalNodes = thisSearch.Frontier.size() + thisSearch.Searched.size(); }

        //If solution not found within max loops, print no solution found
        if(solution == null){
            System.out.println("No solution found for " + thisSearch.longName + " within " + MAX_LOOPS + " loops for " + totalNodes + " nodes");
        } else {
            //Required output
            System.out.println(args[0] + " " + thisSearch.longName + ". Number of nodes: " + totalNodes);
            printSolution(solution);

            //Print information
            System.out.println("\nNumber of goals on the map: " + solution.getEnvironment().getGoalStates().length);
            System.out.println("Number of goals found: " + NUM_GOAL_SEARCH + "\n");
            solution.printMap();
        }
    }

    //Initialise each search method for potential use
    private static void initMethods(MazeState state) {
        lTypes = new SearchType[NUM_SEARCHES];
        lTypes[0] = new BFSType();
        lTypes[1] = new DFSType();
        lTypes[2] = new GBFSType();
        lTypes[3] = new ASType();
        lTypes[4] = new IDDFSType();
        lTypes[5] = new IDASType();
    }

    //Parses strings from the txt file into location objects
    private static Location stringToLoc(String aString) {
        //Strip any trailing spaces
        aString = aString.stripTrailing();

        //Get rid of braces
        aString = aString.substring(1, aString.length() - 1);

        //Split the string at the comma, and handle any number of spaces directly after it
        String[] coords = aString.split(",\\s*");

        //Create location based on coords
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        return (new Location(x,y));
    }

    // Reads and parses text from txt file into an environment object representing the floor map
    private static MazeState readProblemFile(String fileName) {
        try {
            //create file reading objects
            FileReader reader = new FileReader(fileName);
            BufferedReader map = new BufferedReader(reader);

            String mapDimension = map.readLine();

            // Get rid of braces
            mapDimension = mapDimension.substring(1, mapDimension.length() - 1);

            //Split the string at the comma, and remove any spaces directly after
            String[] bothDimensions = mapDimension.split(",\\s*");

            //Set map dimensions
            int mapHeight = Integer.parseInt(bothDimensions[0]);
            int mapWidth = Integer.parseInt(bothDimensions[1]);

            //Create the array of arrays with correct dimensions
            Status[][] floor = new Status[mapWidth][mapHeight];

            //Set agent starting position
            Location robotLoc = stringToLoc(map.readLine());

            //Set goal locations
            String goalLoc = map.readLine();

            //Split by | character and remove any spaces directly after
            String[] goalLocs = goalLoc.split("\\|\\s*");
            Location[] goalStates = new Location[goalLocs.length];

            Environment environment = new Environment(floor, goalStates);

            //Set an entirely blank floor map status
            for (int j = 0; j < mapHeight; j++) {
                for (int i = 0; i < mapWidth; i++) {
                    floor[i][j] = Status.Empty;
                }
            }

            //Set the goal locations into location object
            for(int i = 0; i < goalLocs.length; i++){
                Location goal = stringToLoc(goalLocs[i]);
                floor[goal.getX()][goal.getY()] = Status.Goal;
                goalStates[i] = goal;
            }

            //Set walls on the map
            String line;
            //Keep reading from the file while there are still more lines to read
            while((line = map.readLine()) != null) {
                //Get rid of braces
                line = line.substring(1, line.length() - 1);

                //Set wall cells into array
                String[] wallCells = line.split(",\\s*");
                int wallXStart = Integer.parseInt(wallCells[0]);
                int wallYStart = Integer.parseInt(wallCells[1]);
                int wallXLength = Integer.parseInt(wallCells[2]);
                int wallYHeight = Integer.parseInt(wallCells[3]);

                for (int j = wallYStart; j < wallYStart + wallYHeight; j++) {
                    for (int i = wallXStart; i < wallXStart + wallXLength; i++) {
                        floor[i][j] = Status.Wall;
                    }
                }
            }

            //Close the resource, and return the environment
            map.close();

            return (new MazeState(environment, robotLoc));
        }

        catch(FileNotFoundException ex) {
            //The file didn't exist, show an error
            System.out.println("Error: File \"" + fileName + "\" not found.");
            System.out.println("Please check the path to the file.");
            System.exit(1);
        }
        catch(IOException ex) {
            //There was an IO error, show and error message
            System.out.println("Error in reading \"" + fileName + "\". Try closing it and programs that may be accessing it.");
            System.out.println("If you're accessing this file over a network, try making a local copy.");
            System.exit(1);
        }

        //this code should be unreachable. This statement is simply to satisfy the IDE
        return null;
    }

    public static void printSolution(MazeState solution) {
        //Creates a stack of directions
        Stack<Direction> stack = new Stack<Direction>();
        while(solution != null) {
            stack.push(solution.getLastMove());
            if(solution.getLastMove() != null){
                switch(solution.getLastMove()) {
                    case Up:
                        solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.PathUp;
                        break;
                    case Left:
                        solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.PathLeft;
                        break;
                    case Down:
                        solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.PathDown;
                        break;
                    case Right:
                        solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.PathRight;
                        break;
                    default:
                        break;
                }
            } else {
                solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.PathStart;
            }

            //solution.getEnvironment().fFloorMap[solution.getCoords().getX()][solution.getCoords().getY()] = Status.Path;
            solution = solution.getParent();
        }
        stack.pop();

        //Print out the solution from the beginning to the end
        while(!stack.isEmpty())
            System.out.println(stack.pop() + ";");
    }



}