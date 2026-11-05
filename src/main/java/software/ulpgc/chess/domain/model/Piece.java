package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceMovement;

import java.util.Arrays;

import static software.ulpgc.chess.domain.model.StandardPieceMovement.*;

public enum Piece {
    WhitePawn('P', Pawn),
    WhiteKnight('N', Knight),
    WhiteBishop('B', Bishop),
    WhiteRook('R', Rook),
    WhiteQueen('Q', Queen),
    WhiteKing('K', King),
    BlackPawn('p', Pawn),
    BlackKnight('n', Knight),
    BlackBishop('b', Bishop),
    BlackRook('r', Rook),
    BlackQueen('q', Queen),
    BlackKing('k', King);

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