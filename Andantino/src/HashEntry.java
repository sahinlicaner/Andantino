
public class HashEntry {


    public int hashValue;
    public Minimax.flag flag;
    public int score;
    public Pair bestMove;
    public int depth;

    public HashEntry(int hashValue, Minimax.flag flag, int score, Pair bestMove, int depth)
    {
        this.hashValue = hashValue;
        this.flag = flag;
        this.score = score;
        this.bestMove = bestMove;
        this.depth = depth;
    }

    public HashEntry(int hashValue, int score, Pair bestMove, int depth)
    {
        this.hashValue = hashValue;
        this.score = score;
        this.bestMove = bestMove;
        this.depth = depth;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }
}
