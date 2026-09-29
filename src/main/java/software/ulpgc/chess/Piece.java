package software.ulpgc.chess;

import static software.ulpgc.chess.Color.*;

public enum Piece {
    WhitePawn,
    WhiteRook,
    WhiteKnight,
    WhiteBishop,
    WhiteKing,
    WhiteQueen,
    BlackPawn,
    BlackRook,
    BlackKnight,
    BlackBishop,
    BlackKing,
    BlackQueen;

    Color color() {
        return isWhite() ? White : Black;
    }

    boolean IsPawn() {
        return this == WhitePawn || this == BlackPawn;
    }
    boolean IsRook() {
        return this == WhiteRook || this == BlackRook;
    }
    boolean IsKnight() {
        return this == WhiteKnight || this == BlackKnight;
    }
    boolean IsBishop() {
        return this == WhiteBishop || this == BlackBishop;
    }
    boolean IsKing() {
        return this == WhiteKing || this == BlackKing;
    }
    boolean IsQueen() {
        return this == WhiteQueen || this == BlackQueen;
    }

    private boolean isWhite() {
        return this == WhitePawn ||
                this == WhiteRook ||
                this == WhiteKnight ||
                this == WhiteKing ||
                this == WhiteQueen ||
                this == WhiteBishop;
    }
}
