package software.ulpgc.chess;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static software.ulpgc.chess.File.*;
import static software.ulpgc.chess.Rank.*;
import static software.ulpgc.chess.Piece.*;

public final class Board {
    private final Map<Square, Piece> pieces;

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
        Map<Square, Piece> newPieces = new HashMap<>(pieces);

        Piece fromPiece = pieces.get(from);
        newPieces.remove(from);
        newPieces.put(to, fromPiece);

        return new Board(newPieces);
    }

    public Board(Map<Square, Piece> pieces) {
        this.pieces = Map.copyOf(pieces);
    }

    Piece pieceAt(File file, Rank rank) {
        return pieceAt(new Square(file, rank));
    }

    Piece pieceAt(String square) {
        return null;
    }

    private Piece pieceAt(Square square) {
        return null;
    }

    public Map<Square, Piece> pieces() {
        return pieces;
    }
}