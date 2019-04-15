import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class TetrisGame extends JPanel implements KeyListener, ActionListener {
    /*
    Esmu izveidojis nelielu pamatu tetrim. Pagaidām nekas īpašs. Konsoles izvades ir priekš testēšanas.
    TODO 1.Atradu tetra formu koordinātes, no kurām veidošu pašas formas, pēctam, kad forma parādīsies, viņas likšu iekšā ArrayListā.
    TODO 2.Izveidošu collisions funkciju, kas apstādinās krišanu, kad zem kādas no formas daļas būs citas formas daļa
    TODO 3. Tā, kā ņēmu koordinātes no jau gatava tetra projekta un tur jau ir sarotētas visas formas,
        izveidot funkciju, kas šīs koordinātes izrotē, nevis paņem no jau gatava saraksta, lai būtu nedaudz advanced.
        būtu labi, ja jūs komentāros "pasviestu" ideju, kā to vieglāk izdarīt.
    TODO 4. Ir doma uztaisīt tā, ka tad, kad notīrās līnija, kas nav pašā apakšā, citi gabaliņi, kuriem pazuda pamats
         sāk lēnām krist.
    TODO 5. Varbūt kādi skaņas efekti un epilepsiju izraisošas krāsu maiņas, kad notīras līnija, lai spēle neliktos parāk basic?
    */
    /* Viens formas kvadrāts ir 10x10px. Neliku neko lielāku, lai nebūtu problēmas savādākas izšķirtspējas datoriem, un negribas čakarēties ar responsiveness.
     *2.mājasdarbu neiesniedzu, jo jau iepriekš e-pastā sarunājām par tetri. Neuzskatu, ka man sanāks kaut kas baigi kruts, bet cerams labāk par brick breaker būs :D
     **/

    private int score = 0;

    private int xPos = 0;
    private int yPos = 0;

    private final int width;
    private final int height;


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
        new TetrisBlock().drawBlocks();
        //grid
        g.setColor(Color.black);
        for ( int x = 50; x <= 240; x += 10 )
            for ( int y = 100; y <= 380; y += 10 )
                g.drawRect( x, y, 10, 10 );

        System.out.println("X: "+xPos+" Y: "+ yPos);

        new TetrisBlock().TetrisBlocks(g);

        new TetrisBlock();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                yPos-=10;
                System.out.println("Up");
                repaint();
                break;
            case KeyEvent.VK_DOWN:
                yPos+=10;
                System.out.println("Down");
                if (yPos == 380){
                    yPos = 40;
                    xPos = 150;
                    score+=1;
                }
                repaint();
                break;
            case KeyEvent.VK_LEFT:
                if (xPos == 50){
                    break;
                }
                xPos-=10;
                System.out.println("Left");
                repaint();
                break;
            case KeyEvent.VK_RIGHT :
                if (xPos == 240){
                    break;
                }
                xPos+=10;
                System.out.println("Right");
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
