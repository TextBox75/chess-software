package software.ulpgc.chess.architecture.domain;

import java.util.Map;

import static software.ulpgc.chess.architecture.domain.File.*;
import static software.ulpgc.chess.architecture.domain.Piece.*;
import static software.ulpgc.chess.architecture.domain.Rank.*;

public class StandardBoardFactory {

    public static Board createBoard() {
        return new Board(createMap());
    }

    private static Map<Square, Piece> createMap() {
        return Map.ofEntries(
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
    }
}
