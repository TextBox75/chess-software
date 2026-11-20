package software.ulpgc.chess.architecture.domain;

public record State(Side side, CastlingRights castlingRights) {
    public static State initial() {
        return new State(Side.WHITE, CastlingRights.initial());
    }

    public State next(Square from, Square to, Piece movedPiece) {
        return new State(this.side.opposite(), this.castlingRights().update(from, to, movedPiece));
    }
}
