package software.ulpgc.chess.domain.model;

import java.util.*;

import static software.ulpgc.chess.domain.model.File.*;
import static software.ulpgc.chess.domain.model.Rank.*;
import static software.ulpgc.chess.domain.model.Piece.*;

public record Board(Map<Square, Piece> pieces) {

    public Board(Map<Square, Piece> pieces) {
        this.pieces = Map.copyOf(pieces);
    }

    public static Board initial() {
        return new Board(initialMap());
    }

    public static Map<Square, Piece> initialMap() {
        Map<Square, Piece> Initial = Map.ofEntries(
                Map.entry(new Square(A, R1), WhiteRook),
                Map.entry(new Square(B, R1), WhiteKnight),
                Map.entry(new Square(C, R1), WhiteBishop),
                Map.entry(new Square(D, R1), WhiteQueen),
                Map.entry(new Square(E, R1), WhiteKing),
                Map.entry(new Square(F, R1), WhiteBishop),
                Map.entry(new Square(G, R1), WhiteKnight),
                Map.entry(new Square(H, R1), WhiteRook),

                Map.entry(new Square(A, R2), WhitePawn),
                Map.entry(new Square(B, R2), WhitePawn),
                Map.entry(new Square(C, R2), WhitePawn),
                Map.entry(new Square(D, R2), WhitePawn),
                Map.entry(new Square(E, R2), WhitePawn),
                Map.entry(new Square(F, R2), WhitePawn),
                Map.entry(new Square(G, R2), WhitePawn),
                Map.entry(new Square(H, R2), WhitePawn),

                Map.entry(new Square(A, R7), BlackPawn),
                Map.entry(new Square(B, R7), BlackPawn),
                Map.entry(new Square(C, R7), BlackPawn),
                Map.entry(new Square(D, R7), BlackPawn),
                Map.entry(new Square(E, R7), BlackPawn),
                Map.entry(new Square(F, R7), BlackPawn),
                Map.entry(new Square(G, R7), BlackPawn),
                Map.entry(new Square(H, R7), BlackPawn),

                Map.entry(new Square(A, R8), BlackRook),
                Map.entry(new Square(B, R8), BlackKnight),
                Map.entry(new Square(C, R8), BlackBishop),
                Map.entry(new Square(D, R8), BlackQueen),
                Map.entry(new Square(E, R8), BlackKing),
                Map.entry(new Square(F, R8), BlackBishop),
                Map.entry(new Square(G, R8), BlackKnight),
                Map.entry(new Square(H, R8), BlackRook)
        );

        return Initial;
    }

    public Board move(Square from, Square to) {
        Piece piece = pieces.get(from);

        if (piece == null || !isMoveLegal(piece, from, to)) return new Board(pieces);

        Map<Square, Piece> newPieces = new HashMap<>(pieces);
        newPieces.remove(from);
        newPieces.put(to, piece);
        return new Board(newPieces);
    }

    private boolean isMoveLegal(Piece piece, Square from, Square to) {
        Piece target = pieces.get(to);

        boolean isEnemyTarget = target != null && target.isWhite() != piece.isWhite();

        boolean isKinematicValid = isEnemyTarget
                ? piece.getBehaviour().isAttackValid(from, to)
                : piece.getBehaviour().isMoveValid(from, to);

        return isKinematicValid && isPathClear(piece, from, to) && canOccupySquare(piece, to);
    }

    private boolean isPathClear(Piece piece, Square from, Square to) {
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

    private boolean isSquareAttacked(Square targetSquare, Side attackingSide) {
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

    private static List<Square> allSquares() {
        return Arrays.stream(File.values())
                .flatMap(file -> Arrays.stream(Rank.values()).map(rank -> new Square(file, rank)))
                .toList();
    }
}