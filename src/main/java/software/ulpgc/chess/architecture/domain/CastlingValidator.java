package software.ulpgc.chess.architecture.domain;

import static software.ulpgc.chess.architecture.domain.File.*;
import static software.ulpgc.chess.architecture.domain.Rank.R1;
import static software.ulpgc.chess.architecture.domain.Rank.R8;

public final class CastlingValidator {

    public static boolean isCastlingMove(Move move) {
        if (!move.piece().isKing()) return false;
        Square expectedOrigin = move.piece().isWhite() ? new Square(E, R1) : new Square(E, R8);
        if (!move.from().equals(expectedOrigin)) return false;

        SquareDistance distance = new SquareDistance(move.from(), move.to());
        return Math.abs(distance.x()) == 2 && distance.y() == 0;
    }

    public static boolean isCastlingLegal(Position position, Move move) {
        Side side = move.piece().isWhite() ? Side.WHITE : Side.BLACK;
        boolean kingSide = isKingSide(move.from(), move.to());

        if (!position.state().castlingRights().canCastle(side, kingSide)) return false;

        Piece rook = position.board().pieces().get(rookOrigin(move.from(), move.to()));
        if (rook == null || !rook.isOfSide(side) || !rook.isRook()) return false;

        Move rookMove = new Move(move.from(), rookOrigin(move.from(), move.to()), rook);

        if (!position.board().isPathClear(rookMove)) return false;

        Side enemy = side.opposite();
        return !position.board().isSquareAttacked(move.from(), enemy)
                && !position.board().isSquareAttacked(rookTarget(move.from(), move.to()), enemy)
                && !position.board().isSquareAttacked(move.to(), enemy);
    }

    public static boolean isKingSide(Square from, Square to) {
        return to.file().ordinal() > from.file().ordinal();
    }

    public static Square rookOrigin(Square from, Square to) {
        return new Square(isKingSide(from, to) ? H : A, from.rank());
    }

    public static Square rookTarget(Square from, Square to) {
        return new Square(isKingSide(from, to) ? F : D, from.rank());
    }
}
