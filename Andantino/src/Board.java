import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board
{
    public int[][] tiles;
    public ArrayList<Pair> movesMade;
    private ArrayList<ArrayList<Pair>> whiteGroup, blackGroup;
    private ArrayList<Pair> whites, blacks;
    private int hashValue;
    private int turn;
    private boolean noMoves;

    public Board()
    {
        turn = 0;
        hashValue = 0;
        noMoves = false;
        tiles = new int[Game.rowsNum][];
        movesMade = new ArrayList<Pair>();
        whiteGroup = new ArrayList<ArrayList<Pair>>();
        blackGroup = new ArrayList<ArrayList<Pair>>();

        int jEnd = 10;
        for(int i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            tiles[i] = new int[Math.min(GUI.startColNum + i, Game.rowsNum)];

            for(int j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                tiles[i][j] = 10;
            }
        }
        makeMove(new Pair(9, (int) 'J' - (int) 'A'));
    }

    public void calculateHashValue()
    {
        int j, i, result = 0;
        int jEnd = 10;

        for(i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                if (tiles[i][j] == 1)
                {
                    result ^= TranspositionTable.zobristKey[i][j].getX();
                }
                else if (tiles[i][j] == -1)
                {
                    result ^= TranspositionTable.zobristKey[i][j].getY();
                }
            }
        }
        hashValue = result;
    }

    public ArrayList<Pair> getMoves()
    {
        ArrayList<Pair> list = new ArrayList<>();

        if (turn == 1) list = getNeighbors(new Pair(9, (int) 'J' - (int) 'A'), 0);

        else if (!this.isGameOver())
        {
            int j, i;
            int jEnd = 10;

            for(i = 0; i < 19; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
            {
                for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
                {
                    if (tiles[i][j] == 10 && getNeighbors(new Pair(i, j), 1).size() > 1)
                    {
                        list.add(new Pair(i ,j));
                    }
                }
            }
            if (list.isEmpty()) noMoves = true;
        }
        return list;
    }

    public ArrayList<Pair> getNeighbors(Pair pair, int var)
    {
        ArrayList<Pair> list = new ArrayList<>();
        int [] arr = {0, -1, 0, 1, -1, 0, -1, -1, 1, 0, 1, 1};

        for(int i = 0; i<arr.length; i = i + 2)
        {
            try
            {
                switch(var)
                {
                    case 0:
                        if (tiles[pair.getX() + arr[i]][pair.getY() + arr[i + 1]] != 0)
                            list.add(new Pair(pair.getX() + arr[i], pair.getY() + arr[i + 1]));
                        break;
                    case 1:
                        if (tiles[pair.getX() + arr[i]][pair.getY() + arr[i + 1]] == 1 || tiles[pair.getX() + arr[i]][pair.getY() + arr[i + 1]] == -1)
                            list.add(new Pair(pair.getX() + arr[i], pair.getY() + arr[i + 1]));
                        break;
                    case 2:
                        if (tiles[pair.getX() + arr[i]][pair.getY() + arr[i + 1]] == tiles[pair.getX()][pair.getY()])
                            list.add(new Pair(pair.getX() + arr[i], pair.getY() + arr[i + 1]));
                }
            } catch(Exception e) {}
        }
        return list;
    }

    public void makeMove(Pair pair)
    {
        if (tiles[pair.getX()][pair.getY()] == 10)
        {
            if(!isWhitesTurn())
            {
                tiles[pair.getX()][pair.getY()] = -1;
                AddHexToGroup(pair, blackGroup);
                hashValue ^= TranspositionTable.zobristKey[pair.getX()][pair.getY()].getY();
            }
            else
            {
                tiles[pair.getX()][pair.getY()] = 1;
                AddHexToGroup(pair, whiteGroup);
                hashValue ^= TranspositionTable.zobristKey[pair.getX()][pair.getY()].getX();
            }

            if(this.turn > 0) movesMade.add(pair);
            nextTurn();
        }
    }

    public int getHashValue() {
        return hashValue;
    }

    public void AddHexToGroup(Pair pair, ArrayList<ArrayList<Pair>> lists)
    {
        ArrayList<Integer> x = RelevantLists(lists, pair);

        if (x.isEmpty()) {lists.add(new ArrayList<Pair>()); lists.get(lists.size() - 1).add(pair);}

        else if (x.size() == 1) {lists.get(x.get(0)).add(pair);}

        else if(x.size() > 1)
        {
            for (int i = 1; i < x.size(); i++)
            {
                lists.get(x.get(0)).add(pair);
                lists.get(x.get(0)).removeAll(lists.get(x.get(i)));
                lists.get(x.get(0)).addAll(lists.get(x.get(i)));
                lists.remove(lists.get(x.get(i)));

                if (i + 1 != x.size())
                {
                    x.set(i+1, x.get(i+1)-1);
                }
            }
        }
    }

    public ArrayList<Integer> RelevantLists(ArrayList<ArrayList<Pair>> lists, Pair pair)
    {
        ArrayList<Integer> returnList = new ArrayList<>();

        for(ArrayList<Pair> list : lists)
        {
            for (Pair pair2 : getNeighbors(pair, 2))
            {
                if(pair2.memberOfaList(list)) {
                    returnList.add(lists.lastIndexOf(list)); break;}
            }
        }
        return returnList;
    }

    public void updateStreakArray(int streak, int[] arr, Pair pair)
    {
        switch(streak)
        {
            case 2:
                if (tiles[pair.getX()][pair.getY()] == 1) arr[0]++;
                else arr[3]++;
                break;
            case 3:
                if (tiles[pair.getX()][pair.getY()] == 1) arr[1]++;
                else arr[4]++;
                break;
            case 4:
                if (tiles[pair.getX()][pair.getY()] == 1) arr[2]++;
                else arr[5]++;
                break;
        }
        if (streak > 4) arr[6] = 1;
    }

    public int[] checkStreak()
    {
        int[] arr = new int[7];

        int i, j;
        int end = 10, streak = 0;
        int tempI = 0, tempJ = 0;

        for(i = 0; i < Game.rowsNum; i++, end = (i<10) ? end + 1: end)
        {
            for(j = (i<10) ? 0 : i-9; j < end; j++)
            {
                if (tiles[i][j] == 10)
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 0; continue;
                }

                else if(streak == 0) {streak++; tempI = i; tempJ = j;}

                else if(tiles[i][j] != tiles[tempI][tempJ])
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 1; tempI = i; tempJ = j; continue;
                }
                else if(tiles[i][j] == tiles[tempI][tempJ]) {streak++;}
            }
            if (streak == 5) {arr[6] = 1; return arr;}
            if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));

            streak = 0;
        }
        end = 10;

        for(j = 0; j < Game.rowsNum; j++, end = (j<10) ? end + 1: end)
        {
            for(i = (j<10) ? 0 : j-9; i < end; i++)
            {
                if (tiles[i][j] == 10)
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 0; continue;
                }

                else if(streak == 0) {streak++; tempI = i; tempJ = j;}

                else if(tiles[i][j] != tiles[tempI][tempJ])
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 1; tempI = i; tempJ = j; continue;
                }
                else if(tiles[i][j] == tiles[tempI][tempJ]) {streak++;}
            }
            if (streak == 5) {arr[6] = 1; return arr;}
            if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));

            streak = 0;
        }

        for(int count = 0; count < Game.rowsNum; count++)
        {
            for(i = (count < 10) ? 9 - count : 0, j = (count >= 10) ? count - 10 + 1 : 0; i < 19 && j < (int) 'S' - (int) 'A' + 1; i++, j++)
            {
                if (tiles[i][j] == 10)
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 0; continue;
                }

                else if(streak == 0) {streak++; tempI = i; tempJ = j;}

                else if(tiles[i][j] != tiles[tempI][tempJ])
                {
                    if (streak == 5) {arr[6] = 1; return arr;}
                    if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));
                    streak = 1; tempI = i; tempJ = j; continue;
                }
                else if(tiles[i][j] == tiles[tempI][tempJ]) {streak++;}
            }
            if (streak == 5) {arr[6] = 1; return arr;}
            if (streak > 1) updateStreakArray(streak, arr, new Pair(tempI, tempJ));

            streak = 0;
        }
        return arr;
    }

    public boolean isGameOver()
    {
        int[] arr = checkStreak();
        if (arr[6] == 1) return true;
        if (this.noMoves) return true;

        for (ArrayList<Pair> list : whiteGroup)
        {
            if (!list.isEmpty())
            {
                outerLoop:
                for (Pair pair : list)
                {
                    if (getNeighbors(pair, 0).size() < 6) break outerLoop;

                    for (Pair pair2 : getNeighbors(pair, 0))
                    {
                        if (tiles[pair2.getX()][pair2.getY()] == 10) break outerLoop;
                    }
                    if (pair == list.get(list.size() - 1)) return true;
                }
            }
        }

        for (ArrayList<Pair> list : blackGroup)
        {
            if (!list.isEmpty())
            {
                outerLoop:
                for (Pair pair : list)
                {
                    if (getNeighbors(pair, 0).size() < 6) {break outerLoop;}

                    for (Pair pair2 : getNeighbors(pair, 0))
                    {
                        if (tiles[pair2.getX()][pair2.getY()] == 10) break outerLoop;
                    }
                    if (pair == list.get(list.size() - 1)) return true;
                }
            }
        }
        return false;
    }

    public int getTurn() {return turn;}

    public void nextTurn() {this.turn++;;}

    public void previousTurn() {this.turn--;}

    public boolean isWhitesTurn()
    {
        if (this.turn % 2 == 1) return true;
        else return false;
    }

    public Board copyBoard()
    {
        Board board = new Board();

        for(Pair pair : movesMade) {board.makeMove(pair);}
        return board;
    }

    public int evaluate()
    {
        if (isGameOver()) return -300000;

        int score = -10 + (int)(Math.random() * ((10 - (-10)) + 1));
        int[] arr = checkStreak();
        score += 10*(arr[0] - arr[3]) + 50*(arr[1] - arr[4]) + 100*(arr[2] - arr[5]);// + 500*((blackGroup.size() - whiteGroup.size()));
        
        if (!isWhitesTurn()) return -score;
        else 				return score;
    }

    public void undo()
    {
        Board board = new Board();

        for (int i = 0; i < this.movesMade.size() - 1; i++)
        {
            board.makeMove(this.movesMade.get(i));
        }

        this.blackGroup = board.blackGroup;
        this.whiteGroup = board.whiteGroup;
        this.hashValue = board.hashValue;

        this.tiles[movesMade.get(movesMade.size() - 1).getX()][movesMade.get(movesMade.size() - 1).getY()] = 10;
        previousTurn();
        this.noMoves = false;
        movesMade.remove(movesMade.size() - 1);
    }
}
