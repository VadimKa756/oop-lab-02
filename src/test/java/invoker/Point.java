package invoker;

/** Fixture with no no-args constructor, used to verify recursive constructor-based argument synthesis. */
final class Point {

    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }
}