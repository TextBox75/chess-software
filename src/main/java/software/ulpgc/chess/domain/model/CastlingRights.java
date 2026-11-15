package software.ulpgc.chess.domain.model;

import static software.ulpgc.chess.domain.model.File.A;
import static software.ulpgc.chess.domain.model.File.H;
import static software.ulpgc.chess.domain.model.Rank.R1;
import static software.ulpgc.chess.domain.model.Rank.R8;

public record CastlingRights(boolean whiteKingSide, boolean whiteQueenSide, boolean blackKingSide, boolean blackQueenSide) {

    public static CastlingRights initial() {
        return new CastlingRights(true, true, true, true);
    }

    public boolean canCastle(Side side, boolean isKingSide) {
        return side == Side.WHITE
                ? (isKingSide ? whiteKingSide : whiteQueenSide)
                : (isKingSide ? blackKingSide : blackQueenSide);
    }

    public CastlingRights update(Square from, Square to, Piece movedPiece) {
        boolean wks = whiteKingSide;
        boolean wqs = whiteQueenSide;
        boolean bks = blackKingSide;
        boolean bqs = blackQueenSide;

        if (movedPiece == Piece.WhiteKing) { wks = false; wqs = false; }
        if (movedPiece == Piece.BlackKing) { bks = false; bqs = false; }

        Square a1 = new Square(A, R1), h1 = new Square(H, R1);
        Square a8 = new Square(A, R8), h8 = new Square(H, R8);

        if (from.equals(h1) || to.equals(h1)) wks = false;
        if (from.equals(a1) || to.equals(a1)) wqs = false;
        if (from.equals(h8) || to.equals(h8)) bks = false;
        if (from.equals(a8) || to.equals(a8)) bqs = false;

        return new CastlingRights(wks, wqs, bks, bqs);
    }
}
