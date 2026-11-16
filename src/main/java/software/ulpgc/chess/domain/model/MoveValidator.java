package software.ulpgc.chess.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.ulpgc.chess.domain.model.Square.allSquares;

public final class MoveValidator {
    public static Board move(Board board, Square from, Square to) {
        Piece piece = board.pieces().get(from);

        if (piece == null || !isMoveLegal(board, piece, from, to)) return board;

        if (piece.canPromote(to.rank())) {
            piece = piece.defaultPromote();
        }

        return apply(board, piece, from, to);
    }

    public static boolean hasLegalMove(Board board, Side side) {
        List<Square> squares = board.pieces().entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(side))
                .map(Map.Entry::getKey)
                .toList();

        for (Square from : squares) {
            if (hasLegalMove(board, side, from)) return true;
        }
        return false;
    }

    private static boolean hasLegalMove(Board board, Side side, Square from) {
        for (Square to : allSquares()) {
            if (from.equals(to)) continue;

            Board next = move(board, from, to);

            if (!next.equals(board) && !next.isInCheck(side)) return true;
        }
        return false;
    }

    private static boolean isMoveLegal(Board board, Piece piece, Square from, Square to) {
        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            return CastlingValidator.isLegal(board, piece, from, to);
        }

        Piece target = board.pieces().get(to);
        boolean isEnemyTarget = target != null && target.isWhite() != piece.isWhite();

        boolean isKinematicValid = isEnemyTarget
                ? piece.getBehaviour().isAttackValid(from, to)
                : piece.getBehaviour().isMoveValid(from, to);

        return isKinematicValid
                && board.isPathClear(piece, from, to)
                && board.canOccupySquare(piece, to);
    }

    private static Board apply(Board board, Piece piece, Square from, Square to) {
        Map<Square, Piece> newPieces = new HashMap<>(board.pieces());
        newPieces.remove(from);
        newPieces.put(to, piece);

        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            Piece rook = newPieces.remove(CastlingValidator.rookOrigin(from, to));
            newPieces.put(CastlingValidator.rookTarget(from, to), rook);
        }

        return new Board(newPieces, board.castlingRights().update(from, to, piece));
    }
}
