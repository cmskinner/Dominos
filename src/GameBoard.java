import java.util.ArrayList;
import java.util.Collections;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 * This class is the gameboard for the game of Dominos. This class is what
 * keeps track of which dominos are in what place and creates all the areas
 * in which the dominos will be.
 */
public class GameBoard
{

  private static final boolean DEBUG = false;

  private static final int HAND_SIZE = 7;
  private static final int DOMINO_DOT_SIZE = 6;
  private static int amountOfDominos = summation();

  private ArrayList<DominoPiece> board = new ArrayList<>(amountOfDominos - 1);
  private ArrayList<DominoPiece> boneYard = new ArrayList<>(amountOfDominos);
  private int amountOfDominosInBoneYard = amountOfDominos;


  /**
   * Utility method that creates the amount of dominos needed to play the
   * game based off of the dot size that the game is created with.
   * @return
   */
  private static int summation()
  {
    int start = DOMINO_DOT_SIZE +1;
    int sum = 0;
    while (start > 0)
    {
      sum += start;

      start--;
    }
    return sum;
  }

  /**
   * Creates the gameboard and populates the boneyard.
   */
  public GameBoard()
  {
    populate();
    this.board = board;
    if (DEBUG)
    {
      System.out.println(board.size());
    }

  }

  /**
   * Domino placing mechanic for the board.
   *
   * @param index
   * @param dominoPiece
   */
  public void placeDomino(int index, DominoPiece dominoPiece)
  {
    board.add(index, dominoPiece);
  }

  public DominoPiece getDominoPieceFromBoard(int index)
  {
    return board.get(index);
  }

  /**
   * Utility method for populating the boneyard with all needed Dominos.
   */
  private void populate()
  {
    for (int i = 0; i < DOMINO_DOT_SIZE + 1; i++)
    {
      for (int j = i; j < DOMINO_DOT_SIZE + 1; j++)
      {
        boneYard.add(new DominoPiece(i, j));
      }
    }
    Collections.shuffle(boneYard);

    for (int i = 0; i < 1; i++)
    {
      board.add(new DominoPiece(100,100));
    }
  }

  /**
   * Allows players to get the board and understand what can be played.
   * @return
   */
  public ArrayList getBoard()
  {
//    board.add(new DominoPiece(3, 5));
    return board;
  }

  /**
   * Draws a piece of domino from the boneyard
   * @return
   */
  public DominoPiece drawFromBoneYard()
  {
    if (amountOfDominosInBoneYard == 0)
    {
//      System.out.println("You are out of Dominos");
      amountOfDominosInBoneYard--;
      return new DominoPiece(100,100);
    }
    else
    {
      amountOfDominosInBoneYard--;
//      System.out.println(amountOfDominosInBoneYard);
      return boneYard.get(amountOfDominosInBoneYard);
    }
  }

  /**
   * Utility method for debugging
   */
  public void printBoneYard()
  {
    for (DominoPiece dominoPiece: boneYard)
    {
      System.out.println(dominoPiece);
    }
  }

  /**
   * Utility method for printing the board for debugging.
   */
  public void printBoard()
  {
    int index = 0;
    for (DominoPiece dominoPiece: board)
    {
      if (dominoPiece.equals(new DominoPiece(100, 100)))
      {
        System.out.println("-");

      }
      else
      {
        System.out.print(index + " ");
        System.out.println(dominoPiece);
        index++;
      }
    }
  }

  /**
   * A simple method to get the amount of dominos for the game.
   * @return
   */
  public int getAmountOfDominos()
  {
    return amountOfDominos;
  }

  /**
   * Gets the amount of dominos in the bone yard to detect the end of the game.
   * @return
   */
  public int getAmountOfDominosInBoneYard()
  {
    return amountOfDominosInBoneYard;
  }

}
