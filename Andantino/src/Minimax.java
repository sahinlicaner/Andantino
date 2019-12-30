import java.util.ArrayList;
import java.util.Collections;

public class Minimax extends Player
{
    public static enum flag
    {
        ACCURATE,
        FAIL_HIGH,
        FAIL_LOW
    }

    public TranspositionTable table;

    public Minimax()
    {
        this.table = new TranspositionTable();
    }

    public MoveAndScore abNegamax(Board board, int maxDepth, int currentDepth, int alpha, int beta)
    {
        MoveAndScore output = new MoveAndScore();
        int olda = alpha;

        HashEntry entry =  table.retrieve(board.getHashValue());

        if (entry != null && entry.depth >= maxDepth)
        {
            if (entry.flag == flag.ACCURATE)
            {
                output.setScore(entry.score);
                output.setPair(entry.bestMove);
                return output;
            }
            else if (entry.flag == flag.FAIL_LOW)
            {
                alpha = Math.max(alpha, entry.score);
            }
            else if (entry.flag == flag.FAIL_HIGH)
            {
                beta = Math.min(beta, entry.score);
            }
            if (alpha >= beta)
            {
                output.setScore(entry.score);
                output.setPair(entry.bestMove);
                return output;
            }
        }

        if(maxDepth == currentDepth || board.isGameOver()) {output.setScore(board.evaluate()); return output;}

        Pair bestMove = new Pair();
        int bestScore = alpha;
        int currentScore;

        //ArrayList<Pair> successors = sortMoves(board);

        for (Pair pair: board.getMoves()) // successors
        {
            Board newBoard = board.copyBoard();
            newBoard.makeMove(pair);

            output = abNegamax(newBoard, maxDepth, currentDepth + 1, -beta, -alpha);
            currentScore = output.getScore() * -1;

            if (currentScore > bestScore)
            {
                bestScore = currentScore;
                bestMove.setX(pair.getX());
                bestMove.setY(pair.getY());
            }
            if (bestScore >= alpha) {alpha = bestScore;}
            if (bestScore >= beta) {break;}
        }

        HashEntry entry2 = new HashEntry(board.getHashValue(), bestScore, bestMove, maxDepth);

        if      (bestScore <= olda) entry2.flag = flag.FAIL_LOW;
        else if (bestScore >= beta) entry2.flag = flag.FAIL_HIGH;
        else 						entry2.flag = flag.ACCURATE;

        table.insert(entry2);

        return new MoveAndScore(bestMove, bestScore);
    }

    public Pair iterativeDeepening(Board board, int maxDepth, int alpha, int beta)
    {
        long start_time = System.currentTimeMillis();
        MoveAndScore output = new MoveAndScore();

        for (int i = 2; GUI.timeLimit > System.currentTimeMillis() - start_time && i < maxDepth; i++)
        {
            output = abNegamax(board, i, 0, alpha, beta);
        }
        if (GUI.timeLimit < System.currentTimeMillis() - start_time)
            System.out.println("Time limit reached.");

        return output.getPair();
    }

    public ArrayList<Pair> sortMoves(Board board)
    {
        ArrayList<Pair> successors = board.getMoves();
        ArrayList<Integer> storedValue = new ArrayList<>();

        for (Pair pair: successors)
        {
            Board newBoard = board.copyBoard();
            newBoard.makeMove(pair);

            HashEntry entry = table.retrieve(newBoard.getHashValue());

            if (entry != null) storedValue.add(entry.score);
            else 			   storedValue.add(Integer.MIN_VALUE);
        }

        for (int i = 0; i < successors.size() - 1; i++)
        {
            for (int j = i + 1; j < successors.size(); j++)
            {
                if (storedValue.get(j) > storedValue.get(i))
                {
                    Collections.swap(successors , i, j);
                    Collections.swap(storedValue, i, j);
                }
            }
        }
        return successors;
    }
}
