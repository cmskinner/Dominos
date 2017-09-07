import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 * The basic domino piece that is used in the game of dominos.
 */
public class DominoPiece
{

  private static final boolean DEBUG = true;

  private int leftIndex;
  private int rightIndex;

  /**
   * Creates the domino piece with a left and a right value.
   * @param leftIndex
   * @param rightIndex
   */
  public DominoPiece(int leftIndex, int rightIndex)
  {
    this.leftIndex = leftIndex;
    this.rightIndex = rightIndex;
  }

  /**
   * Gives the left index.
   * @return
   */
  public int getLeftIndex()
  {
    return leftIndex;
  }

  /**
   * Give the right index.
   * @return
   */
  public int getRightIndex()
  {
    return rightIndex;
  }

  /**
   * Utility debugging method for displaying the domino pieces nicely.
   * @return
   */
  public String toString()
  {
    return "L: " + leftIndex + ", R: " + rightIndex;
  }

  /**
   * "Rotates" the piece so that numbers can line-up correctly when the piece
   * is played in particular orientations.
   */
  public void rotate()
  {
    int temp = leftIndex;
    leftIndex = rightIndex;
    rightIndex = temp;
  }

  public static void main(String[] args)
  {
    List list = new ArrayList<DominoPiece>();

    for (int i = 0; i < 7; i++)
    {
      for (int j = i; j < 7; j++)
      {
        list.add(new DominoPiece(i, j));
      }
    }

    if (DEBUG)
    {
      for (int i = 0; i < 28; i++)
      {
        System.out.println(list.get(i));
      }
    }

    Collections.shuffle(list);


    if (DEBUG)
    {
      System.out.println("");
      for (int i = 0; i < 28; i++)
      {
        System.out.println(list.get(i));
      }
    }
  }

}
