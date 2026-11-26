package software.ulpgc.chess.architecture.domain;

import java.util.Optional;

public record State(Side side, CastlingRights castlingRights, Optional<Square> enPassantTarget) {
    public static State initial() {
        return new State(Side.WHITE, CastlingRights.initial(), Optional.empty());
    }

    public State next(Move move) {
        return new State(
                side.opposite(),
                castlingRights.update(move),
                nextEnPassantTarget(move)
        );
    }

    private Optional<Square> nextEnPassantTarget(Move move) {
        if (!move.piece().isPawn()) return Optional.empty();

        SquareDistance distance = new SquareDistance(move.from(), move.to());
        if (distance.absY() != 2) return Optional.empty();

        // La casilla que cruzó el peón: misma columna, fila intermedia
        int middleRank = (move.from().rank().ordinal() + move.to().rank().ordinal()) / 2;
        return Optional.of(Square.of(move.from().file().ordinal(), middleRank));
    }
}
