import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TetrisBlock {
    ArrayList<Point> points = new ArrayList<Point>();
    Color blockColor;

    int startX = 200;
    int startY = 30;

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
            p.x = p.x*30+startX;
            p.y = p.y*30+startY;
            points.add(p);
        }
        this.blockColor = blockColors[i];
    }

    TetrisBlock(int startx,int starty){
        this.startX = startx;
        this.startY = starty;
        this.currentX = startx;
        this.currentY = starty;
        Random rnd = new Random();
        int i = rnd.nextInt(7);
        for(Point p: tetrisBlockTypes[i]){
            p.x = p.x*30+startX;
            p.y = p.y*30+startY;
            points.add(p);
        }
        this.blockColor = blockColors[i];
    }

    protected void moveDown(int step){
        this.currentY+=step;
        for (Point p: this.points){
            p.y +=step;
        }
    }

    protected void moveRight(int step){
        this.currentX+=step;
        for (Point p: this.points){
            p.x +=step;
        }
    }

    protected void moveLeft(int step){
        this.currentX-=step;
        for (Point p: this.points){
            p.x -=step;
        }
    }

    protected void moveUp(int step){
        this.currentY-=step;
        for (Point p: this.points){
            p.y -=step;
        }
    }

    protected ArrayList<Point> getPoints(){
        return this.points;
    }
    protected Color getColor(){
        return this.blockColor;
    }
    protected void rotatePoints() {
        ArrayList<Point> dd = this.points;
        rotate(dd);
        int smallestX = 0;
        int biggestX = 0;
        for (Point p: dd){
            System.out.println(p.x);
            if (p.x < 50){
                smallestX = 50 - p.x;
            }
            if (p.x > 380){
                biggestX = p.x - 380;
            }
        }
        if (smallestX > 0){
            this.currentX+=smallestX;
            for (Point p: dd){
                p.x +=smallestX;
            }
        }
        if (biggestX > 0){
            this.currentX-=biggestX;
            for (Point p: dd){
                p.x -=biggestX;
            }
        }

    }

    private void rotate(ArrayList<Point> pList){
        for (Point p : pList){
            p.x -= currentX;
            p.y -=currentY;
            int newx = p.y;
            int newy = -p.x;
            p.x = newx+currentX;
            p.y = newy+currentY+90;
        }
    }


    protected void removeBlock(int x, int y){
        this.points.remove(new Point(x,y));
    }


    protected int getCurrentY(){
        return this.currentY;
    }
    protected int getCurrentX(){
        return this.currentX;
    }

    protected void moveUpAnimated(int y){
        while (this.getCurrentY() > y) {
            this.moveUp(1);
            try{
                Thread.sleep(10);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    protected void moveUpAnimated2(int y) {
        for (int i = y; i >= 0; i--) {
            this.moveUp(1);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    protected void moveLeftAnimated(int x){
        while (this.getCurrentX() > x) {
            this.moveLeft(1);
            try{
                Thread.sleep(5);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
