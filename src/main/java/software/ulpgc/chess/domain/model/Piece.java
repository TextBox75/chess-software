package software.ulpgc.chess.domain.model;

import java.util.Arrays;

public enum Piece {
    WhitePawn('P'),
    WhiteKnight('N'),
    WhiteBishop('B'),
    WhiteRook('R'),
    WhiteQueen('Q'),
    WhiteKing('K'),
    BlackPawn('p'),
    BlackKnight('n'),
    BlackBishop('b'),
    BlackRook('r'),
    BlackQueen('q'),
    BlackKing('k');

    private final char fenSymbol;

    Piece(char fenSymbol) {
        this.fenSymbol = fenSymbol;
    }

    public static Piece fromFenSymbol(char symbol) {
        return Arrays.stream(values())
                .filter(piece -> piece.fenSymbol == symbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pieza no encontrada para el símbolo: " + symbol));
    }

    public boolean isWhite() {
        return this.name().startsWith("White");
    }

    public boolean isBlack() {
        return this.name().startsWith("Black");
    }

    public boolean isRook() {
        return this.name().endsWith("Rook");
    }

    public boolean isPawn() {
        return this.name().endsWith("Pawn");
    }

    public boolean isKnight() {
        return this.name().endsWith("Knight");
    }

    public boolean isBishop() {
        return this.name().endsWith("Bishop");
    }

    public boolean isQueen() {
        return this.name().endsWith("Queen");
    }

    public boolean isKing() {
        return this.name().endsWith("King");
    }
}