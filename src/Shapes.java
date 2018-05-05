import java.awt.*;
import java.util.Random;

/**
 * Created by Nelvin on 05/12/2016.
 */

public class Shapes {

    //Shapes created
    enum EachShapes {
        NoShape(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0)),
        Z(new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, new Color(204, 52, 49)),
        S(new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, new Color(204, 85, 191)),
        Line(new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, new Color(81, 83, 204)),
        T(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, new Color(104, 192, 109)),
        SquareShape(new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, new Color(101, 50, 102)),
        L(new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(255, 253, 73)),
        OppositeLShape(new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(109, 255, 98));
        public int[][] coordinates;
        public Color colour;

        EachShapes(int[][] xy, Color k) { this.coordinates = xy;
            colour = k;
        }
    }

     EachShapes eachPiece;
     int[][] coordinates;

    Shapes() {
      coordinates = new int[10][2];
      newShape(EachShapes.NoShape);
    }

    //new shape coordinates
    public void newShape(EachShapes s) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coordinates[i][j] = s.coordinates[i][j];
            }
        }
        eachPiece = s;
    }

    //value of x
    public int xEquals(int i)
    {
      return coordinates[i][0];
    }

    //value of y
    public int yEquals(int i)
    {
      return coordinates[i][1];
    }


    //values of x and y coordinates
    void xCoordinate(int i, int x)
    {
      coordinates[i][0] = x;
    }
    void yCoordinate(int i, int y)
    {
      coordinates[i][1] = y;
    }

    //calling for each shape
    public EachShapes currentShape() {
      return eachPiece;
    }

    //get random shape each time
    public void randomPiece() {
      Random rand = new Random();
      int i = Math.abs(rand.nextInt()) % 7 + 1;
      EachShapes[] values = EachShapes.values();
      newShape(values[i]);
    }

    //minimum value y can move to
    public int yMinimum() {
      int g = coordinates[0][1];
      for (int i = 0; i < 4; i++) {
          g = Math.min(g, coordinates[i][1]);
      }
      return g;
    }

    //for rotating shape
    public Shapes rightTurn() {
      if (eachPiece == EachShapes.SquareShape)
            return this;

      Shapes output = new Shapes();
      output.eachPiece = eachPiece;
      for (int i = 0; i < 4; i++) {
          output.xCoordinate(i, -yEquals(i));
          output.yCoordinate(i, xEquals(i));
        }           return output;
    }

}
