import java.util.ArrayList;
import java.util.Random;

public class TranspositionTable {

    public ArrayList<HashEntry> table;

    public TranspositionTable()
    {
        table = new ArrayList<>();
    }

    public static Pair[][] zobristKey = new Pair[Game.rowsNum][];
    static
    {
        Random rand = new Random();

        int j, i;
        int jEnd = 10;

        for(i = 0; i < Game.rowsNum; i++, jEnd = (i<10) ? jEnd + 1: jEnd)
        {
            zobristKey[i] = new Pair[Math.min(GUI.startColNum+ i, Game.rowsNum)];

            for(j = (i<10) ? 0 : i-9; j < jEnd; j++)
            {
                zobristKey[i][j] = new Pair(rand.nextInt(), rand.nextInt());
            }
        }
    }

    public void insert(HashEntry entry)
    {
        HashEntry stored = retrieve(entry.hashValue);

        if (stored == null) table.add(entry);

        else
        {
            if (entry.depth > stored.depth)
            {
                table.remove(table.indexOf(stored));
                table.add(entry);
            }
        }
    }
    public HashEntry retrieve(int hashValue)
    {
        for (HashEntry entry: table)
        {
            if (entry.getHashValue() ==  hashValue)
                return entry;
        }
        return null;
    }
}
