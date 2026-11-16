package software.ulpgc.chess.domain.model;

import static software.ulpgc.chess.domain.model.File.*;
import static software.ulpgc.chess.domain.model.File.D;
import static software.ulpgc.chess.domain.model.File.F;
import static software.ulpgc.chess.domain.model.Rank.R1;
import static software.ulpgc.chess.domain.model.Rank.R8;

public class CastlingValidator {
    public static boolean isCastlingMove(Piece piece, Square from, Square to) {
        if (!piece.isKing()) return false;
        Square expectedOrigin = piece.isWhite() ? new Square(E, R1) : new Square(E, R8);
        if (!from.equals(expectedOrigin)) return false;

        SquareDistance distance = new SquareDistance(from, to);
        return Math.abs(distance.X()) == 2 && distance.Y() == 0;
    }

    public static boolean isCastlingLegal(Board board, Piece king, Square from, Square to) {
        Side side = king.isWhite() ? Side.WHITE : Side.BLACK;
        boolean isKingSide = to.file().ordinal() > from.file().ordinal(); // true = corto (hacia H), false = largo (hacia A)

        if (!board.castlingRights().canCastle(side, isKingSide)) return false;

        // 1. Verificar que la torre aliada está en su esquina
        Square rookSquare = new Square(isKingSide ? H : A, from.rank());
        Piece rook = board.pieces().get(rookSquare);
        if (rook == null || !rook.isOfSide(side) || !rook.isRook()) return false;

        // 2. Verificar que no hay piezas entre el rey y la torre
        if (!board.isPathClear(king, from, rookSquare)) return false;

        // 3. Verificar que ni el origen, ni el paso intermedio, ni el destino están atacados
        Side enemy = side.opposite();
        Square passingSquare = new Square(isKingSide ? F : D, from.rank());

        return !board.isSquareAttacked(from, enemy)
                && !board.isSquareAttacked(passingSquare, enemy)
                && !board.isSquareAttacked(to, enemy);
    }
}
