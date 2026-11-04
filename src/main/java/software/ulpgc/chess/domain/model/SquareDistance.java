package software.ulpgc.chess.domain.model;

public record SquareDistance(int dx, int dy) {

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
}