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
    }
    public static void main(String[] args){
        System.out.println("Game starts");
        new Tetris();
    }

    public void background(){

    }
}
