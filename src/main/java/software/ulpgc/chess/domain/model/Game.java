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
                Board.initial(),
                Side.WHITE,
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    public Game play(Square from, Square to) {
        GameStatus currentStatus = checkGameStatus();
        if (currentStatus == FINISHED) return this; // Retorna la misma instancia de game pq el juego ya ha acabado

        // TODO: aquí se deberá comprobar los diferentes game states

        return new Game(
                this.board.move(from, to),
                this.side.opposite(),
                currentStatus,
                ChessClock.initial()
        );
    }

    private GameStatus checkGameStatus() {
        Side oppositeSide = this.side.opposite();
        boolean inCheck = this.board.isInCheck(oppositeSide);

        if (inCheck) {
            if (!this.board.hasLegalMove(oppositeSide)) return FINISHED;
            return CHECK;
        }

        return PLAYING;
    }
}
