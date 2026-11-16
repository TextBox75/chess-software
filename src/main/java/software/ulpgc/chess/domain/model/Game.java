package software.ulpgc.chess.domain.model;

import static software.ulpgc.chess.domain.model.GameStatus.*;

public record Game(
        Board board,
        Side side,
        GameStatus status,
        ChessClock clock
) {

    public static Game initial() {
        return new Game(
                StandardBoardFactory.createBoard(),
                Side.WHITE,
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    public Game play(Square from, Square to) {

        Board nextBoard = MoveValidator.move(this.board, from, to);

        if (nextBoard.equals(this.board) || this.status == CHECKMATE) return this;

        return new Game(
                nextBoard,
                this.side.opposite(),
                checkGameStatus(nextBoard),
                ChessClock.initial()
        );
    }

    private GameStatus checkGameStatus(Board boardToCheck) {
        Side oppositeSide = this.side.opposite();
        boolean inCheck = boardToCheck.isInCheck(oppositeSide);

        if (inCheck) {
            if (!MoveValidator.hasLegalMove(board, oppositeSide)) return CHECKMATE;
            return CHECK;
        }

        return PLAYING;
    }
}
