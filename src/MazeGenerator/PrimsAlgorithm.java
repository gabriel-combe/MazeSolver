package MazeGenerator;

// Imports
import java.util.ArrayList;

import MazeDisplay.Grid;
import MazeSolver.Node;

/**
 *
 * @author x9x71
 */
public class PrimsAlgorithm extends MazeGenerator{
    private ArrayList<Node> neighbourWalls = new ArrayList<Node>();
    
    public PrimsAlgorithm(Grid grid){
        this.grid = grid;
        this.gridMaze = grid.cells;
    }

    public ArrayList<Node> getMazeProgression(){
        return this.neighbourWalls;
    }

    private ArrayList<Node> getWalls(Node node){
        ArrayList<Node> neighbourWalls = new ArrayList<Node>();

        int x = node.x;
        int y = node.y;

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(Math.abs(i+j) != 1)
                    continue;

                if((x+i < 0) || (x+i >= this.grid.getWidth()) || (y+j < 0) || (y+j >= this.grid.getHeight()))
                    continue;
                
                if(!this.gridMaze[x+i][y+j].isWalkable())
                    neighbourWalls.add(this.grid.cells[x+i][y+j]);
            }
        }
        
        return neighbourWalls;
    }

    private void openWall(Node wall){
        
        if(wall.x%2 == 1){
            if((wall.x-1 < 0) || (wall.x+1 >= this.grid.getWidth()))
                return;

            Node rightCell = this.gridMaze[wall.x+1][wall.y];
            Node leftCell = this.gridMaze[wall.x-1][wall.y];
            
            if(rightCell.isWalkable() && leftCell.isWalkable())
                return;
            
            if(rightCell.isWalkable()){
                this.neighbourWalls.addAll(this.getWalls(leftCell));
                this.gridMaze[wall.x-1][wall.y].setWalkable(true);
            } else {
                this.neighbourWalls.addAll(this.getWalls(rightCell));
                this.gridMaze[wall.x+1][wall.y].setWalkable(true);
            }

        } else {
            if((wall.y-1 < 0) || (wall.y+1 >= this.grid.getHeight()))
                return;

            Node topCell = this.gridMaze[wall.x][wall.y-1];
            Node bottomCell = this.gridMaze[wall.x][wall.y+1];
            
            if(topCell.isWalkable() && bottomCell.isWalkable())
                return;
            
            if(topCell.isWalkable()){
                this.neighbourWalls.addAll(this.getWalls(bottomCell));
                this.gridMaze[wall.x][wall.y+1].setWalkable(true);
            } else {
                this.neighbourWalls.addAll(this.getWalls(topCell));
                this.gridMaze[wall.x][wall.y-1].setWalkable(true);
            }
        }

        this.gridMaze[wall.x][wall.y].setWalkable(true);
    }

    @Override
    public void setupMaze(){
        int indexX = 2*this.rand.nextInt(this.grid.cells.length/2);
        int indexY = 2*this.rand.nextInt(this.grid.cells[0].length/2);

        this.gridMaze[indexX][indexY].setWalkable(true);

        this.neighbourWalls = this.getWalls(this.gridMaze[indexX][indexY]);
    }
    
    @Override
    public void nextMazeStep(){
        if(this.neighbourWalls.isEmpty()){
            this.mazeFinished = true;
            return;
        }

        int index = this.rand.nextInt(this.neighbourWalls.size());
        Node wall = this.neighbourWalls.get(index);

        this.openWall(wall);

        this.neighbourWalls.remove(index);
    }

    @Override
    public void generateMaze(){
        this.setupMaze();

        while(!this.neighbourWalls.isEmpty()){
            int index = rand.nextInt(this.neighbourWalls.size());
            Node wall = this.neighbourWalls.get(index);

            this.openWall(wall);

            this.neighbourWalls.remove(index);
        }

        this.mazeFinished = true;
    }
}
