package software.ulpgc.chess.architecture.domain;

public enum Side {
    WHITE,
    BLACK;

    public Side opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
