package software.ulpgc.chess.architecture.domain;

public class FenStateDeserializer  {

    public State deserialize(String sideField, String castlingField) {
        return new State(sideFrom(sideField), rightsFrom(castlingField));
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
}