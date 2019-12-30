import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

    public static final double firstHexCorX = 250, firstHexCorY = 50;
    public static final double length = 25, v = Math.sqrt(3) / 2;
    public static final int startColNum = 10;
    public static long timeLimit = 10*1000;
    private Hexagon[][] tiles = new Hexagon[Game.rowsNum][];

    public void arrayInit(Hexagon[][] tiles, BorderPane pane)
    {
        int j, i;
        int jEnd = 10;

        for(i = 0; i < Game.rowsNum; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            tiles[i] = new Hexagon[Math.min(startColNum + i, Game.rowsNum)];

            for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                tiles[i][j] = new Hexagon(firstHexCorX - length * v * i + j * 2 * v * length, firstHexCorY + 1.5 * length * i);
                pane.getChildren().add(tiles[i][j]);
            }
        }
        tiles[9][(int) 'J' - (int) 'A'].setFill(false);
    }

    public void arrayRestart(Hexagon[][] tiles)
    {
        int j, i;
        int jEnd = 10;

        for(i = 0; i < Game.rowsNum; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                tiles[i][j].unfill();
            }
        }
        tiles[9][(int) 'J' - (int) 'A'].setFill(false);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane pane = new BorderPane();
        VBox box = new VBox();
        HBox box2 = new HBox();

        Button minimax = new Button("Minimax");
        Button undo = new Button("Undo");
        Button restart = new Button("Restart");
        Button moves = new Button("Moves");
        Label turn = new Label("Turn:");
        Hexagon hex = new Hexagon(50,50);
        hex.setFill(true);

        Label gameOver = new Label("GAME OVER!");
        gameOver.setFont(new Font(100));
        gameOver.setPrefSize(800,500);
        gameOver.setTextFill(Paint.valueOf("#20B2AA"));
        gameOver.setAlignment(Pos.CENTER);

        minimax.setPrefSize(100, 50);
        undo.setPrefSize(100, 50);
        restart.setPrefSize(100, 50);
        turn.setPrefSize(50, 50);

        box2.getChildren().addAll(turn, hex);
        box.getChildren().addAll(minimax, undo, restart, moves, box2);
        pane.setRight(box);

        pane.setStyle("-fx-background-color:POWDERBLUE");
        arrayInit(tiles, pane);

        Scene scene = new Scene(pane, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Ardantino");
        stage.show();

        Game game = new Game();

        minimax.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Pair pair = game.updateGame();
                tiles[pair.getX()][pair.getY()].setFill(!game.board.isWhitesTurn());
                hex.setFill(game.board.isWhitesTurn());

                if(game.board.isGameOver())
                {
                    pane.setCenter(gameOver);
                }
            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int jEnd = 10;

                outerloop:
                for(int i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
                {
                    for(int j = (i<10) ? 0 : i-9; j < jEnd; j++)
                    {
                        if(tiles[i][j].contains(event.getSceneX(), event.getSceneY()) && new Pair(i, j).memberOfaList(game.board.getMoves()))
                        {
                            tiles[i][j].setFill(game.board.isWhitesTurn());
                            game.board.makeMove(new Pair(i, j));
                            hex.setFill(game.board.isWhitesTurn());
                            break outerloop;
                        }
                    }
                }
                if(game.board.isGameOver())
                {
                    pane.setCenter(gameOver);
                }
            }
        });

        undo.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle (ActionEvent event)
            {
                if (game.board.isGameOver())
                {
                    pane.getChildren().remove(gameOver);
                }
                int num = game.board.movesMade.size()-1;
                Pair undoMove = game.board.movesMade.get(num);
                tiles[undoMove.getX()][undoMove.getY()].unfill();
                game.board.undo();
                hex.setFill(game.board.isWhitesTurn());
            }
        });

        restart.setOnAction(new EventHandler<ActionEvent>()
         {
             @Override
             public void handle (ActionEvent event)
             {
                 if (game.board.isGameOver())
                     pane.getChildren().remove(gameOver);

                 arrayRestart(tiles);
                 game.restart();
                 hex.setFill(game.board.isWhitesTurn());
             }
         });

        moves.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle (ActionEvent event)
            {
                for (Pair pair: game.board.movesMade)
                {
                    System.out.println(pair.getX() + " " + pair.getY());
                }
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
        //Statistics.printStatistics(50);
        System.exit(0);
    }
}