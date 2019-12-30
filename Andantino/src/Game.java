public class Game {
    public static final int rowsNum = 19;
    public Board board;
    public Minimax a1;
    public Minimax a2;

    public Game()
    {
        this.board = new Board();
        this.a1 = new Minimax();
        this.a2 = new Minimax();
    }

    public Pair updateGame()
    {
        Pair pair;

        if (board.isWhitesTurn())
            pair = a1.iterativeDeepening(board, 7, Integer.MIN_VALUE, Integer.MAX_VALUE);
        else
            pair = a2.iterativeDeepening(board, 7, Integer.MIN_VALUE, Integer.MAX_VALUE);

        board.makeMove(pair);
        return pair;
    }
    public void restart()
    {
        this.board = new Board();
        this.a1 = new Minimax();
        this.a2 = new Minimax();
    }
}
