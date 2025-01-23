public class Level3 extends Levels {
    @Override
    public int getInitialTries() {
        return 12;
    }

    @Override
    public int getMatchPoints() {
        return 3;
    }

    @Override
    public int getMismatchPenalty() {
        return 3;
    }
}