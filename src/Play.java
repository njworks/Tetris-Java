import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;


/**
 * Created by Nelvin on 05/12/2016.
 */
public class Play extends JFrame implements ActionListener{

    //buttons- start, pause, resume and reset
    static JButton pauseButton = new JButton("Pause ");
    JButton startButton = new JButton("Start ");
    JButton resetButton = new JButton("Reset  ");
    JButton resume = new JButton("   Play  ");

    //calling the grid class for game
    Grid board = new Grid();

    //clip for playing music
    public static Clip clip;

    //constructor for main
    public Play() {
        JPanel boardPanel = new JPanel(new BorderLayout());
        JPanel eastPanel = new JPanel();

        pauseButton.setVisible(false);
        resume.setVisible(false);
        resetButton.setVisible(false);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.add(startButton);
        eastPanel.add(pauseButton);
        eastPanel.add(resume);
        eastPanel.add(resetButton);
        eastPanel.add(board.statusScore);
        eastPanel.add(board.statusOver);

        boardPanel.add(board, BorderLayout.CENTER);

        eastPanel.setBackground(Color.BLUE);
        add(eastPanel, BorderLayout.EAST);

        getContentPane().add(boardPanel, BorderLayout.CENTER);
        setSize(400, 500);
        setResizable(false);
        setTitle("Nelvin Joseph-1503054 ");

        ImageIcon img = new ImageIcon(Play.class.getResource("T.png"));
        setIconImage(img.getImage());

        startButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resume.addActionListener(this);
        resetButton.addActionListener(this);

    }


    public static void main(String[] args) {
        Play game = new Play();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        game.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       try {
           //start button actions
           if (e.getSource() == startButton) {
               board.startGame();
               startButton.setVisible(false);
               resetButton.setVisible(true);
               pauseButton.setVisible(true);

               //play music
               try{
                   URL url = this.getClass().getClassLoader().getResource("thelounge.wav");
                   AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                   clip = AudioSystem.getClip();
                   clip.open(audio);
                   clip.start();
                   clip.loop(Clip.LOOP_CONTINUOUSLY);
               }catch(Exception i){
                   System.out.println(i);
               }

           }

           //pause button action
           if (e.getSource() == pauseButton) {
               board.pauseGame();
               pauseButton.setVisible(false);
               resume.setVisible(true);
               clip.stop();
               resetButton.setVisible(false);
           }

           //resume button action
           if(e.getSource() == resume){
               board.replay();
               resume.setVisible(false);
               pauseButton.setVisible(true);
               clip.start();
               resetButton.setVisible(true);
           }

           //reset game button action
           if (e.getSource() == resetButton) {
               board.resetGame();
               clip.start();

           }
       }
       catch(Exception i){
            System.out.println(i);
       }
    }
}
