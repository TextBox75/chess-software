package software.ulpgc.chess.adapters;

import software.ulpgc.chess.domain.model.Board;
import software.ulpgc.chess.domain.model.Piece;
import software.ulpgc.chess.domain.model.Square;
import software.ulpgc.chess.ports.BoardDeserializer;

import java.util.HashMap;
import java.util.Map;

public class FenBoardSerializer implements BoardDeserializer {
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
        int i = 1;
        for (char order : rank) {
            // TODO: identificar si es número o letra y proceder según lo averigüado
            Square square = Square.of(i, j);
        }
        return Map.of();
    }
}
