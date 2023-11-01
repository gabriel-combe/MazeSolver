package MazeGenerator;

// Imports
import java.util.*;

import MazeDisplay.Grid;
import MazeSolver.Node;

/**
 *
 * @author x9x71
 */
public abstract class MazeGenerator {
    protected Grid grid;
    protected Random rand = new Random();

    public boolean mazeFinished = false;
    public Node[][] gridMaze;

    abstract public void setupMaze();
    abstract public void nextMazeStep();
    abstract public void generateMaze();
    abstract public ArrayList<Node> getMazeProgression();
}
