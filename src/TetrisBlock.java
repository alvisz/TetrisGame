import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TetrisBlock {
    ArrayList<Point> points = new ArrayList<Point>();
    Color blockColor;

    final int startX = 120;
    final int startY = 20;

    int currentX = startX;
    int currentY = startY;



    private final Color[] blockColors = {Color.GREEN,Color.BLUE,Color.RED,Color.ORANGE,Color.CYAN, Color.MAGENTA, Color.YELLOW};
    private final Point[][] tetrisBlockTypes = {
            // I-Piece
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            // J-Piece
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
            // L-Piece
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
            // O-Piece
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            // S-Piece
                    { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            // T-Piece
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            // Z-Piece
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }
    };

    TetrisBlock(){
        Random rnd = new Random();
        int i = rnd.nextInt(7);
        for(Point p: tetrisBlockTypes[i]){
            p.x = p.x*10+startX;
            p.y = p.y*10+startY;
            points.add(p);
        }
        this.blockColor = blockColors[i];
    }


    /*public void TetrisBlocks(Graphics g){
        g.setColor(Color.RED);
        for (Point p : tetrisBlockTypes[0]) {
            System.out.println("X:"+(p.x + 10) * 10);
            System.out.println("Y:"+(p.y + 10) * 10);
            g.fillRect(p.x * 10+actualX, p.y * 10+actualY, 10, 10);
        }
    }*/


    protected void moveDown(){
        this.currentY+=10;
        for (Point p: this.points){
            p.y +=10;

        }
    }

    protected void moveRight(){
        this.currentX+=10;
        for (Point p: this.points){
            p.x +=10;
        }
    }

    protected void moveLeft(){
        this.currentX+=10;
        for (Point p: this.points){
            p.x -=10;
        }
    }

    protected ArrayList<Point> getPoints(){
        return this.points;
    }
    protected Color getColor(){
        return this.blockColor;
    }
    protected void rotatePoints() {
        int a = (int)Math.toRadians((90));
        for (Point p : this.points){
            p.x -= currentX;
            p.y -=currentY;
            int newx = p.y;
            int newy = -p.x;
            p.x = newx+currentX;
            p.y = newy+currentY;


        }
    }
    protected void removeBlock(int x, int y){
        this.points.remove(new Point(x,y));
    }

}
