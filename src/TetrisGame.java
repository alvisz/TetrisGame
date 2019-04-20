import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TetrisGame extends JPanel implements KeyListener, ActionListener {
    private int score = 0;
    private final int width;
    private final int height;

    int bgX = 0,bgX1 = 0,bgX2 = 0, bgX3 = 0;
    Image img,img2,img3,img4,img5;
    Font font;

    //gameStates
    boolean isRunning = false;
    boolean gameOver = false;

    ArrayList<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    TetrisBlock currentBlock;
    JLabel scoreText = new JLabel(Integer.toString(score));

    Timer gameplay = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            goDown();
        }
    });

    Timer BGtime = new Timer(41, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bgX -=1;
            bgX1 -=2;
            bgX2 -=3;
            bgX3 -=4;
            repaint();
        }
    });
    boolean textOn = true;
    Timer textBlink = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            textOn = !textOn;
        }
    });

    private void getAssets(){
        try {
            InputStream is = TetrisGame.class.getResourceAsStream("assets/m6x11.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            img = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-bg.png"));
            img2 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-montain-far.png"));
            img3 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-mountains.png"));
            img4 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-trees.png"));
            img5 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-foreground-trees.png"));
        } catch (IOException|FontFormatException e){
            e.printStackTrace();
        }
    }


    public TetrisGame(int width, int height){
        this.getAssets();
        this.width = width;
        this.height = height;
        setSize(width,height);
        setFocusable(true);
        addKeyListener(this);
        setOpaque(false);
        BGtime.start();
    }

    private void start(){
        isRunning = true;
        textBlink.stop();
        if (gameOver){
            score = 0;
            blocks.clear();
            this.currentBlock = null;
            gameOver = false;
        }
        repaint();
        this.currentBlock = new TetrisBlock();
        this.blocks.add(currentBlock);
        gameplay.start();

    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.background(g);
        g.setColor(new Color(255,255,255,100));
        g.fillRect(50,630,360,10);
        //left wall
        g.fillRect(40,120,10,520);
        //right wall
        g.fillRect(410,120,10,520);

        g.setColor(Color.GREEN);
        g.setFont(getFont(40));
        if (!gameOver) g.drawString("Score: ".concat(Integer.toString(score)),700,260);
        g.setColor(new Color(255,255,255,100));
        g.fillRect(460,120 ,100,520);

        if (!isRunning){
            g.setColor(Color.GREEN);
            g.setFont(getFont(40));
            g.drawString("Press \"P\" to play",700,350);
        }

        if (gameOver){
            textBlink.start();
            g.setColor(Color.RED);
            g.setFont(getFont(60));
            g.drawString("You lost!",700,350);
            g.drawString("Your score was: "+score,700,420);
            if (textOn){
                g.drawString("Press \"P\" to play",700,490);
            }

        }
        drawArray(g);
        //grid
        g.setColor(Color.black);
        for ( int x = 50; x <= 400; x += 30 )
            for ( int y = 120; y <= 620; y += 30 )
                g.drawRect( x, y, 30, 30 );
    }

    private void drawArray(Graphics g){
        for (TetrisBlock t: blocks){
            g.setColor(t.getColor());
            for (Point p :t.getPoints()) {
                g.fillRect(p.x , p.y, 30, 30);
            }
        }
    }

    private void clearLine(int lineY){
        for (int i = 50;i<400;i+=30){
            for (TetrisBlock t: blocks){
                t.removeBlock(i,lineY);
            }
        }
        for (TetrisBlock t: blocks){
            for (Point pp: t.getPoints()){
                if (pp.y<lineY){
                    pp.y+=30;
                }
            }
        }
        score+=1;
    }

    private void scanForFullLine(){
        for (int y=600;y>=90;y-=30){
            int count = 0;
            for (int x = 50;x<=400;x+=30){
                if (findInBlocksList(x,y)){
                    System.out.println("Atrada, Y:"+y);
                    count+=1;
                    System.out.println("CountAtY:"+y+", Count:"+count);
                    if (y==90){
                        gameOver = true;
                    }
                }
                if (count == 12){
                    clearLine(y);
                    scanForFullLine();
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
            int targetY = p.y+30;
            int targetX = p.x;
            if (p.y == 600){
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

    private boolean allowedToMoveHorizontal(TetrisBlock block, boolean side){
        // If side is TRUE, checks for right side else for left
        for (Point p: block.getPoints()){
            if (side){
                if (p.getX()+30>400) return false;
            } else {
                if (p.getX()-30<50) return false;
            }
            for (TetrisBlock pp: blocks){
                if (pp != block){
                    for (Point ppp: pp.getPoints()){
                        if (p.getY() == ppp.getY()){
                            if (side){
                                if (p.getX()+30 == ppp.getX()){
                                    return false;
                                }
                            } else {
                                if (p.getX()-30 == ppp.getX()){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void goDown(){
        if (checkIfUnder()){
            scanForFullLine();
            if (!gameOver){
                this.currentBlock = new TetrisBlock();
                this.blocks.add(currentBlock);
            } else gameplay.stop();
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
                if (allowedToMoveHorizontal(currentBlock,false)){
                    this.currentBlock.moveLeft();
                }
                repaint();
                break;
            case KeyEvent.VK_RIGHT :
                if (allowedToMoveHorizontal(currentBlock, true)){
                    this.currentBlock.moveRight();
                }
                repaint();
                break;
            case KeyEvent.VK_SPACE :
                break;
            case KeyEvent.VK_ENTER :
                if (allowedToMoveHorizontal(currentBlock,true) && allowedToMoveHorizontal(currentBlock,false)) {
                    this.currentBlock.rotatePoints();
                }
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

    private void background(Graphics g){

            g.drawImage(img,0,0,1280,720,null);
            if (this.bgX <= -1280){
                this.bgX = 0;
            }
            g.drawImage(img2,bgX,0,1280,720,null);
            g.drawImage(img2,bgX+1280,0,1280,720,null);
            if (this.bgX1 <= -1280){
                this.bgX1 = 0;
            }
            g.drawImage(img3,bgX1,0,1280,720,null);
            g.drawImage(img3,bgX1+1280,0,1280,720,null);
            if (this.bgX2 <= -1280){
                this.bgX2 = 0;
            }
            g.drawImage(img4, bgX2,0,1280,720,null);
            g.drawImage(img4,bgX2+1280,0,1280,720,null);
            if (this.bgX3 <= -1280){
                this.bgX3 = 0;
            }
            g.drawImage(img5,bgX3,0,1280,720,null);
            g.drawImage(img5,bgX3+1280,0,1280,720,null);


    }

    public static Font getFont(int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, TetrisGame.class.getResourceAsStream("assets/m6x11.ttf"));
            font = font.deriveFont(Font.PLAIN, size);
            return font;
        } catch (Exception ex) {
        }
        return new Font("SansSerif", Font.PLAIN, size);
    }



}
