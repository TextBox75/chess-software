package software.ulpgc.chess.architecture.domain;

import static software.ulpgc.chess.architecture.domain.File.*;
import static software.ulpgc.chess.architecture.domain.Rank.R1;
import static software.ulpgc.chess.architecture.domain.Rank.R8;

public final class CastlingValidator {

    public static boolean isCastlingMove(Piece piece, Square from, Square to) {
        if (!piece.isKing()) return false;
        Square expectedOrigin = piece.isWhite() ? new Square(E, R1) : new Square(E, R8);
        if (!from.equals(expectedOrigin)) return false;

        SquareDistance distance = new SquareDistance(from, to);
        return Math.abs(distance.x()) == 2 && distance.y() == 0;
    }

    public static boolean isCastlingLegal(Position position, Piece king, Square from, Square to) {
        Side side = king.isWhite() ? Side.WHITE : Side.BLACK;
        boolean kingSide = isKingSide(from, to);

        if (!position.state().castlingRights().canCastle(side, kingSide)) return false;

        Piece rook = position.board().pieces().get(rookOrigin(from, to));
        if (rook == null || !rook.isOfSide(side) || !rook.isRook()) return false;

        if (!position.board().isPathClear(king, from, rookOrigin(from, to))) return false;

        Side enemy = side.opposite();
        return !position.board().isSquareAttacked(from, enemy)
                && !position.board().isSquareAttacked(rookTarget(from, to), enemy)
                && !position.board().isSquareAttacked(to, enemy);
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
