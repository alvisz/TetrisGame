import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame{
    static GraphicsConfiguration graphics;

    private final int SCALE = 3;
    private final int width = 320*SCALE;
    private final int height = 240*SCALE;

    public Tetris(){
        TetrisGame tetris = new TetrisGame(width,height);
        setSize(width,height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("TETRIS");
        setVisible(true);
        setResizable(false);
        this.add(tetris);
    }
    public static void main(String[] args){
        System.out.println("Game starts");
        new Tetris();
    }
}
