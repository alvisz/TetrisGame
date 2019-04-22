import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame{
    static GraphicsConfiguration graphics;

    private final int width = 1280;
    private final int height = 720;

    public Tetris(){
        TetrisGame tetris = new TetrisGame(width,height);
        setSize(width,height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("TETRIS");
        setVisible(true);
        setResizable(false);
        this.add(tetris);
        Thread music = new Thread(playMusic);
        music.start();
    }
    public static void main(String[] args){
        System.out.println("Game starts");
        new Tetris();
    }

    Runnable playMusic = new Runnable() {
        public void run() {
            try
            {
                Clip crit = AudioSystem.getClip();
                AudioInputStream inputStream1 = AudioSystem.getAudioInputStream(this.getClass().getResource("assets/sounds/bgmusic.wav"));
                crit.open(inputStream1);
                crit.loop(Clip.LOOP_CONTINUOUSLY);
                crit.start();

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
