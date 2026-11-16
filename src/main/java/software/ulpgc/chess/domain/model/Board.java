package software.ulpgc.chess.domain.model;

import java.util.*;

import static software.ulpgc.chess.domain.model.File.*;
import static software.ulpgc.chess.domain.model.Square.allSquares;

public record Board(Map<Square, Piece> pieces, CastlingRights castlingRights) {

    public Board(Map<Square, Piece> pieces) {
        this(pieces, CastlingRights.initial());
    }

    public Board(Map<Square, Piece> pieces, CastlingRights castlingRights) {
        this.pieces = Map.copyOf(pieces);
        this.castlingRights = castlingRights;
    }

    public Board move(Square from, Square to) {

        Piece piece = pieces.get(from);

        if (piece == null || !isMoveLegal(piece, from, to)) return new Board(pieces, this.castlingRights);

        if (piece.canPromote(to.rank())) {
            piece = piece.defaultPromote();
        }

        return move(piece, from, to);
    }

    private Board move(Piece piece, Square from, Square to) {
        Map<Square, Piece> newPieces = new HashMap<>(pieces);
        newPieces.remove(from);
        newPieces.put(to, piece);

        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            boolean isKingSide = to.file().ordinal() > from.file().ordinal();
            Square rookOrigin = new Square(isKingSide ? H : A, from.rank());
            Square rookTarget = new Square(isKingSide ? F : D, from.rank());
            Piece rook = newPieces.remove(rookOrigin);
            newPieces.put(rookTarget, rook);
        }

        return new Board(newPieces, castlingRights.update(from, to, piece));
    }

    private boolean isMoveLegal(Piece piece, Square from, Square to) {
        if (CastlingValidator.isCastlingMove(piece, from, to)) {
            return CastlingValidator.isCastlingLegal(this, piece, from, to);
        }

        Piece target = pieces.get(to);

        boolean isEnemyTarget = target != null && target.isWhite() != piece.isWhite();

        boolean isKinematicValid = isEnemyTarget
                ? piece.getBehaviour().isAttackValid(from, to)
                : piece.getBehaviour().isMoveValid(from, to);

        return isKinematicValid && isPathClear(piece, from, to) && canOccupySquare(piece, to);
    }

    public boolean isPathClear(Piece piece, Square from, Square to) {
        if (piece.isKnight()) return true;
        return isPathClear(from, to);
    }

    private boolean isPathClear(Square from, Square to) {
        SquareDistance distance = new SquareDistance(from, to);

        int stepX = Integer.signum(distance.X());
        int stepY = Integer.signum(distance.Y());

        int currentFile = from.file().ordinal() + stepX;
        int currentRank = from.rank().ordinal() + stepY;

        while (currentFile != to.file().ordinal() || currentRank != to.rank().ordinal()) {
            if (pieces.containsKey(Square.of(currentFile, currentRank))) return false;
            currentFile += stepX;
            currentRank += stepY;
        }

        return true;
    }

    private boolean canOccupySquare(Piece fromPiece, Square to) {
        Piece toPiece = this.pieces.get(to);
        return toPiece == null || (!toPiece.isKing() && fromPiece.isWhite() != toPiece.isWhite());
    }

    public boolean isInCheck(Side side) {
        Square kingSquare = findKingSquare(side);
        Side enemySide = side.opposite();
        return isSquareAttacked(kingSquare, enemySide);
    }

    private Square findKingSquare(Side side) {
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isKing() && (entry.getValue().isOfSide(side)))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Rey no encontrado en el tablero"));
    }

    public boolean isSquareAttacked(Square targetSquare, Side attackingSide) {
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(attackingSide))
                .anyMatch(entry -> {
                    Square originSquare = entry.getKey();
                    Piece piece = entry.getValue();

                    return piece.getBehaviour().isAttackValid(originSquare, targetSquare)
                            && isPathClear(piece, originSquare, targetSquare);
                });
    }

    public boolean hasLegalMove(Side attackedSide) {
        List<Square> attackedSideSquares = pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(attackedSide))
                .map(Map.Entry::getKey)
                .toList();
        return hasLegalMove(attackedSide, attackedSideSquares);
    }

    private boolean hasLegalMove(Side attackedSide, List<Square> attackedSideSquares) {
        for (Square square : attackedSideSquares) {
            if (hasLegalMove(attackedSide, square)) return true;
        }
        return false;
    }

    private boolean hasLegalMove(Side attackedSide, Square from) {
        for (Square to : allSquares()) {
            if (from.equals(to)) continue;

            Board nextBoard = this.move(from, to);

            if (!nextBoard.equals(this) && !nextBoard.isInCheck(attackedSide)) return true;
        }

        return false;
    }
}