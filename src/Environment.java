public class Environment {
    // floor is an array of vectors; each vector is a column of the floor
    public Status[][] fFloorMap;
    private int fMapHeight;
    private int fMapWidth;
    private Location[] fGoalStates;

    public Environment(Status[][] aFloorMap, Location[] aGoalStates) {
        fFloorMap = aFloorMap;
        fMapHeight = fFloorMap[0].length;
        fMapWidth = fFloorMap.length;
        fGoalStates = aGoalStates;
    }
    public Location[] getGoalStates() { return fGoalStates; }
    public int getMapHeight() { return fMapHeight; }
    public int getMapWidth() { return fMapWidth; }

}
