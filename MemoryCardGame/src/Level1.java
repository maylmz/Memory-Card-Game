public class Level1 extends Levels {
    @Override
    public int getInitialTries() {
        return 18;
    }

    @Override
    public int getMatchPoints() {
        return 5;
    }

    @Override
    public int getMismatchPenalty() {
        return 1;
    }
}