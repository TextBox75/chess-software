package software.ulpgc.chess.architecture.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static software.ulpgc.chess.architecture.domain.GameStatus.*;

public record Game(
        Position position,
        GameStatus status,
        ChessClock clock,
        List<Move> moveHistory
) {

    public static Game initial() {
        return new Game(
                Position.initial(),
                GameStatus.PLAYING,
                ChessClock.initial(),
                new ArrayList<>()
        );
    }

    public Game play(Square from, Square to) {

        Move move = new Move(from, to, position.board().pieceAt(from));

        Position nextPosition = MoveValidator.move(this.position, move, getLastMove());

        if (nextPosition.board().equals(this.position.board()) || this.status == CHECKMATE) return this;

        return new Game(
                nextPosition,
                checkGameStatus(nextPosition),
                ChessClock.initial(),
                Stream.concat(this.moveHistory.stream(), Stream.of(move)).toList()
        );
    }

    private GameStatus checkGameStatus(Position position) {
        Side oppositeSide = this.position.state().side().opposite();
        boolean inCheck = position.board().isInCheck(oppositeSide);

        if (inCheck) {
            if (!MoveValidator.hasLegalMove(this.position, oppositeSide, getLastMove())) return CHECKMATE;
            return CHECK;
        }

        return PLAYING;
    }

    private Move getLastMove() {
        return this.moveHistory.getLast();
    }
}
