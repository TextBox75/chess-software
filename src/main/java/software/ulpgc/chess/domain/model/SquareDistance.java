package software.ulpgc.chess.domain.model;

public class SquareDistance {
    private final int dx;
    private final int dy;

    public SquareDistance(Square from, Square to) {
        dx = to.file().ordinal() - from.file().ordinal();
        dy = to.rank().ordinal() - from.rank().ordinal();
    }

    public int X() {
        return dx;
    }

    public int Y() {
        return dy;
    }

    public int absX() {
        return Math.abs(dx);
    }

    public int absY() {
        return Math.abs(dy);
    }

    public boolean isOrthogonal() {
        return (dx == 0 && dy != 0) || (dx != 0 && dy == 0);
    }

    public boolean isDiagonal() {
        return absX() == absY() && absX() != 0;
    }

    public boolean isVertical() {
        return absX() == 0 && absY() > 0;
    }
}