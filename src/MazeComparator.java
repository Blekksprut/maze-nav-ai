import java.util.Comparator;

public class MazeComparator implements Comparator<MazeState>
{

    @Override
    public int compare(MazeState state1, MazeState state2)
    {
        return state1.getTotalCost() - state2.getTotalCost();
    }

}