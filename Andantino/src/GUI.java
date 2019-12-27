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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

    public static final double firstHexCorX = 250, firstHexCorY = 50;
    public static final double length = 25, v = Math.sqrt(3) / 2;
    public static final int startColNum = 10;
    public static int timeLimit = 30000;
    private Hexagon[][] tiles = new Hexagon[Game.rowsNum][];

    public void arrayInit(Hexagon[][] tiles, AnchorPane pane)
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

    @Override
    public void start(Stage stage) throws Exception
    {
        AnchorPane pane = new AnchorPane();
        Button minimax = new Button("Minimax");
        pane.getChildren().add(minimax);
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

                if(game.board.isGameOver())
                {
                    minimax.setDisable(true);
                    pane.setOnMouseClicked(null);
                    System.out.println("GAME OVER!");
                }
            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int jEnd = 10;

                for(int i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
                {
                    for(int j = (i<10) ? 0 : i-9; j < jEnd; j++)
                    {
                        if(tiles[i][j].contains(event.getSceneX(), event.getSceneY()) && new Pair(i, j).memberOfaList(game.board.getMoves()))
                        {
                            tiles[i][j].setFill(game.board.isWhitesTurn());
                            game.board.makeMove(new Pair(i, j));
                        }
                    }
                }
                if(game.board.isGameOver())
                {
                    minimax.setDisable(true);
                    pane.setOnMouseClicked(null);
                    System.out.println("GAME OVER!");
                }
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
    /*
    public void createContent(Stage stage)
    {
        Board board = new Board();
        board.setPrefSize(800, 800);
        board.setStyle("-fx-background-color:POWDERBLUE");

        Hexagon[][] tiles = new Hexagon[rowsNum][];
        arrayInit(board, tiles);

        VBox p1 = new VBox();
        p1.setPrefSize(200, 800);
        p1.setStyle("-fx-background-color:POWDERBLUE");
        Label turn = new Label("Turn: ");
        Hexagon hex = new Hexagon(900, 200);
        hex.setFill(Paint.valueOf("#FFFFFF"));
        Button restart = new Button("Restart");
        Button undo = new Button("Undo");

        p1.getChildren().addAll(turn, hex, restart,undo);
        p1.setSpacing(50);

        VBox.setMargin(turn, new Insets(100, 0, 0, 50));
        VBox.setMargin(hex, new Insets(-40, 0, 0, 50));
        VBox.setMargin(restart, new Insets(0, 0, 0, 50));
        VBox.setMargin(undo, new Insets(0, 0, 0, 50));

        HBox root = new HBox();
        root.getChildren().addAll(board, p1);
        Scene scene = new Scene(root, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Ardantino");
        stage.show();

        restart.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                restart(stage);
            }
        });

        undo.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                tiles[board.movesMade.get(board.movesMade.size() - 1).getX()][board.movesMade.get(board.movesMade.size() - 1).getY()].unfill();
                board.undo();
                hex.setFill(board.isWhitesTurn());
            }
        });

        root.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int jEnd = 10;

                for(int i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
                {
                    for(int j = (i<10) ? 0 : i-9; j < jEnd; j++)
                    {
                        if(tiles[i][j].contains(event.getSceneX(), event.getSceneY()) && new Pair(i, j).memberOfaList(board.getMoves()))
                        {
                            tiles[i][j].setFill(board.isWhitesTurn());
                            board.makeMove(new Pair(i, j));
                            hex.setFill(board.isWhitesTurn());

                            if (board.isGameOver())
                            {
                                gameOver(stage);
                            }
                        }
                    }
                }
            }
        });
    }

    public void gameOver(Stage stage)
    {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        Label gameOver = new Label("Game Over!");
        gameOver.setMinSize(400, 400);
        gameOver.setFont(new Font(100));
        Button playHum = new Button("Human");
        Button playAIw = new Button("AI - be white");
        Button playAIb = new Button("AI - be black");

        root.getChildren().addAll(gameOver, playHum, playAIw, playAIb);
        Scene scene = new Scene(root, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Ardantino");
        stage.show();

        playHum.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                restart(stage);
            }
        });

        playAIw.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                restartAI(stage, true);
            }
        });

        playAIb.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                restartAI(stage, false);
            }
        });
    }

    public void createAIContent(Stage stage, boolean colour)
    {
        Minimax ai = new Minimax(colour);
        Board board = new Board();
        board.setPrefSize(800, 800);
        board.setStyle("-fx-background-color:POWDERBLUE");

        Hexagon[][] tiles = new Hexagon[rowsNum][];
        arrayInit(board, tiles);

        VBox p1 = new VBox();
        p1.setPrefSize(200, 800);
        p1.setStyle("-fx-background-color:POWDERBLUE");
        Label turn = new Label("Turn: ");
        Hexagon hex = new Hexagon(900, 200);
        hex.setFill(Paint.valueOf("#FFFFFF"));
        Button restart = new Button("Restart");
        Button minimax = new Button("Minimax");
        Button undo = new Button("Undo");

        p1.getChildren().addAll(turn, hex, restart, minimax, undo);
        p1.setSpacing(50);

        VBox.setMargin(turn, new Insets(100, 0, 0, 50));
        VBox.setMargin(hex, new Insets(-40, 0, 0, 50));
        VBox.setMargin(restart, new Insets(0, 0, 0, 50));
        VBox.setMargin(minimax, new Insets(0, 0, 0, 50));
        VBox.setMargin(undo, new Insets(0, 0, 0, 50));

        HBox root = new HBox();
        root.getChildren().addAll(board, p1);
        Scene scene = new Scene(root, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Ardantino");
        stage.show();

        if (ai.isMyTurn(board))
        {
            Pair pair = ai.iterativeDeepening(board, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
            tiles[pair.getX()][pair.getY()].setFill(board.isWhitesTurn());
            board.makeMove(pair);
            hex.setFill(board.isWhitesTurn());
        }

        restart.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                restartAI(stage, colour);
            }
        });

        undo.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                tiles[board.movesMade.get(board.movesMade.size() - 1).getX()][board.movesMade.get(board.movesMade.size() - 1).getY()].unfill();
                board.undo();
                hex.setFill(board.isWhitesTurn());
            }
        });

        root.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (board.isWhitesTurn() == !colour)
                {
                    int jEnd = 10;

                    for(int i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
                    {
                        for(int j = (i<10) ? 0 : i-9; j < jEnd; j++)
                        {
                            if(tiles[i][j].contains(event.getSceneX(), event.getSceneY()) && new Pair(i, j).memberOfaList(board.getMoves()))
                            {
                                tiles[i][j].setFill(board.isWhitesTurn());
                                board.makeMove(new Pair(i, j));
                                hex.setFill(board.isWhitesTurn());

                                if (board.isGameOver())
                                {
                                    gameOver(stage);
                                }

                            }
                        }
                    }
                    Pair pair = ai.iterativeDeepening(board, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    tiles[pair.getX()][pair.getY()].setFill(board.isWhitesTurn());
                    board.makeMove(pair);
                    hex.setFill(board.isWhitesTurn());

                    if (board.isGameOver())
                    {
                        gameOver(stage);
                    }
                }
            }
        });
    }

    public void restart(Stage stage) {createContent(stage);}

    public void restartAI(Stage stage, boolean colour) {createAIContent(stage, colour);}

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //createContent(arg0);
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        Button againstFriend = new Button("Click here if you want to have some fun with your friend.");
        Button againstAI = new Button("No, you are sick of humans. Lets play against AI.");

        root.getChildren().addAll(againstFriend, againstAI);
        Scene scene = new Scene(root, 1000, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ardantino");
        primaryStage.show();

        againstFriend.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                createContent(primaryStage);
            }
        });

        againstAI.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Label colour = new Label("Do you want to make the first move?");
                colour.setMinSize(100, 100);
                colour.setFont(new Font(50));
                Button white = new Button("Hell yea");
                Button black = new Button("Let the AI lead the way");

                VBox root = new VBox();
                root.setAlignment(Pos.CENTER);
                root.getChildren().addAll(colour, white, black);
                Scene scene = new Scene(root, 1000, 800);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Ardantino");
                primaryStage.show();

                white.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event)
                    {
                        createAIContent(primaryStage, false);
                    }
                });

                black.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event)
                    {
                        createAIContent(primaryStage, true);
                    }
                });
            }
        });
    }

    public void arrayInit(Board board, Hexagon[][] tiles)
    {
        int j, i;
        int jEnd = 10;

        for(i = 0; i < rowsNum; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            tiles[i] = new Hexagon[Math.min(firstRow_ColNum + i, rowsNum)];

            for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                tiles[i][j] = new Hexagon(firstHexCorX - length * v * i + j * 2 * v * length, firstHexCorY + 1.5 * length * i);
                board.getChildren().add(tiles[i][j]);
            }
        }
        tiles[9][(int) 'J' - (int) 'A'].setFill(false);
    }

    public static void main(String[] args) {
        com.sun.javafx.runtime.VersionInfo.getRuntimeVersion();
        //launch(args);
    }
}

     */