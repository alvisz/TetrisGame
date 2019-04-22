import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    Image img,img2,img3,img4,img5, left, right, down;
    Font font;

    //gameStates
    boolean isRunning = false;
    boolean gameOver = false;
    boolean allowedToGoMove = true;


    ArrayList<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    ArrayList<TetrisBlock> queueBlocks = new ArrayList<TetrisBlock>();
    TetrisBlock currentBlock;
    JLabel scoreText = new JLabel(Integer.toString(score));

    Timer gameplay = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            goDown();
        }
    });
    // BG time laiks bija 41ms, bet baigi lago, tapec 100 :D
    Timer BGtime = new Timer(100, new ActionListener() {
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
            left = ImageIO.read(getClass().getResource("assets/arrows/left.png"));
            right = ImageIO.read(getClass().getResource("assets/arrows/right.png"));
            down = ImageIO.read(getClass().getResource("assets/arrows/down.png"));
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
        queueBlocks.clear();
        isRunning = true;
        textBlink.stop();
        if (gameOver){
            score = 0;
            blocks.clear();
            this.currentBlock = null;
            gameOver = false;
        }
        repaint();
        fillTheQueue();
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
        // left wall
        g.fillRect(40,120,10,520);
        // right wall
        g.fillRect(410,120,10,520);
        // queue block
        g.fillRect(460,120 ,getMaxQueueX()-460,520);


        if (!gameOver && isRunning) {
            g.setColor(new Color(255,255,255,200));
            g.setFont(getFont(120));
            g.drawString("SCORE: ".concat(Integer.toString(score)),700,400);
        }

        if (!isRunning){
            g.setColor(new Color(255,255,255,200));
            g.setFont(getFont(40));
            g.drawString("Press \"P\" to play",700,350);

            g.setColor(new Color(255,255,255,200));
            g.setFont(getFont(35));

            g.drawImage(left,550,500, null);
            g.drawString("Left",600,530);

            g.drawImage(down,750,500, null);
            g.drawString("Down",800,530);

            g.drawImage(right,950,500, null);
            g.drawString("Right",1000,530);

            g.drawString("Press SPACE to rotate",660,625);
        } else {
            g.setColor(new Color(255,255,255,200));
            g.setFont(getFont(40));
            g.drawString("Queue: ",480,160);
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
        drawArray(g, blocks);
        drawArray(g, queueBlocks);
        //grid
        g.setColor(Color.black);
        for ( int x = 50; x <= 400; x += 30 )
            for ( int y = 120; y <= 620; y += 30 )
                g.drawRect( x, y, 30, 30 );
    }

    private void drawArray(Graphics g, ArrayList<TetrisBlock> list){
        for (TetrisBlock t: list){
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
                    count+=1;
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
        if (!allowedToGoMove) return false;
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
        if (allowedToGoMove){
            if (checkIfUnder()){
                scanForFullLine();
                if (!gameOver){
                    Thread t = new Thread(moveFromQueue);
                    t.start();
                } else gameplay.stop();
            } else {
                currentBlock.moveDown(30);
                Thread sound = new Thread(playSound);
                sound.start();
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
            case KeyEvent.VK_DOWN:
                goDown();
                repaint();
                break;
            case KeyEvent.VK_LEFT:
                if (allowedToMoveHorizontal(currentBlock,false)){
                    this.currentBlock.moveLeft(30);
                }
                repaint();
                break;
            case KeyEvent.VK_RIGHT :
                if (allowedToMoveHorizontal(currentBlock, true)){
                    this.currentBlock.moveRight(30);
                }
                repaint();
                break;
            case KeyEvent.VK_SPACE :
                if (allowedToMoveHorizontal(currentBlock,true) && allowedToMoveHorizontal(currentBlock,false)) {
                    this.currentBlock.rotatePoints();
                }
                repaint();
                break;
            case KeyEvent.VK_P :
                if (!isRunning | gameOver){
                    start();
                }
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

    public void fillTheQueue(){
        if (!queueBlocks.contains(new TetrisBlock(490,200))){
            queueBlocks.add(new TetrisBlock(490,200));
        }
        if (!queueBlocks.contains(new TetrisBlock(490,300))){
            queueBlocks.add(new TetrisBlock(490,300));
        }
        if (!queueBlocks.contains(new TetrisBlock(490,400))){
            queueBlocks.add(new TetrisBlock(490,400));
        }
        if (!queueBlocks.contains(new TetrisBlock(490,500))){
            queueBlocks.add(new TetrisBlock(490,500));
        }
    }

    public int getMaxQueueX(){
        int max = 0;
        for (TetrisBlock block: queueBlocks){
            for (Point p: block.getPoints()){
                if (p.x>max){
                    max = p.x;
                }
            }
        }
        return max+60;
    }
    Runnable moveFromQueue = new Runnable() {
        public void run() {
            System.out.println("THREAD STARTED: "+Thread.currentThread()+"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"+ System.currentTimeMillis());
            gameplay.stop();
            allowedToGoMove = false;
            TetrisBlock block = queueBlocks.get(0);
            block.moveUpAnimated(30);
            block.moveLeftAnimated(200);
            queueBlocks.remove(block);
            currentBlock = block;
            blocks.add(currentBlock);
            for (int i = 0; i<3; i++){
                block = queueBlocks.get(i);
                block.moveUpAnimated2(100);
                repaint();
            }
            queueBlocks.add(new TetrisBlock(490,500));
            gameplay.start();
            allowedToGoMove = true;
        }
    };

    Runnable playSound = new Runnable() {
        public void run() {
            try
            {
                Clip crit = AudioSystem.getClip();
                AudioInputStream inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("assets/sounds/sound1.wav"));
                crit.open(inputStream1);
                crit.start();

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}
