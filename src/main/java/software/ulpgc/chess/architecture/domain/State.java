package software.ulpgc.chess.architecture.domain;

public record State(Side side, CastlingRights castlingRights) {
    public static State initial() {
        return new State(Side.WHITE, CastlingRights.initial());
    }

    public State next(Move move) {
        return new State(this.side.opposite(), this.castlingRights().update(move));
    }
}
