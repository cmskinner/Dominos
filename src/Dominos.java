import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Christopher Skinner
 * August 25, 2017
 *
 * This is the JavaFX GUI. It contains most of the game loop logic,
 * mechanics, and display features. The computer player exists in this class,
 * which isn't the best design, but it works for what is needed.
 */
public class Dominos extends Application implements EventHandler<ActionEvent>
{
  private Text dominoTitle = new Text("WELCOME TO DOMINOS!");

  HBox hBoxGameOver = new HBox();

  private Button draw, rotateButton, leftBoard, rightBoard;
  GameManager gameManager = new GameManager(2, 7);

  private static final int WINDOW_WIDTH = 1500;
  private static final int WINDOW_HEIGHT = 400;
  private static final int DOMINO_WIDTH = 133;
  private static final int DOMINO_HEIGHT = 67;

  BorderPane handPane, boardPaneTop, boardPaneBottom;

  private DominoPiece nullDomino = new DominoPiece(100,100);
  private DominoPiece currentDominoPiece = nullDomino;

  private int currentPlayer = 0;
  private boolean foundPlaceForComputer = false;
  private int round = 0;
  private int leftIndex, rightIndex = 0;

  private boolean gameOver = false;

  private Canvas canvas;

  Text gameOverText = new Text ("Game Over!");

  /**
   * This is where the program lives for most of it's time during execution.
   * This is where all the button logic, which is where the game logic is.
   *
   * @param stage
   */
  public void start(Stage stage)
  {
    dominoTitle.setFont(Font.font("Verdana", 30));
    gameOverText.setFont(Font.font("Verdana", 30));

    draw = new Button("Draw");
    draw.setOnAction(this);

    leftBoard = new Button("Left");
    leftBoard.setOnAction(this);

    rightBoard = new Button("Right");
    rightBoard.setOnAction(this);

    canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT - 250);

    VBox vBox = new VBox();
    vBox.setPadding(new Insets(10, 10, 10, 10)); //margins (top/right/bottom/left) Default = (0,0,0,0).
    vBox.setSpacing(15);

    Pos centrePos = Pos.BASELINE_CENTER;

    HBox hBoxTitle = new HBox();
    hBoxTitle.setSpacing(15);
    hBoxTitle.setAlignment(centrePos);
    hBoxTitle.getChildren().addAll(dominoTitle);

    HBox hBoxButtons = new HBox();
    hBoxButtons.setSpacing(15); //Spacing in pixels between elements.  Default = 0.
    hBoxButtons.setAlignment(centrePos);
    hBoxButtons.getChildren().addAll(draw);

    HBox hBoxLeftRight = new HBox();
    hBoxLeftRight.setSpacing(15);
    hBoxLeftRight.setAlignment(centrePos);
    hBoxLeftRight.getChildren().addAll(leftBoard, rightBoard);

    HBox hBoxPlayerHand = playersHand();


    handPane = new BorderPane();
    handPane.setCenter(hBoxPlayerHand);
    handPane.setOnMouseClicked(event -> {
//      System.out.println("CLICKED");
      int index = (int) Math.floor(event.getX() / DOMINO_WIDTH);
      if ((index >= 0) && (index < gameManager.players.get(0).getPlayerHand().size()))
      {
        currentDominoPiece = (DominoPiece)gameManager.players.get(0).getPlayerHand().get(index);
//        System.out.println(gameManager.players.get(0).getPlayerHand().get(index));
      }

    });

//    hBoxPlayerHand.setAlignment(Pos.CENTER);


    HBox hBoxBoardBottom = boardRedrawTop();
    HBox hBoxBoardTop = boardRedrawBottom();

    boardPaneTop = new BorderPane();
    boardPaneTop.setCenter(hBoxBoardBottom);
    boardPaneTop.setCenter(hBoxBoardTop);
    boardPaneTop.setOnMouseClicked(event -> {
      HBox tempHBox = boardRedrawTop();
      boardPaneTop.setCenter(tempHBox);
    });


