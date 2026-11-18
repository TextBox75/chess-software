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
        return Math.abs(distance.X()) == 2 && distance.Y() == 0;
    }

    public static boolean isLegal(Board board, Piece king, Square from, Square to) {
        Side side = king.isWhite() ? Side.WHITE : Side.BLACK;
        boolean kingSide = isKingSide(from, to);

        if (!board.castlingRights().canCastle(side, kingSide)) return false;

        // 1. La torre aliada está en su esquina
        Piece rook = board.pieces().get(rookOrigin(from, to));
        if (rook == null || !rook.isOfSide(side) || !rook.isRook()) return false;

        // 2. No hay piezas entre el rey y la torre
        if (!board.isPathClear(king, from, rookOrigin(from, to))) return false;

        // 3. Ni el origen, ni el paso intermedio, ni el destino están atacados
        Side enemy = side.opposite();
        return !board.isSquareAttacked(from, enemy)
                && !board.isSquareAttacked(rookTarget(from, to), enemy)
                && !board.isSquareAttacked(to, enemy);
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
