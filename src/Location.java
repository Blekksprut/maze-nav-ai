public class Location {
    private int fx;
    private int fy;
    public Location(int aX, int aY) {
        fx = aX;
        fy = aY;
    }

    //Get the x or y coordinate
    public int getX() { return fx; }
    public int getY() { return fy; }

    //Used to move in a direction by one space
    public void goUp() { this.fy--;}
    public void goDown() { this.fy++;}
    public void goLeft() { this.fx--;}
    public void goRight() { this.fx++;}

    //returns true if both x's and y's match
    public boolean matchingLocation(Location testLoc) {
        return !(this.getX() != testLoc.getX() || this.getY() != testLoc.getY());
    }
}



