import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 * This is the object that creates all the elements and players that the game
 * needs to function. This includes the gameboard, 2 players currently, and
 * well as certain mechanics.
 */
public class GameManager
{

  private static final boolean CONSOLE_MODE = false;
  private static final boolean DEBUG = false;

  Scanner scanner = new Scanner(System.in);

  List<Player> players = new ArrayList<>();
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
//    gameLoop();  //This was used when the GUI didn't exist, it now does and
// the game loop in basically within the GUI in Dominos.java

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

    if (CONSOLE_MODE)
    {
      printPlayerHands();
      System.out.println();
    }
  }

  /**
   * The draw mechanic for a given player to draw from the boneyard.
   * @param player
   */
  protected void draw(Player player)
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
   * This is simply only used for the console version of the
   */
  private void gameLoop()
  {

    while (!gameOver)
    {
      if (gameBoard.getAmountOfDominosInBoneYard() < 0)
      {
        gameOver = true;
        break;
      }

      if (CONSOLE_MODE)
      {
        System.out.println("Player " + currentPlayer + "'s turn.");
        System.out.println("1 for Draw, 2 for Placement");
      }

      int temp = scanner.nextInt();
      if (temp == 1)
      {
        draw(players.get(currentPlayer));
        printPlayerHands();
      }
      else
      {

        if (CONSOLE_MODE)
        {
          System.out.println("Which domino?");
        }

        temp = scanner.nextInt();

        if (DEBUG)
        {
          System.out.println("GetLeftIndex: " + leftIndex
                  + " GetRightIndex: " + rightIndex);

        }
        if (round == 0)
        {
          gameBoard.placeDomino(gameBoard.getAmountOfDominos() - 1, players.get
                  (currentPlayer).getDominoFromIndex(temp));
          leftIndex = gameBoard.getAmountOfDominos() - 1;
          rightIndex = gameBoard.getAmountOfDominos() - 1;

        }
        else
        {
          DominoPiece pickedDomino = players.get(currentPlayer)
                  .getDominoFromIndex(temp);

          if (CONSOLE_MODE)
          {
            System.out.println("Left (1) or right (2) of board?");
          }

          int leftOrRight = scanner.nextInt();
          if (leftOrRight == 1)
          {
            int leftValue = gameBoard.getDominoPieceFromBoard(leftIndex)
                    .getLeftIndex();

            if (DEBUG)
            {
              System.out.println((pickedDomino.getLeftIndex() == leftValue) ||
                      (pickedDomino.getRightIndex() == leftValue));
            }

            if ((pickedDomino.getLeftIndex() == leftValue) || (pickedDomino
                    .getRightIndex() == leftValue) || (pickedDomino
                    .getLeftIndex() == 0) || (leftValue == 0))
            {
              if ((pickedDomino.getLeftIndex() == leftValue)|| (pickedDomino
                      .getLeftIndex() == 0))
              {
                pickedDomino.rotate();
              }

              gameBoard.placeDomino(leftIndex, pickedDomino);
              rightIndex++;
            }
          }
          else
          {
            int rightValue = gameBoard.getDominoPieceFromBoard(rightIndex )
                    .getRightIndex();
            if ((pickedDomino.getLeftIndex() == rightValue) || (pickedDomino
                    .getRightIndex() == rightValue) || (pickedDomino
                    .getRightIndex() == 0) || (rightValue == 0))
            {
              if ((pickedDomino.getRightIndex() == rightValue) ||
                      (pickedDomino.getRightIndex() == 0))
              {
                pickedDomino.rotate();
              }
              gameBoard.placeDomino(rightIndex + 1, pickedDomino);
              rightIndex++;
            }
            else
            {
              players.get(currentPlayer).addDominoToHand(pickedDomino);
            }
          }
        }



        if (CONSOLE_MODE)
        {
          switchPlayer();
          gameBoard.printBoard();
          System.out.println();
          printPlayerHands();
          System.out.println();
        }

//        System.out.println("PLAY: " + gameBoard.getAmountOfDominosInBoneYard());
        if (gameBoard.getAmountOfDominosInBoneYard() < 0)
        {
          gameOver = true;
          break;
        }
      }
    }

    switchPlayer();
    if (CONSOLE_MODE)
    {
      System.out.println("GAME OVER! PLAYER " + currentPlayer + " WINS!");
    }
  }

  /**
   * Switch players mechanic.
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

  /**
   * Returns the gameboard, which includes where dominos are played and the
   * boneyard.
   *
   * @return GameBoard
   */
  public GameBoard getGameBoard()
  {
    return gameBoard;
  }


  /*************************
   * Utility
   ******************************/

  /**
   * Utility method for debugging that prints player hands.
   */
  public void printPlayerHands()
  {
    for (Player player: players)
    {
      System.out.println("Player " + players.indexOf(player));
      player.printPlayerHand();
    }
  }

  /**
   * Returns the current player
   *
   * @return int current player index in players ArrayList
   */
  public int getCurrentPlayer()
  {
    return currentPlayer;
  }

}
