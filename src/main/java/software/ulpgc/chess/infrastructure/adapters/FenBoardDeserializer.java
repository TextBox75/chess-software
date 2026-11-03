package software.ulpgc.chess.infrastructure.adapters;

import software.ulpgc.chess.domain.model.Board;
import software.ulpgc.chess.domain.model.Piece;
import software.ulpgc.chess.domain.model.Square;
import software.ulpgc.chess.architecture.ports.BoardDeserializer;

import java.util.HashMap;
import java.util.Map;

public class FenBoardDeserializer implements BoardDeserializer {
    @Override
    public Board deserialize(String value) {
        return deserialize(value.split(" ")[0].split("/"));
    }

    private Board deserialize(String[] ranks) {
        return new Board(piecesIn(ranks));
    }

    private Map<Square, Piece> piecesIn(String[] ranks) {
        Map<Square, Piece> map = new HashMap<>();
        int j = 7;
        for (String rank : ranks) {
            System.out.println(rank);
            map.putAll(piecesIn(j--, rank.toCharArray()));
        }
        return map;
    }

    private Map<Square, Piece> piecesIn(int j, char[] rank) {
        Map<Square, Piece> rankMap = new HashMap<>();
        int i = 0;

        for (char order : rank) {
            if (Character.isLetter(order)) {
                Square square = Square.of(i, j);
                rankMap.put(square, piecesIn(order));
                i++;
            } else if (Character.isDigit(order)) {
                i += Integer.parseInt(String.valueOf(order));
            }
        }
        return rankMap;
    }

    private Piece piecesIn(char charCharacter) {
        return Piece.fromFenSymbol(charCharacter);
    }
}
