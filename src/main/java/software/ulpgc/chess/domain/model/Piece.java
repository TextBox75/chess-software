package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceBehaviour;

import java.util.Arrays;

import static software.ulpgc.chess.domain.model.StandardPieceBehaviour.*;

public enum Piece {
    WhitePawn('P', WhitePawnBehaviour),
    WhiteKnight('N', KnightBehaviour),
    WhiteBishop('B', BishopBehaviour),
    WhiteRook('R', RookBehaviour),
    WhiteQueen('Q', QueenBehaviour),
    WhiteKing('K', KingBehaviour),
    BlackPawn('p', BlackPawnBehaviour),
    BlackKnight('n', KnightBehaviour),
    BlackBishop('b', BishopBehaviour),
    BlackRook('r', RookBehaviour),
    BlackQueen('q', QueenBehaviour),
    BlackKing('k', KingBehaviour);

    private final char fenSymbol;
    private final PieceBehaviour Behaviour;

    Piece(char fenSymbol, PieceBehaviour Behaviour) {
        this.fenSymbol = fenSymbol;
        this.Behaviour = Behaviour;
    }

    public static Piece fromFenSymbol(char symbol) {
        return Arrays.stream(values())
                .filter(piece -> piece.fenSymbol == symbol)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pieza no encontrada para el símbolo: " + symbol));
    }

    public PieceBehaviour getBehaviour() {
        return Behaviour;
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
}