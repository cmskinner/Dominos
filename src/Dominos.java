import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
 * Created by chris on 11-Sep-17.
 */
public class Dominos extends Application implements EventHandler<ActionEvent>
{
  private Text dominoTitle = new Text("WELCOME TO DOMINOS!");

  private Button draw, rotateButton, leftBoard, rightBoard;
  GameManager gameManager = new GameManager(2, 7);

  private static final int WINDOW_WIDTH = 1200;
  private static final int WINDOW_HEIGHT = 400;
  private static final int DOMINO_WIDTH = 133;
  private static final int DOMINO_HEIGHT = 67;

  BorderPane handPane, boardPane;

  private DominoPiece nullDomino = new DominoPiece(100,100);
  private DominoPiece currentDominoPiece = nullDomino;

  private int round = 0;
  private int leftIndex, rightIndex = 0;

  private Canvas canvas;

  public void start(Stage stage)
  {
    dominoTitle.setFont(Font.font("Verdana", 30));

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
        System.out.println(gameManager.players.get(0).getPlayerHand().get(index));
      }
    });

    HBox hBoxBoard = boardRedraw();

    boardPane = new BorderPane();
    boardPane.setCenter(hBoxBoard);
    boardPane.setOnMouseClicked(event -> {
      HBox tempHBox = boardRedraw();
      boardPane.setCenter(tempHBox);
    });

    draw.setOnAction(event -> {
      gameManager.draw(gameManager.players.get(gameManager.getCurrentPlayer()));
      HBox tempHBox = playersHand();
      handPane.setCenter(tempHBox);
    });


    leftBoard.setOnAction(event -> {

      if (round == 0)
      {
        gameManager.getGameBoard().placeDomino(0, currentDominoPiece);
        gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        round++;
      }
      else
      {
        int leftValue = gameManager.getGameBoard().getDominoPieceFromBoard(leftIndex)
                .getLeftIndex();
        System.out.println((currentDominoPiece.getLeftIndex() == leftValue) || (currentDominoPiece
                .getRightIndex() == leftValue) || (currentDominoPiece
                .getLeftIndex() == 0) || (leftValue == 0));
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
          gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        }
      }
      HBox tempHBox = boardRedraw();
      boardPane.setCenter(tempHBox);
      gameManager.getGameBoard().printBoard();
      HBox tempHBox2 = playersHand();
      handPane.setCenter(tempHBox2);


    });

    rightBoard.setOnAction(event -> {

      if (round == 0)
      {
        gameManager.getGameBoard().placeDomino(0, currentDominoPiece);
        gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        round++;
      }
      else
      {
        int rightValue = gameManager.getGameBoard().getDominoPieceFromBoard(rightIndex)
                .getRightIndex();
        System.out.println((currentDominoPiece.getLeftIndex() == rightValue) || (currentDominoPiece
                .getRightIndex() == rightValue) || (currentDominoPiece
                .getRightIndex() == 0) || (rightValue == 0));
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
          gameManager.players.get(0).getPlayerHand().remove(currentDominoPiece);
        }
      }
      HBox tempHBox = boardRedraw();
      boardPane.setCenter(tempHBox);
      gameManager.getGameBoard().printBoard();
      HBox tempHBox2 = playersHand();
      handPane.setCenter(tempHBox2);

    });


    vBox.getChildren().addAll(hBoxTitle, hBoxButtons, boardPane, hBoxLeftRight,
            handPane);

    Scene scene = new Scene(vBox, WINDOW_WIDTH, WINDOW_HEIGHT);

    stage.setScene(scene);
    stage.show();

  }

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

  private HBox playersHand()
  {
    ArrayList<ImageView> imageViews = rowToBeDisplayed(gameManager.players
            .get(0).getPlayerHand());
    HBox hbox = new HBox();
    for (int i = 0; i < imageViews.size(); i++)
    {
      hbox.getChildren().add(imageViews.get(i));
    }
    return hbox;
  }

  private HBox boardRedraw()
  {
    ArrayList<ImageView> imageViews = rowToBeDisplayed(gameManager
            .getGameBoard().getBoard());
    HBox hbox = new HBox();
    for (int i = 0; i < imageViews.size(); i++)
    {
      hbox.getChildren().add(imageViews.get(i));
    }
    return hbox;
  }

  private void draw()
  {

  }

  private void play()
  {

  }

  public void handle(ActionEvent event)
  {

  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
