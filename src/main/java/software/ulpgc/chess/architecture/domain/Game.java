package software.ulpgc.chess.architecture.domain;

import static software.ulpgc.chess.architecture.domain.GameStatus.*;

public record Game(
        Position position,
        GameStatus status,
        ChessClock clock
) {

    public static Game initial() {
        return new Game(
                Position.initial(),
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    public Game play(Square from, Square to) {

        Board nextBoard = MoveValidator.move(this.position().board(), from, to);

        if (nextBoard.equals(this.position.board()) || this.status == CHECKMATE) return this;

        return new Game(
                new Position(
                        MoveValidator.move(this.position.board(), from, to),
                        this.position.state().next(from, to, this.position.board().pieceAt(from))
                ),
                checkGameStatus(nextBoard),
                ChessClock.initial()
        );
    }

    private GameStatus checkGameStatus(Board boardToCheck) {
        Side oppositeSide = this.position.state().side().opposite();
        boolean inCheck = boardToCheck.isInCheck(oppositeSide);

        if (inCheck) {
            if (!MoveValidator.hasLegalMove(this.position.board(), oppositeSide)) return CHECKMATE;
            return CHECK;
        }

        return PLAYING;
    }
}
