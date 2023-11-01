/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.*;

/**
 *
 * @author Gabriel Combe-Ounkham
 */
public class MazeGenerator {
    private Grid grid;
    private Random rand = new Random();
    private ArrayList<Node> neighbourWalls = new ArrayList<Node>();

    public boolean mazeFinished = false;
    public Node[][] gridMaze;

    public MazeGenerator(Grid grid){
        this.grid = grid;
        this.gridMaze = grid.cells;
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
            if((wall.y-1 < 0) || (wall.y+1 >= this.grid.getWidth()))
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

    public void setupMaze(){
        int indexX = 2*rand.nextInt(this.grid.cells.length/2);
        int indexY = 2*rand.nextInt(this.grid.cells[0].length/2);

        this.gridMaze[indexX][indexY].setWalkable(true);

        this.neighbourWalls = this.getWalls(this.gridMaze[indexX][indexY]);
    }

    public void nextMazeStep(){
        if(this.neighbourWalls.isEmpty()){
            this.mazeFinished = true;
            return;
        }

        int index = rand.nextInt(this.neighbourWalls.size());
        Node wall = this.neighbourWalls.get(index);

        this.openWall(wall);

        this.neighbourWalls.remove(index);
    }

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