    boardPaneBottom = new BorderPane();
    boardPaneBottom.setCenter(hBoxBoardBottom);
    boardPaneBottom.setOnMouseClicked(event -> {
      HBox tempHBox = boardRedrawBottom();
      boardPaneBottom.setCenter(tempHBox);
    });

    //executes a draw on the human player that presses the button
    draw.setOnAction(event -> {
      gameManager.draw(gameManager.players.get(gameManager.getCurrentPlayer()));
      HBox tempHBox = playersHand();
      handPane.setCenter(hBoxPlayerHand);
//      hBoxPlayerHand.setAlignment(Pos.CENTER);
      handPane.setCenter(tempHBox);
      if (gameManager.getGameBoard().getAmountOfDominosInBoneYard() == 0)
      {
        gameOver = true;
        switchPlayer();
        disableAllButtons();
      }
    });

    //This deals with all the game logic that comes from trying to place a
    // Domino on the left side of the board.
    leftBoard.setOnAction(event -> {

      if (round == 0)
      {
        gameManager.getGameBoard().placeDomino(0, currentDominoPiece);
        gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        gameManager.getGameBoard().getBoard().remove(1);
        currentDominoPiece = nullDomino;
        round++;
        switchPlayer();
        computerPlayerTurn();
      }
      else
      {
        int leftValue = gameManager.getGameBoard().getDominoPieceFromBoard(leftIndex)
                .getLeftIndex();
//        System.out.println((currentDominoPiece.getLeftIndex() == leftValue) || (currentDominoPiece
//                .getRightIndex() == leftValue) || (currentDominoPiece
//                .getLeftIndex() == 0) || (leftValue == 0));
        if ((currentDominoPiece.getLeftIndex() == leftValue) || (currentDominoPiece
                .getRightIndex() == leftValue) || (currentDominoPiece
                .getLeftIndex() == 0) || (currentDominoPiece.getRightIndex()
                == 0) || (leftValue == 0))
        {
          if ((currentDominoPiece.getLeftIndex() == leftValue)|| (currentDominoPiece
                  .getLeftIndex() == 0))
          {
            currentDominoPiece.rotate();
          }

          gameManager.getGameBoard().placeDomino(leftIndex, currentDominoPiece);
          rightIndex++;
          gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
          currentDominoPiece = nullDomino;

          if (gameManager.players.get(0).getPlayerHand().size() == 0)
          {
            gameOver = true;
//            disableAllButtons();
          }
          else
          {
            switchPlayer();
            computerPlayerTurn();
          }

        }
      }
      HBox tempHBox = boardRedrawTop();
      boardPaneTop.setCenter(tempHBox);
      tempHBox = boardRedrawBottom();
      boardPaneBottom.setCenter(tempHBox);
      HBox tempHBox2 = playersHand();
      handPane.setCenter(hBoxPlayerHand);
      handPane.setCenter(tempHBox2);

      if (gameOver)
      {
        disableAllButtons();
      }

    });

