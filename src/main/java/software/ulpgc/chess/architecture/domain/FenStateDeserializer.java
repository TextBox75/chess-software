package software.ulpgc.chess.architecture.domain;

import java.util.Optional;

public class FenStateDeserializer  {

    public State deserialize(String sideField, String castlingField, String enPassantField) {
        return new State(sideFrom(sideField), rightsFrom(castlingField), enPassantFrom(enPassantField));
    }

    private Side sideFrom(String field) {
        return switch (field) {
            case "w" -> Side.WHITE;
            case "b" -> Side.BLACK;
            default -> throw new IllegalArgumentException("Turno FEN no válido: " + field);
        };
    }

    private CastlingRights rightsFrom(String field) {
        return new CastlingRights(
                field.contains("K"),
                field.contains("Q"),
                field.contains("k"),
                field.contains("q")
        );
    }

    private Optional<Square> enPassantFrom(String field) {
        return field.equals("-") ? Optional.empty() : Optional.of(Square.of(field));
    }
}