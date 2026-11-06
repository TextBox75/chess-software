package software.ulpgc.chess.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

        if (!isMoveLegal(piece, from, to)) return new Board(pieces);

        Map<Square, Piece> newPieces = new HashMap<>(pieces);
        newPieces.remove(from);
        newPieces.put(to, piece);
        return new Board(newPieces);
    }

    private boolean isMoveLegal(Piece piece, Square from, Square to) {
        return (piece != null &&
                piece.getMovement().isMoveValid(from, to) &&
                isPathClear(piece, from, to) &&
                canOccupySquare(piece, to));
    }

    private boolean isPathClear(Piece piece, Square from, Square to) {
        if (piece.isKnight()) return true;

        SquareDistance dist = new SquareDistance(from, to);

        int stepX = Integer.signum(dist.dx());
        int stepY = Integer.signum(dist.dy());

        int currentFile = from.file().ordinal() + stepX;
        int currentRank = from.rank().ordinal() + stepY;

        int goalFile = to.file().ordinal();
        int goalRank = to.rank().ordinal();

        while (currentFile != goalFile || currentRank != goalRank) {
            Square currentSquare = new Square(File.values()[currentFile], Rank.values()[currentRank]);

            if (pieces.containsKey(currentSquare)) return false;

            currentFile += stepX;
            currentRank += stepY;
        }

        return true;
    }

    private boolean canOccupySquare(Piece fromPiece, Square to) {
        Piece toPiece = this.pieces.get(to);
        return toPiece == null || (!toPiece.isKing() && fromPiece.isWhite() != toPiece.isWhite());
    }
}