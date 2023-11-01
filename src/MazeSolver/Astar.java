package MazeSolver;

// Imports
import java.util.*;

import MazeDisplay.Grid;

/**
 *
 * @author x9x71
 */
public class Astar {

    // PRIVATE
    private Grid grid;
    private TreeSet<Node> openSet;
    private HashSet<Node> closeSet;
    private Node start = null;
    private Node end = null;
    
    // PUBLIC
    public ArrayList<Node> path;
    public boolean searchFinished = false;

    public Astar(Grid grid){
        this.grid = grid;
        this.openSet = new TreeSet<Node>(Comparator.comparing(Node::fCost).thenComparing(Node::getHCost).thenComparing(Node::getX).thenComparing(Node::getY));
        this.closeSet = new HashSet<Node>();
        this.path = new ArrayList<Node>();
        this.searchFinished = false;
    }
    
    public Astar(Grid grid, Node start, Node end){
        this.grid = grid;
        this.openSet = new TreeSet<Node>(Comparator.comparing(Node::fCost).thenComparing(Node::getHCost).thenComparing(Node::getX).thenComparing(Node::getY));
        this.closeSet = new HashSet<Node>();
        this.path = new ArrayList<Node>();
        this.start = start;
        this.end = end;
        this.searchFinished = false;
    }
    
    public void setupSearch(Node start, Node end){
        this.start = start;
        this.end = end;
        this.clear();
        this.openSet.add(this.start);
    }
    
    // Start searching a path between the start and end node
    public int searchPath(Node start, Node end){
        this.setupSearch(start, end);
        this.findPath();

        if(this.path.isEmpty())
            return -1;
        
        return 0;
    }
    
    // Start node setter
    public void setStart(Node start){
        this.start = start;
    }
    
    // End node setter
    public void setEnd(Node end){
        this.end = end;
    }
    
    // Get open set
    public TreeSet<Node> getOpenSet(){
        return this.openSet;
    }
    
    // Get close set
    public HashSet<Node> getCloseSet(){
        return this.closeSet;
    }
    
    // Get all the neighbours of a given node
    private ArrayList<Node> neighbours(Node node) {
        ArrayList<Node> neighboursList = new ArrayList<Node>();

        int x = node.x;
        int y = node.y;

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(Math.abs(i+j) != 1)
                    continue;


                if((x+i < 0) || (x+i >= this.grid.getWidth()) || (y+j < 0) || (y+j >= this.grid.getHeight()))
                    continue;
                
                neighboursList.add(this.grid.cells[x+i][y+j]);
            }
        }
        
        return neighboursList;
    }

    // Implementation of one step of the Astar algorithm
    private void astarStep(){
        Node current = this.openSet.first();
        this.openSet.remove(current);
        this.closeSet.add(current);

        if(current.equals(this.end)){
            this.searchFinished = true;
            this.backtrackPath(this.start, this.end);
            return;
        }

        for (Node neighbour : neighbours(current)) {
            if(!neighbour.isWalkable() || this.closeSet.contains(neighbour))
                continue;
            
            float newGCost = current.getGCost() + 1.0f;
            if(newGCost < neighbour.getGCost() || !this.openSet.contains(neighbour)){
                if(this.openSet.contains(neighbour))
                    this.openSet.remove(neighbour);

                neighbour.setGCost(newGCost);
                neighbour.setHCost(neighbour.getDistanceTo(this.end));
                neighbour.setParent(current);
                this.openSet.add(neighbour);
            }
        }
    }

    // Perform one step of astar algorithm (with check)
    public void nextStep(){
        if(this.openSet.isEmpty()){ return; }
        
        this.astarStep();
    }
    
    // Implementation of the full Astar algorithm
    private void findPath(){
        while(!this.openSet.isEmpty()){
            this.astarStep();
        }
    }
    
    // Backtrack the final end node to get the path
    private void backtrackPath(Node start, Node end){
        Node currentNode = end;
        while(!currentNode.equals(start)){
            this.path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        this.path.add(currentNode);

        Collections.reverse(this.path);
    }
    
    // Clear the sets and the path before starting a new search
    private void clear(){
        this.openSet = new TreeSet<Node>(Comparator.comparing(Node::fCost).thenComparing(Node::getHCost).thenComparing(Node::getX).thenComparing(Node::getY));
        this.closeSet = new HashSet<Node>();
        this.path = new ArrayList<Node>();
        this.searchFinished = false;
    }
}
