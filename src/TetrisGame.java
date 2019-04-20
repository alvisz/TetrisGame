import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class TetrisGame extends JPanel implements KeyListener, ActionListener {
    private int score = 0;
    private int xPos = 0;
    private int yPos = 0;
    private final int width;
    private final int height;

    boolean isRunning = false;

    ArrayList<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    TetrisBlock currentBlock;
    JLabel scoreText = new JLabel(Integer.toString(score));

    Timer time = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            goDown();
            repaint();
        }
    });

    public TetrisGame(int width, int height){
        this.width = width;
        this.height = height;
        setSize(width,height);
        setFocusable(true);
        addKeyListener(this);
    }

    private void start(){
        isRunning = true;
        repaint();
        this.currentBlock = new TetrisBlock();
        this.blocks.add(currentBlock);
        time.start();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,width,height);

        //bottom
        g.setColor(Color.GRAY);
        g.fillRect(50,390,200,10);
        //left wall
        g.fillRect(40,100,10,300);
        //right wall
        g.fillRect(250,100,10,300);

        g.setColor(Color.GREEN);
        g.setFont(new Font("SansSerif", Font.PLAIN, 25));
        g.drawString("Score: ".concat(Integer.toString(score)),300,260);

        if (isRunning == false){
            g.setFont(new Font("SansSerif", Font.PLAIN, 40));
            g.drawString("Spied \"P\", lai sāktu spēli",350,150);
        }

        drawArray(g);
        //grid

        g.setColor(Color.black);
        for ( int x = 50; x <= 240; x += 10 )
            for ( int y = 100; y <= 380; y += 10 )
                g.drawRect( x, y, 10, 10 );
    }

    private void drawArray(Graphics g){
        for (TetrisBlock t: blocks){
            g.setColor(t.getColor());
            for (Point p :t.getPoints()) {
                g.fillRect(p.x , p.y, 10, 10);
            }
        }
    }

    private void clearLine(int lineY){
        for (int i = 50;i<250;i+=10){
            for (TetrisBlock t: blocks){
                t.removeBlock(i,lineY);
            }
        }
        for (TetrisBlock t: blocks){
            for (Point pp: t.getPoints()){
                if (pp.y<lineY){
                    pp.y+=10;
                }
            }
        }
        score+=1;
    }

    private void scanForFullLine(){
        for (int y=380;y>90;y-=10){
            int count = 0;
            for (int x = 50;x<=240;x+=10){
                if (findInBlocksList(x,y)){
                    System.out.println("Atrada, Y:"+y);
                    count+=1;
                    System.out.println("CountAtY:"+y+", Count:"+count);
                }
                if (count == 20){
                    clearLine(y);
                }
            }
        }
    }

    private boolean findInBlocksList(int x, int y){
        for (TetrisBlock tt: blocks){
            if (tt.getPoints().contains(new Point(x,y))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfUnder(){
        for (Point p: currentBlock.getPoints()){
            int targetY = p.y+10;
            int targetX = p.x;
            if (p.y == 380){
                return true;
            }
            for (TetrisBlock t: blocks){
                if (t == currentBlock){
                    continue;
                }
                for (Point pp: t.getPoints()){
                    if (pp.y == targetY && pp.x == targetX) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void goDown(){
        if (checkIfUnder()){
            scanForFullLine();
            this.currentBlock = new TetrisBlock();
            this.blocks.add(currentBlock);
        } else currentBlock.moveDown();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                repaint();
                break;
            case KeyEvent.VK_DOWN:
                goDown();
                repaint();
                break;
            case KeyEvent.VK_LEFT:
                this.currentBlock.moveLeft();
                repaint();
                break;
            case KeyEvent.VK_RIGHT :
                this.currentBlock.moveRight();
                repaint();
                break;
            case KeyEvent.VK_SPACE :
                break;
            case KeyEvent.VK_ENTER :
                this.currentBlock.rotatePoints();
                repaint();
                break;
            case KeyEvent.VK_P :
                start();
                break;
            case KeyEvent.VK_C :
                clearLine(380);
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
