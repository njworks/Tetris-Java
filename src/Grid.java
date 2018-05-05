import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Created by Nelvin on 05/12/2016.
 */
public class Grid extends JPanel implements ActionListener{

    static final int GRID_WIDTH = 10;   //width of board
    static final int GRID_HEIGHT = 21;  //height of the board
    Timer timer;
    Shapes currentPiece;
    Shapes.EachShapes[] grid_position;

    Clip sound = null;  //sound effect when line created


    //class constructor
    public Grid() {
        setFocusable(true);
        currentPiece = new Shapes();
        timer = new Timer(350, this); // timer for lines down
        grid_position = new Shapes.EachShapes[GRID_WIDTH * GRID_HEIGHT];
        clearGrid();
        addMouseListener(new tetrisAdapter());


        //sound effect when line achieved
        try
        {
            URL newurl = this.getClass().getClassLoader().getResource("pling.wav");
            AudioInputStream aud = AudioSystem.getAudioInputStream(newurl);

            if (sound == null)
            {
                sound = AudioSystem.getClip();
            }
            else
            {
                sound.close();
            } sound.open(aud);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    //rewinds the sound effect back to start to replay
    public void rewind() {
        sound.setFramePosition(0);
    }

    //getting size of shape width and matching with grid
    public int squareW()

    {
        return (int) getSize().getWidth() / GRID_WIDTH;
    }

    public int squareH()

    {
        return (int) getSize().getHeight() / GRID_HEIGHT;
    }


    //position of each shape in board
    public Shapes.EachShapes currentPosition(int xPos, int yPos)

    {
        return grid_position[yPos * GRID_WIDTH + xPos];
    }


    //emptying the board
    void clearGrid() {
        for (int i = 0; i < GRID_HEIGHT * GRID_WIDTH; i++) {
            grid_position[i] = Shapes.EachShapes.NoShape;
        }
    }


    //shape position at bottom of board and begin new shape
    int xPosi = 0;
    int yPosi = 0;
    boolean finished = false;
    void dropFinished() {
    for (int i = 0; i < 4; i++) {
        int x = xPosi + currentPiece.xEquals(i);
        int y = yPosi - currentPiece.yEquals(i);
        grid_position[y * GRID_WIDTH + x] = currentPiece.currentShape();
    }
        removeLine();

    if (!finished) {
        nextPiece();
    }
    }

    //status
    JLabel statusScore = new JLabel("Score: 0");
    JLabel statusOver = new JLabel("");
    boolean started = false;

    //random shape created and game over when reached top
    public void nextPiece() {

    currentPiece.randomPiece();
    xPosi = GRID_WIDTH / 2 + 1;
    yPosi = GRID_HEIGHT - 1 + currentPiece.yMinimum();
            if (!move(currentPiece, xPosi, yPosi - 1)) {
                Play.clip.stop();
                timer.stop();
                started = false;
                currentPiece.newShape(Shapes.EachShapes.NoShape);
                statusScore.setText("Score: " + String.valueOf(removedLines));
                statusOver.setText("Game Over");
                Play.pauseButton.setVisible(false);
            }
    }

    void eachHorizontalLine() {
        if (!move(currentPiece, xPosi, yPosi -1 ))
            dropFinished();
    }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (finished) {
                finished = false;
                nextPiece();
            } else {
                eachHorizontalLine();
            }
        }

        //drawing the shapes
        void drawShape(Graphics g, int xValue, int yValue, Shapes.EachShapes shape) {
            Color color = shape.colour;
            g.setColor(color);
            g.fillRect(xValue + 1, yValue + 1, squareW() - 2, squareH() - 2);
            g.setColor(color.brighter());
            g.drawLine(xValue, yValue + squareH() - 1, xValue, yValue);
            g.drawLine(xValue, yValue, xValue + squareW() - 1, yValue);
            g.setColor(color.darker());
            g.drawLine(xValue + 1, yValue + squareH() - 1, xValue + squareW() - 1, yValue + squareH() - 1);
            g.drawLine(xValue + squareW() - 1, yValue + squareH() - 1, xValue + squareW() - 1, yValue + 1);
        }

    //drawing the grid
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        super.setBackground(Color.black);
        int gridTop = (int) size.getHeight() - GRID_HEIGHT * squareH();

    for (int i = 0; i < GRID_HEIGHT; i++) {
        for (int j = 0; j < GRID_WIDTH; ++j) {
          Shapes.EachShapes shape = currentPosition(j, GRID_HEIGHT - i - 1);

            if (shape != Shapes.EachShapes.NoShape) {
                drawShape(g, j * squareW(), gridTop + i * squareH(), shape);
            }
        }
    }

        if (currentPiece.currentShape() != Shapes.EachShapes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = xPosi + currentPiece.xEquals(i);
                int y = yPosi - currentPiece.yEquals(i);
                drawShape(g, x * squareW(), gridTop + (GRID_HEIGHT - y - 1) * squareH(), currentPiece.currentShape());
            }
        }
    }


    //start game for button
    int removedLines = 0;
    boolean paused = false;

    public void startGame() {
        if (paused)
            return;

        started = true;
        finished = false;
        removedLines = 0;
        clearGrid();
        nextPiece();
        timer.start();
    }


    //reset game for button
    public void resetGame(){
        startGame();
        removedLines = 0;
        Play.pauseButton.setVisible(true);
        statusScore.setText("Score: "+String.valueOf(removedLines));
        statusOver.setText("");
    }

    //pause game
    public void pauseGame() {
        if (!started)
            return;

        paused = !paused;

        if (paused) {
            timer.stop();
            statusScore.setText("Paused");
        }
    }

    //resume game after pause
    public void replay(){
        if(!started)
            return;

        paused = !paused;

        if(!paused){
        timer.start();
        statusScore.setText("Score: "+String.valueOf(removedLines));}
        repaint();
    }

    //moving the shape in the grid
    public boolean move(Shapes newP, int xValue, int yValue) {
        for (int i = 0; i < 4; ++i) {

            int x = xValue + newP.xEquals(i);
            int y = yValue - newP.yEquals(i);

            if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT)
                return false;

            if (currentPosition(x, y) != Shapes.EachShapes.NoShape)
                return false;
        }

        currentPiece = newP;
        xPosi = xValue;
        yPosi = yValue;
        repaint();

        return true;
    }


    //keeps score and removes full lines
    public void removeLine() {
    int numberLines = 0;

    for (int i = GRID_HEIGHT - 1; i >= 0; --i) {
        boolean line = true;
        for (int j = 0; j < GRID_WIDTH; ++j) {
            if (currentPosition(j, i) == Shapes.EachShapes.NoShape) {
                line = false;
                break;
            }
        }


        if (line) {
            sound.start();
            numberLines += 10;
            rewind();

        for (int k = i; k < GRID_HEIGHT - 1; ++k) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                grid_position[k * GRID_WIDTH + j] = currentPosition(j, k + 1);
            }
        }


    }

        if (numberLines > 0) {
            finished = true;
            removedLines += numberLines;
            currentPiece.newShape(Shapes.EachShapes.NoShape);
            statusScore.setText("Score: "+String.valueOf(removedLines));
            repaint();

        }

    }
    }

    //mouse tracker for movements
    class tetrisAdapter extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e){
            if(!started || currentPiece.currentShape() == Shapes.EachShapes.NoShape)
                return;
            int mouseButton = e.getButton();

            if(paused){
                return;
            }else {


                switch (mouseButton) {
                    case MouseEvent.BUTTON1:
                        move(currentPiece, xPosi - 1, yPosi);
                        break;
                    case MouseEvent.BUTTON3:
                        move(currentPiece, xPosi + 1, yPosi);
                        break;
                    case MouseEvent.BUTTON2:
                        move(currentPiece.rightTurn(), xPosi, yPosi);
                        break;
                }

            }
        }

    }


}



