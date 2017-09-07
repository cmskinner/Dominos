import java.util.ArrayList;
import java.util.List;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 */
public class Player
{

  private static final int HAND_SIZE = 7;

  private int playerIndex;
  private List<DominoPiece> playerHand = new ArrayList<DominoPiece>(HAND_SIZE);


  public Player(int playerIndex)
  {
    this.playerIndex = playerIndex;
  }

//  public List getPlayerHand()
//  {
//    return playerHand;
//  }

  public int getPlayerIndex()
  {
    return playerIndex;
  }

  public DominoPiece getDominoFromIndex(int index)
  {
    DominoPiece temp = playerHand.get(index);
    playerHand.remove(index);
    return temp;
  }

  public DominoPiece playDomino(int cardIndex)
  {
    return playerHand.get(cardIndex);
  }

  public void addDominoToHand(DominoPiece dominoPiece)
  {
    playerHand.add(dominoPiece);
  }

  public void printPlayerHand()
  {
    for (DominoPiece dp: playerHand)
    {
      System.out.println(dp);
    }
  }

}
