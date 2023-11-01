/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Gabriel Combe-Ounkham
 */
public class Node {
    
    // PRIVATE
    private boolean walkable;
    private Node parent = null;
    private float gCost = 0.0f;
    private float hCost = 0.0f;
    
    // PUBLIC
    public int x;
    public int y;


    public Node(int x, int y, boolean walkable){
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }
    
    // Get walkable
    public boolean isWalkable(){
        return this.walkable;
    }

    // Set walkable
    public void setWalkable(boolean walkable){
        this.walkable = walkable;
    }
    
    // gCost Setter
    public void setGCost(float gCost){
        this.gCost = gCost;
    }
    
    // gCost Getter
    public float getGCost(){
        return this.gCost;
    }
    
    // hCost Setter
    public void setHCost(float hCost){
        this.hCost = hCost;
    }
    
    // hCost Getter
    public float getHCost(){
        return this.hCost;
    }
    
    // Set parent
    public void setParent(Node parent){
        this.parent = parent;
    }
    
    // Get parent
    public Node getParent(){
        return this.parent;
    }

    // Get X
    public int getX(){
        return this.x;
    }

    // Get Y
    public int getY(){
        return this.y;
    }
    
    // Compute the fCost
    public float fCost(){
        return this.gCost + this.hCost;
    }

    // Compute the euclidian distance between two nodes
    public float getDistanceTo(Node other){
        return (float)Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    @Override
    public boolean equals(Object o){
        if(o == this)
            return true;

        if(!(o instanceof Node))
            return false;

        Node n = (Node) o;

        return Integer.compare(this.x, n.x) == 0 && Integer.compare(this.y, n.y) == 0;
    }
}
