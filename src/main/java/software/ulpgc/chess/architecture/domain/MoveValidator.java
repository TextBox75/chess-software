package software.ulpgc.chess.architecture.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.ulpgc.chess.architecture.domain.Square.allSquares;

public final class MoveValidator {
    public static Position move(Position position, Move move, Move lastMove) {
        if (move.piece() == null || !isMoveLegal(position, move, lastMove))
            return position;

        return apply(position, move);
    }

    public static boolean hasLegalMove(Position position, Side side, Move lastMove) {
        List<Square> squares = position.board().pieces().entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(side))
                .map(Map.Entry::getKey)
                .toList();

        for (Square from : squares) {
            if (hasLegalMove(position, side, from, lastMove)) return true;
        }
        return false;
    }

    private static boolean hasLegalMove(Position position, Side side, Square from, Move lastMove) {
        for (Square to : allSquares()) {
            if (from.equals(to)) continue;

            Move move = new Move(from, to, position.board().pieceAt(from));
            Position next = MoveValidator.move(position, move, lastMove);

            if (!next.board().equals(position.board()) && !next.board().isInCheck(side)) return true;
        }
        return false;
    }

    private static boolean isMoveLegal(Position position, Move move, Move lastMove) {
        if (CastlingValidator.isCastlingMove(move)) {
            return CastlingValidator.isCastlingLegal(position, move);
        } else if (EnPassantValidator.isEnPassantMove(position, move.from(), move.to())) {
            return EnPassantValidator.isEnPassantLegal(position, move, lastMove);
        }

        Piece target = position.board().pieces().get(move.to());
        boolean isEnemyTarget = target != null && target.isWhite() != move.piece().isWhite();

        boolean isKinematicValid = isEnemyTarget
                ? move.piece().getBehaviour().isAttackValid(move.from(), move.to())
                : move.piece().getBehaviour().isMoveValid(move.from(), move.to());

        return isKinematicValid
                && position.board().isPathClear(move)
                && position.board().canOccupySquare(move.piece(), move.to());
    }

    private static Position apply(Position position, Move move) {
        Map<Square, Piece> newPieces = new HashMap<>(position.board().pieces());
        newPieces.remove(move.from());
        newPieces.put(move.to(), move.piece());

        if (CastlingValidator.isCastlingMove(move)) {
            Piece rook = newPieces.remove(CastlingValidator.rookOrigin(move.from(), move.to()));
            newPieces.put(CastlingValidator.rookTarget(move.from(), move.to()), rook);
        } else if (EnPassantValidator.isEnPassantMove(position, move.from(), move.to())) {

        }

        return new Position(new Board(newPieces), position.state().next(move));
    }
}