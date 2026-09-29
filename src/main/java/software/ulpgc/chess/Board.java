package software.ulpgc.chess;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static software.ulpgc.chess.Piece.*;

public final class Board {
    private final Map<Square, Piece> pieces;

    public static Board initial() {
        return new Board(initialMap());
    }

    public static Map<Square, Piece> initialMap() {
        Map<Square, Piece> Initial = Map.ofEntries(
                new Square("a1"), WhiteRook,
                new Square("a1"), WhiteRook
        );

        return Initial;
    }

    public Board move(Square from, Square to) {

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Board) obj;
        return Objects.equals(this.pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }

    @Override
    public String toString() {
        return "Board[" +
                "pieces=" + pieces + ']';
    }

}