    //This deals with all the game logic that comes from trying to place a
    // Domino on the right side of the board.
    rightBoard.setOnAction(event -> {

      if (round == 0)
      {
        gameManager.getGameBoard().placeDomino(0, currentDominoPiece);
        gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        round++;
        gameManager.getGameBoard().getBoard().remove(0);
        currentDominoPiece = nullDomino;
        switchPlayer();
        computerPlayerTurn();
      }
      else
      {
        int rightValue = gameManager.getGameBoard().getDominoPieceFromBoard(rightIndex)
                .getRightIndex();
//        System.out.println((currentDominoPiece.getLeftIndex() == rightValue) || (currentDominoPiece
//                .getRightIndex() == rightValue) || (currentDominoPiece
//                .getRightIndex() == 0) || (rightValue == 0));
        if ((currentDominoPiece.getLeftIndex() == rightValue) || (currentDominoPiece
                .getRightIndex() == rightValue) || (currentDominoPiece
                .getRightIndex() == 0) || (currentDominoPiece.getLeftIndex()
                == 0) || (rightValue == 0))
        {
          if ((currentDominoPiece.getRightIndex() == rightValue) ||
                  (currentDominoPiece.getRightIndex() == 0))
          {
            currentDominoPiece.rotate();
          }
          gameManager.getGameBoard().placeDomino(rightIndex + 1, currentDominoPiece);
          rightIndex++;
          gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
          currentDominoPiece = nullDomino;
          if (gameManager.players.get(0).getPlayerHand().size() == 0)
          {
//            disableAllButtons();
            gameOver = true;
          }
          else
          {
            switchPlayer();
            computerPlayerTurn();
          }
        }
      }

      HBox tempHBox = boardRedrawTop();
      boardPaneTop.setCenter(tempHBox);
      tempHBox = boardRedrawBottom();
      boardPaneBottom.setCenter(tempHBox);
      HBox tempHBox2 = playersHand();
      handPane.setCenter(hBoxPlayerHand);
//      hBoxPlayerHand.setAlignment(Pos.CENTER);
      handPane.setCenter(tempHBox2);

      if (gameOver)
      {
        disableAllButtons();
      }

    });


    hBoxGameOver.setSpacing(15);
    hBoxGameOver.setAlignment(centrePos);
    hBoxGameOver.getChildren().addAll(gameOverText);
    hBoxGameOver.setVisible(false);



    vBox.getChildren().addAll(hBoxTitle, hBoxButtons, boardPaneTop,
            boardPaneBottom,
            hBoxLeftRight,
            handPane, hBoxGameOver);

    Scene scene = new Scene(vBox, WINDOW_WIDTH, WINDOW_HEIGHT);

