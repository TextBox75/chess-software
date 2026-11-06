package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceMovement;

import java.util.Arrays;

import static software.ulpgc.chess.domain.model.StandardPieceMovement.*;

public enum Piece {
    WhitePawn('P', WhitePawnMovement),
    WhiteKnight('N', KnightMovement),
    WhiteBishop('B', BishopMovement),
    WhiteRook('R', RookMovement),
    WhiteQueen('Q', QueenMovement),
    WhiteKing('K', KingMovement),
    BlackPawn('p', BlackPawnMovement),
    BlackKnight('n', KnightMovement),
    BlackBishop('b', BishopMovement),
    BlackRook('r', RookMovement),
    BlackQueen('q', QueenMovement),
    BlackKing('k', KingMovement);

    private final char fenSymbol;
    private final PieceMovement movement;

    Piece(char fenSymbol, PieceMovement movement) {
        this.fenSymbol = fenSymbol;
        this.movement = movement;
    }

    public static Piece fromFenSymbol(char symbol) {
        return Arrays.stream(values())
                .filter(piece -> piece.fenSymbol == symbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pieza no encontrada para el símbolo: " + symbol));
    }

    public PieceMovement getMovement() {
        return movement;
    }

    public boolean isKing() {
        return this.name().endsWith("King");
    }

    public boolean isKnight() {
        return this.name().endsWith("Knight");
    }

    public boolean isWhite() {
        return this.name().startsWith("White");
    }

    public boolean isBlack() {
        return this.name().startsWith("Black");
    }

}