import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class background{
    Image img, img2, img3, img4, img5;
    int x = 0, x1 = 0, x2 = 0;

    ArrayList<bgImg> mountains1 = new ArrayList<>();
    ArrayList<bgImg> mountains2 = new ArrayList<>();
    ArrayList<bgImg> trees = new ArrayList<>();


    public void background(){
        try {
            this.img = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-bg.png"));
            this.img2 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-foreground-trees.png"));
            this.img3 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-montain-far.png"));
            this.img4 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-mountains.png"));
            this.img5 = ImageIO.read(getClass().getResource("assets/paralaxBG/parallax-mountain-trees.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    Timer time = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            x-=1;
            x1-=2;
            x2-=3;
        }
    });

    public void paint(Graphics g) {
        g.drawImage(img,0,0,1280,720,null);
        g.drawImage(img2,0,0,1280,720,null);
        g.drawImage(img3,0,0,1280,720,null);
        g.drawImage(img4,0,0,1280,720,null);
        g.drawImage(img5,0,0,1280,720,null);
    }
}
