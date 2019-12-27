import java.util.ArrayList;

public class Pair {

    private int x, y;

    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Pair(){}

    public int getX() {return x;}

    public void setX(int x) {this.x = x;}

    public int getY() {return y;}

    public void setY(int y) {this.y = y;}

    public boolean memberOfaList (ArrayList<Pair> pairArray)
    {
        for (Pair pair: pairArray)
        {
            if (this.x == pair.getX() && this.y == pair.getY()) return true;
        }
        return false;
    }
}