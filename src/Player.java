import java.util.ArrayList;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 * This is the player class which holds the player's hand information and
 * other miscellaneous mechanics that has to do with player interaction.
 */
public class Player
{

  private static final int HAND_SIZE = 7;

  private int playerIndex;
  private ArrayList<DominoPiece> playerHand = new ArrayList<DominoPiece>(HAND_SIZE);

  /**
   * Player constructor.
   * @param playerIndex
   */
  public Player(int playerIndex)
  {
    this.playerIndex = playerIndex;
  }

  /**
   * Gets the player's hand for game ending check, deletion of dominos, and
   * playing dominos.
   * @return ArrayList that is the player's hand
   */
  public ArrayList getPlayerHand()
  {
    return playerHand;
  }

  /**
   * Gets a domino from an index in the hand to check for legal moves and
   * copying to board.
   *
   * @param index
   * @return
   */
  public DominoPiece getDominoFromIndex(int index)
  {
    DominoPiece temp = playerHand.get(index);
    playerHand.remove(index);
    return temp;
  }

  /**
   * Called by draw method in other parts of the game when trying to add a
   * domino the the player's hand.
   *
   * @param dominoPiece
   */
  public void addDominoToHand(DominoPiece dominoPiece)
  {
    playerHand.add(dominoPiece);
  }

  /**
   * Utility method used for debugging.
   */
  public void printPlayerHand()
  {
    for (DominoPiece dp: playerHand)
    {
      System.out.print(dp);
    }
    System.out.print("\n");
  }

}
