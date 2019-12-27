
public class MoveAndScore {

    private int score;
    private Pair pair;

    public MoveAndScore()
    {
        pair = new Pair();
    }

    public MoveAndScore(Pair pair, int score)
    {
        this.pair = pair;
        this.score = score;
    }

    public int getScore() {return score;}

    public void setScore(int score) {this.score = score;}

    public Pair getPair() {return pair;}

    public void setPair(Pair pair) {this.pair = pair;}


}