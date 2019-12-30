public class Statistics {
    private static int counterW;
    private static int counterB;

    public static void printStatistics(int number)
    {
        for (int i = 0; i < number; i++)
        {
            Game game = new Game();
            while (!game.board.isGameOver())
                game.updateGame();
            if (game.board.isWhitesTurn())
                counterB++;
            else
                counterW++;
        }
        System.out.println(counterW + " " + counterB);
    }
}
