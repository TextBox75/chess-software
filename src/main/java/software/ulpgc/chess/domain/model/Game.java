package software.ulpgc.chess.domain.model;

public record Game(
        Board board,
        Side side,
        GameStatus status,
        ChessClock clock
) {
    public static Game initial() {
        return new Game(
                Board.initial(),
                Side.WHITE,
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    public Game play(Square from, Square to) {

        if (canGameFinish()) return this;

        return new Game(
                this.board.move(from, to),
                this.side.opposite(),
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    private boolean canGameFinish() {
        Side oppositeSide = this.side.opposite();
        boolean inCheck = this.board.isInCheck(oppositeSide);
        if (inCheck && !this.board.hasLegalMove(oppositeSide)) return false;
        return true;
    }
}
