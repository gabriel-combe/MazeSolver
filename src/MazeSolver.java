import java.lang.Thread;

import MazeDisplay.Grid;
import MazeGenerator.*;
import MazeSolver.Astar;
import MazeSolver.Node;


public class MazeSolver
{
    final static int delay = 0;
    
    public static void main(String[] args)
    {
        Grid grid = new Grid(101, 101, 10);
        Astar solver = new Astar(grid);
        
        int index = 0;
        int displayPathDelay = 0;
        
        // grid.initCells(0.2);
        grid.initCellsWall();

        // MazeGenerator mazegen = new PrimsAlgorithm(grid);
        MazeGenerator mazegen = new WilsonsAlgorithm(grid);
        mazegen.setupMaze();

        // Generate the maze
        while(!mazegen.mazeFinished){
            grid.updateGridImage();
            grid.updateMazeImage(mazegen.getMazeProgression());
            
            mazegen.nextMazeStep();
            grid.cells = mazegen.gridMaze;
            
            grid.drawFrame();
            
            // try {
            //     Thread.sleep(1);
            // } catch (InterruptedException e) {
            //     System.out.println(e.toString());
            // }
        }
        
        Node start = grid.firstFreeNode();
        // Node end = grid.RandomFreeNode();
        Node end = grid.LastFreeNode();
        
        // int status = solver.searchPath(start, end);
        solver.setupSearch(start, end);
        
        // Solve the maze
        while(true)
        {
            grid.updateGridImage();
        
            if(solver.path.isEmpty() && !solver.searchFinished){
                solver.nextStep();
                grid.displayAstarProgress(solver.getOpenSet(), solver.getCloseSet());
            } else if(solver.path.size() != index && solver.searchFinished){
                displayPathDelay = 10;
                grid.displayPathUntil(solver.path, index++);
            } else {
                grid.displayPath(solver.path);
            }

            grid.updateNode(end, 0.0f, 0.0f, 1.0f);
            grid.drawFrame();
            
            try {
                Thread.sleep(delay + displayPathDelay);
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }
}