    stage.setScene(scene);
    stage.show();

  }

  /**
   * This sets up the images to be viewed and displayed on the screen correctly
   *
   * @param list
   * @return ImageView imageView
   */
  private ArrayList<ImageView> rowToBeDisplayed(ArrayList<DominoPiece> list)
  {
    ArrayList<ImageView> imageViews = new ArrayList<>();

    for (int i = 0; i < list.size(); i++)
    {
      ImageView imageView = new ImageView(list.get(i).getDominoImage());
      if (list.get(i).isRotated) imageView.setRotate(180);
      imageViews.add(imageView);

    }

    return imageViews;
  }

  /**
   * Creates the HBox for the player's hand that is drawn, redrawn, and
   * displayed.
   *
   * @return HBox
   */
  private HBox playersHand()
  {
    ArrayList<ImageView> imageViews = rowToBeDisplayed(gameManager.players
            .get(0).getPlayerHand());
    HBox hbox = new HBox();
    for (int i = 0; i < imageViews.size(); i++)
    {
      hbox.getChildren().add(imageViews.get(i));
    }
//    hbox.setAlignment(Pos.CENTER);
    return hbox;
  }

  /**
   * Creates the HBox that is needed for the board to be drawn, redrawn,
   * and displayed on screen.
   *
   * @return
   */
  private HBox boardRedrawTop()
  {
    ArrayList<ImageView> imageViews = rowToBeDisplayed(gameManager
            .getGameBoard().getBoard());
    HBox hbox = new HBox();
    for (int i = 0; i < imageViews.size(); i++)
    {
      if (i % 2 == 1)
      {
        hbox.getChildren().add(imageViews.get(i));
      }
    }
    hbox.setAlignment(Pos.CENTER);
    if (gameOver)
    {
      hbox.setTranslateX(0);
    }
    else
    {
      hbox.setTranslateX(70);
    }

    return hbox;
  }

  private HBox boardRedrawBottom()
  {
    ArrayList<ImageView> imageViews = rowToBeDisplayed(gameManager
            .getGameBoard().getBoard());
    HBox hbox = new HBox();
//    HBox hbox2 = new HBox();
    for (int i = 0; i < imageViews.size(); i++)
    {

      if (i % 2 == 0)
      {
        hbox.getChildren().add(imageViews.get(i));
      }

    }


    hbox.setAlignment(Pos.CENTER);
//    if (gameManager.players.get())
    return hbox;
  }

  /**
   * A simple method to switch players.
   */
  private void switchPlayer()
  {
    if (currentPlayer ==0)
    {
      currentPlayer = 1;
    }
    else
    {
      currentPlayer = 0;
    }
  }

  /**
   * This is all the computer player's logic in how it determines it's move.
   *
   * It does all the checking within the method to check for logical moves
   * and places it. This is a somewhat hack-y way to get around this and not
   * very well designed, it could've been much better.
   *
   * The computer is very simple and just places the first Domino it seems as
   * a possible move and if it has no possible moves, it draws.
   */
  private void computerPlayerTurn()
  {
    int leftValue = gameManager.getGameBoard().getDominoPieceFromBoard
            (leftIndex).getLeftIndex();
    int rightValue = gameManager.getGameBoard().getDominoPieceFromBoard
            (rightIndex).getRightIndex();
    for (int i = 0; i < gameManager.players.get(currentPlayer).getPlayerHand
            ().size(); i++)
    {
      currentDominoPiece = (DominoPiece)gameManager.players.get(currentPlayer)
              .getPlayerHand().get(i);
      if ((currentDominoPiece.getLeftIndex() == leftValue) || (currentDominoPiece
              .getRightIndex() == leftValue) || (currentDominoPiece
              .getLeftIndex() == 0) || (leftValue == 0))
      {
        if ((currentDominoPiece.getLeftIndex() == leftValue)|| (currentDominoPiece
                .getLeftIndex() == 0))
        {
          currentDominoPiece.rotate();
        }

        gameManager.getGameBoard().placeDomino(leftIndex, currentDominoPiece);
        rightIndex++;
        gameManager.players.get(1).getPlayerHand().remove(currentDominoPiece);
        currentDominoPiece = nullDomino;
        switchPlayer();
        foundPlaceForComputer = true;
        break;
      }

      if ((currentDominoPiece.getLeftIndex() == rightValue) || (currentDominoPiece
              .getRightIndex() == rightValue) || (currentDominoPiece
              .getRightIndex() == 0) || (rightValue == 0))
      {
        if ((currentDominoPiece.getRightIndex() == rightValue) ||
                (currentDominoPiece.getRightIndex() == 0))
        {
          currentDominoPiece.rotate();
        }
        gameManager.getGameBoard().placeDomino(rightIndex + 1, currentDominoPiece);
        rightIndex++;
        gameManager.players.get(1).getPlayerHand().remove(currentDominoPiece);
        currentDominoPiece = nullDomino;
        if (gameManager.players.get(1).getPlayerHand().size() == 0)
        {
          gameOver = true;
          disableAllButtons();
        }
        else
        {
          switchPlayer();
        }
        foundPlaceForComputer = true;
        break;
      }
    }

    if (!foundPlaceForComputer)
    {
      gameManager.draw(gameManager.players.get(1));
      computerPlayerTurn();
    }

    foundPlaceForComputer = false;
  }

  /**
   * This method is used to end the game. This disables all buttons and
   * displays the gameover screen with the appropriate winner.
   */
  private void disableAllButtons()
  {
    draw.setDisable(true);
    leftBoard.setDisable(true);
    rightBoard.setDisable(true);
    handPane.setDisable(true);
    dominoTitle.setVisible(false);
    gameOverText = new Text("Game Over!");
    hBoxGameOver.setVisible(true);
    new Alert(Alert.AlertType.INFORMATION, "Player " + currentPlayer + " " +
            "wins!").showAndWait();
  }

  /**
   * This is never used in this program, it simply needs to be implemented.
   *
   * @param event
   */
  public void handle(ActionEvent event)
  {

  }

  /**
   * This is the main method that is the entry point for the entire program.
   *
   * @param args
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}
