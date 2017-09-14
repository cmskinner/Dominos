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
  boolean isRotated = false;

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
    if (leftIndex == 100)
    {
      return " |-|-| ";
    }
    return " |" + leftIndex + "|" + rightIndex + "| ";
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
    isRotated = true;
  }

  /**
   * Makes the Domino essentially get itself, rather than getting it when it
   * is needed. The Domino just knows how to find the image for itself.
   *
   * @return String which is the file path needed for the image.
   */
  public String getDominoImage()
  {
    String imageName;
    if (isRotated)
    {
      imageName = "file:resources/d_tile" + rightIndex + leftIndex + ".png";
    }
    else
    {
      imageName = "file:resources/d_tile" + leftIndex + rightIndex + ".png";
    }
    return imageName;
  }

}
