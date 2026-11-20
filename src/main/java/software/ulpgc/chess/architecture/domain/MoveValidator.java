package software.ulpgc.chess.architecture.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.ulpgc.chess.architecture.domain.Square.allSquares;

public final class MoveValidator {
    public static MoveResult move(Position position, Square from, Square to) {
        Piece piece = position.board().pieceAt(from);

        if (piece == null || !isMoveLegal(position, piece, from, to)) return new MoveResult(position, null);

        if (piece.canPromote(to.rank())) {
            piece = piece.defaultPromote();
        }

        return apply(position, piece, from, to);
    }

    public static boolean hasLegalMove(Position position, Side side) {
        List<Square> squares = position.board().pieces().entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(side))
                .map(Map.Entry::getKey)
                .toList();

        for (Square from : squares) {
            if (hasLegalMove(position, side, from)) return true;
        }
        return false;
    }

    private static boolean hasLegalMove(Position position, Side side, Square from) {
        for (Square to : allSquares()) {
            if (from.equals(to)) continue;

            Position next = MoveValidator.move(position, from, to).position();

            if (!next.board().equals(position.board()) && !next.board().isInCheck(side)) return true;
        }
        return false;
    }

    private static boolean isMoveLegal(Position position, Piece piece, Square from, Square to) {
        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            return CastlingValidator.isCastlingLegal(position, piece, from, to);
        } else if (EnPassantValidator.isEnPassantMove(position, from, to)) {
            return EnPassantValidator.isEnPassantLegal(position, from, to, );
        }

        Piece target = position.board().pieces().get(to);
        boolean isEnemyTarget = target != null && target.isWhite() != piece.isWhite();

        boolean isKinematicValid = isEnemyTarget
                ? piece.getBehaviour().isAttackValid(from, to)
                : piece.getBehaviour().isMoveValid(from, to);

        return isKinematicValid
                && position.board().isPathClear(piece, from, to)
                && position.board().canOccupySquare(piece, to);
    }

    private static MoveResult apply(Position position, Piece piece, Square from, Square to) {
        Map<Square, Piece> newPieces = new HashMap<>(position.board().pieces());
        newPieces.remove(from);
        newPieces.put(to, piece);

        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            Piece rook = newPieces.remove(CastlingValidator.rookOrigin(from, to));
            newPieces.put(CastlingValidator.rookTarget(from, to), rook);
        } else if (EnPassantValidator.isEnPassantMove(position, from, to)) {

        }

        Position nextPositon = new Position(new Board(newPieces), position.state().next(from, to, piece));
        Move move = new Move(from, to, piece);

        return new MoveResult(nextPositon, move);
    }
}