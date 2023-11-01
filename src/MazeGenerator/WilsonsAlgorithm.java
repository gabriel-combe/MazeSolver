package MazeGenerator;

// Imports
import java.util.ArrayList;
import java.util.HashSet;

import MazeDisplay.Grid;
import MazeSolver.Node;

public class WilsonsAlgorithm extends MazeGenerator {
    private HashSet<Node> mazeNodes = new HashSet<Node>();
    private ArrayList<Node> randomWalkNodes = new ArrayList<Node>();
    private ArrayList<Node> validNode = new ArrayList<Node>();
    private int[][] directionList = {{0,-1}, {1, 0}, {0, 1}, {-1, 0}};
    private int totalMazeCell = 0;

    public WilsonsAlgorithm(Grid grid){
        this.grid = grid;
        this.gridMaze = grid.cells;
        this.totalMazeCell = Math.round((float)this.grid.getWidth()/2) * Math.round((float)this.grid.getHeight()/2);
        
        for(int i = 0; i < this.grid.getWidth(); i+=2)
            for(int j = 0; j < this.grid.getHeight(); j+=2)
                this.validNode.add(this.gridMaze[i][j]);
    }

    public ArrayList<Node> getMazeProgression(){
        return this.randomWalkNodes;
    }

    private void randomWalk(){
        int randomIndex = this.rand.nextInt(this.directionList.length);

        int[] offsets = this.directionList[randomIndex];

        Node lastNode = this.randomWalkNodes.get(this.randomWalkNodes.size()-1);
        Node lastLastNode = this.randomWalkNodes.size() == 1? null: this.randomWalkNodes.get(this.randomWalkNodes.size()-2);
        
        if(2*offsets[0] + lastNode.getX() < 0 || 2*offsets[0] + lastNode.getX() >= this.grid.getWidth()){
            this.randomWalk();
            return;
        }

        if(2*offsets[1] + lastNode.getY() < 0 || 2*offsets[1] + lastNode.getY() >= this.grid.getHeight()){
            this.randomWalk();
            return;
        }

        if(this.gridMaze[2*offsets[0]+lastNode.getX()][2*offsets[1]+lastNode.getY()].equals(lastLastNode)){
            this.randomWalk();
            return;
        }

        this.randomWalkNodes.add(this.gridMaze[offsets[0]+lastNode.getX()][offsets[1]+lastNode.getY()]);
        this.randomWalkNodes.add(this.gridMaze[2*offsets[0]+lastNode.getX()][2*offsets[1]+lastNode.getY()]);
    }

    private void eraseLoop(int indexNode){
        int randomWalkNodeLength = this.randomWalkNodes.size()-1;
        for(int index = randomWalkNodeLength; indexNode < index; index--)
            this.randomWalkNodes.remove(index);
    }

    @Override
    public void setupMaze(){
        int randomIndex = this.rand.nextInt(this.validNode.size());

        Node newNode = this.validNode.get(randomIndex);
        newNode.setWalkable(true);
        this.mazeNodes.add(newNode);

        this.validNode.remove(randomIndex);
    }
    
    @Override
    public void nextMazeStep(){
        if(this.mazeNodes.size() == this.totalMazeCell){
            this.mazeFinished = true;
            return;
        }
        
        if(this.randomWalkNodes.isEmpty()){
            int randomIndex = this.rand.nextInt(this.validNode.size());

            Node newStartNode = this.validNode.get(randomIndex);

            this.randomWalkNodes.add(newStartNode);

            this.validNode.remove(randomIndex);
        }
        
        this.randomWalk();
        
        Node lastNode = this.randomWalkNodes.get(this.randomWalkNodes.size()-1);
        int indexLastNode = this.randomWalkNodes.indexOf(lastNode);
        
        if(indexLastNode != this.randomWalkNodes.size()-1){
            this.eraseLoop(indexLastNode);
            return;
        }
        
        if(this.mazeNodes.contains(lastNode)){
            for (Node node : this.randomWalkNodes) {
                this.gridMaze[node.getX()][node.getY()].setWalkable(true);
                
                if(node.getX()%2 == 0 && node.getY()%2 == 0){
                    this.validNode.remove(node);
                    this.mazeNodes.add(node);
                }
            }
            
            this.randomWalkNodes.clear();
        }
    }

    @Override
    public void generateMaze(){
        this.setupMaze();

        while(this.mazeNodes.size() != this.totalMazeCell){
            if(this.randomWalkNodes.isEmpty()){
                int randomIndex = this.rand.nextInt(this.validNode.size());
    
                Node newStartNode = this.validNode.get(randomIndex);
    
                this.randomWalkNodes.add(newStartNode);
    
                this.validNode.remove(randomIndex);
            }
    
            this.randomWalk();
    
            Node lastNode = this.randomWalkNodes.get(this.randomWalkNodes.size()-1);
            int indexLastNode = this.randomWalkNodes.indexOf(lastNode);
    
            if(indexLastNode != this.randomWalkNodes.size()-1){
                this.eraseLoop(indexLastNode);
                return;
            }
    
            if(this.mazeNodes.contains(lastNode)){
                for (Node node : this.randomWalkNodes) {
                    this.gridMaze[node.getX()][node.getY()].setWalkable(true);
                    
                    if(node.getX()%2 == 0 && node.getY()%2 == 0){
                        this.validNode.remove(node);
                        this.mazeNodes.add(node);
                    }
                }
                this.randomWalkNodes.clear();
            }
        }

        this.mazeFinished = true;
    }
}
