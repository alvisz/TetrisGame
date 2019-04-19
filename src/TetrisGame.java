import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class TetrisGame extends JPanel implements KeyListener, ActionListener {
    private int score = 0;
    private int xPos = 0;
    private int yPos = 0;
    private final int width;
    private final int height;

    ArrayList<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    TetrisBlock currentBlock;


    JLabel scoreText = new JLabel(Integer.toString(score));
    public TetrisGame(int width, int height){
        this.width = width;
        this.height = height;
        setSize(width,height);
        setFocusable(true);
        addKeyListener(this);

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
        g.setFont(new Font("SansSerif", Font.PLAIN, 50));
        g.drawString("Score: ".concat(Integer.toString(score)),300,260);

        g.setColor(Color.GREEN);
        g.fillRect(xPos,yPos, 10,10);

        drawArray(g);
        //grid

        System.out.println("X: "+xPos+" Y: "+ yPos);

        //new TetrisBlock().TetrisBlocks(g);

        new TetrisBlock();
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
                this.currentBlock.moveDown();
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
                this.currentBlock = new TetrisBlock();
                this.blocks.add(currentBlock);
                repaint();
                break;
            case KeyEvent.VK_ENTER :
                this.currentBlock.rotatePoints();
                repaint();
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
