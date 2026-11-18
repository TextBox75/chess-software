package software.ulpgc.chess.architecture.domain;

public record State(CastlingRights rights, Side side) {
    public static State initial() {
        return new State(CastlingRights.initial(), Side.WHITE);
    }

    public State next(Square from, Square to, Piece movedPiece) {
        return new State(this.rights.update(from, to, movedPiece), this.side.opposite());
    }
}
