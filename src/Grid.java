import java.lang.Math;
import java.util.ArrayList;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Grid
{
    private int width, height, scale;

    private BufferedImage image, scaledImage;
    private WritableRaster raster;
    private ColorModel colourModel;
    
    JFrame imageFrame = new JFrame("Maze Solver");
    ImagePanel imagePanel;
    
    Node[][] cells;

    
    public Grid(int width, int height, int scale)
    {
        this.width = width;
        this.height = height;
        this.scale = scale;

        cells = new Node[width][height];
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        raster = image.getRaster();
        colourModel = image.getColorModel();

        scaledImage = new BufferedImage((scale * width), (scale * height), BufferedImage.TYPE_INT_RGB);

        imagePanel = new ImagePanel(scaledImage);        
        imageFrame.add(imagePanel);
        imageFrame.setSize(((scale * width) + 16), ((scale * height) + 36));
        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imageFrame.setVisible(true);
    }
    
    public void updateGridImage()
    {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(cells[i][j].isWalkable()) {
                    setCellColour(i, j, 1.0F, 1.0F, 1.0F);
                } else {
                    setCellColour(i, j, 0.0F, 0.0F, 0.0F);
                }
            }
        }
    }

    // Display the full path
    public void displayPath(ArrayList<Node> path){
        for (Node node : path)
            setCellColour(node.x, node.y, 0.0F, 1.0F, 0.0F);
    }

    public void displayPathUntil(ArrayList<Node> path, int index){
        for(int i = 0; i < index; i++)
            setCellColour(path.get(i).x, path.get(i).y, 0.0F, 1.0F, 0.0F);
    }
    
    public void displayAstarProgress(TreeSet<Node> openSet, HashSet<Node> closeSet){
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(openSet.contains(cells[i][j])) {
                    setCellColour(i, j, 1.0F, 1.0F, 0.0F);
                } else if(closeSet.contains(cells[i][j])){
                    setCellColour(i, j, 1.0F, 0.0F, 0.0F);
                }
            }
        }
    }

    public void drawFrame(){
        Graphics2D scaledImageGraphicsContext = scaledImage.createGraphics();
        scaledImageGraphicsContext.drawImage(image, 0, 0,(scale * width), (scale * height), null);
        scaledImageGraphicsContext.dispose();
        
        imageFrame.validate();
        imageFrame.repaint();
    }
    
    private void setCellColour(int x, int y, float red, float green, float blue)
    {
        Color colour  = new Color(red, green, blue);
        
        raster.setDataElements(x, y, colourModel.getDataElements(colour.getRGB(), null));
    }

    public void initCells()
    {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(Math.random() < 0.5) {
                    cells[i][j] = new Node(i, j, false);
                } else {
                    cells[i][j] = new Node(i, j, true);
                }
            }
        }
    }

    public void initCellsWall()
    {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                cells[i][j] = new Node(i, j, false);
            }
        }
    }
    
    public void initCells(double density)
    {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(Math.random() < density) {
                    cells[i][j] = new Node(i, j, false);
                } else {
                    cells[i][j] = new Node(i, j, true);
                }
            }
        }
    }

    public void updateMazeImage(HashSet<Node> maze){
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(maze.contains(cells[i][j]))
                    setCellColour(i, j, 1.0F, 1.0F, 1.0F);
                else
                    setCellColour(i, j, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public Node firstFreeNode(){
        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                if(cells[i][j].isWalkable())
                    return cells[i][j];
            }
        }

        return null;
    }

    public Node LastFreeNode(){
        for(int j = height-1; j >= 0 ; j--) {
            for(int i = width-1; i >= 0; i--) {
                if(cells[i][j].isWalkable())
                    return cells[i][j];
            }
        }

        return null;
    }
    
    private void copyGrid(Node[][] source, Node[][] destination)
    {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                destination[i][j] = source[i][j];
            }
        }
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    private class ImagePanel extends JPanel
    {
        private BufferedImage image;
    
        public ImagePanel(BufferedImage image)
        {  
            this.image = image;
        }

        public void paintComponent(Graphics g)   
        {  
            super.paintComponent(g);

            if (image == null) {
                return;
            }
    
            g.drawImage(image, 0, 0, null);
        }
    }
}
