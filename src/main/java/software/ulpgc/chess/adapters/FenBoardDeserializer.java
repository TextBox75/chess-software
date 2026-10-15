package software.ulpgc.chess.adapters;

import software.ulpgc.chess.domain.model.Board;
import software.ulpgc.chess.domain.model.Piece;
import software.ulpgc.chess.domain.model.Square;
import software.ulpgc.chess.ports.BoardDeserializer;

import java.util.HashMap;
import java.util.Map;

import static software.ulpgc.chess.domain.model.Piece.*;

public class FenBoardDeserializer implements BoardDeserializer {
    @Override
    public Board deserialize(String value) {
        return deserialize(value.split("/"));
    }

    private Board deserialize(String[] ranks) {
        return new Board(piecesIn(ranks));
    }

    private Map<Square, Piece> piecesIn(String[] ranks) {
        Map<Square, Piece> map = new HashMap<>();
        int j = 8;
        for (String rank : ranks)
            map.putAll(piecesIn(j--, rank.toCharArray()));
        return map;
    }

    private Map<Square, Piece> piecesIn(int j, char[] rank) {
        Map<Square, Piece> rankMap = Map.of();
        int i = 1;

        for (char order : rank) {
            if (Character.isLetter(order)) {
                Square square = Square.of(i, j);
                rankMap.put(square, piecesIn(order));
                i++;
                continue;
            }

            i += Integer.parseInt(String.valueOf(order));
        }
        return rankMap;
    }

    private Piece piecesIn(char charCharacter) {
        Piece piece = null;
        switch (charCharacter) {
            case 'p':
                piece = BlackPawn;
                break;
            case 'r':
                piece = BlackRook;
                break;
            case 'n':
                piece = BlackKnight;
                break;
            case 'b':
                piece = BlackBishop;
                break;
            case 'k':
                piece = BlackKing;
                break;
            case 'q':
                piece = BlackQueen;
                break;
            case 'P':
                piece = WhitePawn;
                break;
            case 'R':
                piece = WhiteRook;
                break;
            case 'N':
                piece = WhiteKnight;
                break;
            case 'B':
                piece = WhiteBishop;
                break;
            case 'K':
                piece = WhiteKing;
                break;
            case 'Q':
                piece = WhiteQueen;
                break;
        }

        return piece;
    }
}
