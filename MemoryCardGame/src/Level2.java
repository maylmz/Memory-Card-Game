public class Level2 extends Levels {
    @Override
    public int getInitialTries() {
        return 15;
    }

    @Override
    public int getMatchPoints() {
        return 4;
    }

    @Override
    public int getMismatchPenalty() {
        return 2;
    }
}