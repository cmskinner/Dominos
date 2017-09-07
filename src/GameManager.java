import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 */
public class GameManager
{

  Scanner scanner = new Scanner(System.in);

  private List<Player> players = new ArrayList<>();
  private int numberOfPlayers;
  private int handSize;

  private boolean gameOver = false;
  private int round = 0;
  private int currentPlayer = 0;
  private int leftIndex;
  private int rightIndex;

  private GameBoard gameBoard = new GameBoard();

  /**
   * Constructor that creates the game to be played and goes through the
   * phases of the game.
   *
   * @param numberOfPlayers
   * @param handSize
   */
  public GameManager(int numberOfPlayers, int handSize)
  {
    this.numberOfPlayers = numberOfPlayers;
    this.handSize = handSize;
    for (int i = 0; i < numberOfPlayers; i++)
    {
      players.add(new Player(i));
    }

    drawPhase();
    gameLoop();

  }

  /**
   * Initalizes all the hands with the predetermined amount of cards.
   */
  private void drawPhase()
  {
    for (int cards = 0; cards < handSize; cards++)
    {

      for (Player player: players)
      {

        draw(player);

      }
    }

    printPlayerHands();
    System.out.println();
//    gameBoard.printBoneYard();
  }

  /**
   * The draw mechanic for a given player to draw from the boneyard.
   * @param player
   */
  private void draw(Player player)
  {
    DominoPiece dp = gameBoard.drawFromBoneYard();
    if (dp == new DominoPiece(100, 100))
    {
      gameOver = true;
    }
    else
    {
      player.addDominoToHand(dp);
    }
  }

  /**
   * Main gameloop for the Domino game that controls player turns and how
   * dominos are placed to the gameBoard.
   *
   * There is A LOT of work to do here for refactoring and to still get the
   * game logic to fully work.
   */
  private void gameLoop()
  {

    while (!gameOver)
    {
      System.out.println("1 for Draw, 2 for Placement");
      int temp = scanner.nextInt();
      if (temp == 1)
      {
        draw(players.get(currentPlayer));
        printPlayerHands();
      }
      else
      {
        System.out.println("Which domino?");
        temp = scanner.nextInt();

        if (round == 0)
        {
          gameBoard.placeDomino(gameBoard.getAmountOfDominos(), players.get
                  (currentPlayer).getDominoFromIndex(temp));
          System.out.println(gameBoard.getAmountOfDominos());
          leftIndex = gameBoard.getAmountOfDominos();
          rightIndex = gameBoard.getAmountOfDominos();
        }
        else
        {
          DominoPiece pickedDomino = players.get(currentPlayer)
                  .getDominoFromIndex(temp);
          System.out.println("Left (1) or right (2) of board?");
          int leftOrRight = scanner.nextInt();
          if (leftOrRight == 1)
          {
            int leftValue = gameBoard.getDominoPieceFromBoard(leftIndex)
                    .getLeftIndex();
            System.out.println("Left Value: " + leftValue + "Left Index: " +
                    leftIndex);
            System.out.println("GetLeftIndex: " + pickedDomino.getLeftIndex()
                    + " GetRightIndex: " + pickedDomino.getRightIndex());
            System.out.println((pickedDomino.getLeftIndex() == leftValue) ||
                    (pickedDomino.getRightIndex() == leftValue));
            if ((pickedDomino.getLeftIndex() == leftValue) || (pickedDomino
                    .getRightIndex() == leftValue))
            {
              if (pickedDomino.getLeftIndex() == leftValue)
              {
                pickedDomino.rotate();
              }
              System.out.println("leftIndex: " + leftIndex);
              gameBoard.placeDomino(leftIndex, pickedDomino);
              leftIndex--;
            }
          }
          else
          {
            int rightValue = gameBoard.getDominoPieceFromBoard(rightIndex )
                    .getRightIndex();
            if ((pickedDomino.getLeftIndex() == rightValue) || (pickedDomino
                    .getRightIndex() == rightValue))
            {
              if (pickedDomino.getRightIndex() == rightValue)
              {
                pickedDomino.rotate();
              }
              System.out.println("rightIndex: " + rightIndex);
              gameBoard.placeDomino(rightIndex, pickedDomino);
              rightIndex++;
            }
          }
        }


        switchPlayer();
        gameBoard.printBoard();
        System.out.println();
        printPlayerHands();
        System.out.println();
      }
      //placement
      //draw


    }
  }

  /**
   * Switches players mechanic.
   */
  private void switchPlayer()
  {
    if (currentPlayer ==0)
    {
      currentPlayer = 1;
      round++;
    }
    else
    {
      currentPlayer = 0;

    }
  }


  /*************************
   * Utility
   ******************************/

  public void printPlayerHands()
  {
    for (Player player: players)
    {
      System.out.println("Player " + players.indexOf(player));
      player.printPlayerHand();
    }
  }


  public static void main(String[] args)
  {

    GameManager gm = new GameManager(2, 7);

  }

}
