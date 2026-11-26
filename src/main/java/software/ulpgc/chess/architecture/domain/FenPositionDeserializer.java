package software.ulpgc.chess.architecture.domain;

import software.ulpgc.chess.architecture.ports.PositionDeserializer;

public class FenPositionDeserializer implements PositionDeserializer {
    private final FenBoardDeserializer boardDeserializer = new FenBoardDeserializer();
    private final FenStateDeserializer stateDeserializer = new FenStateDeserializer();

    @Override
    public Position deserialize(String fen) {
        String[] fields = fen.split(" ");
        Board board = boardDeserializer.deserialize(fields[0]);
        State state = stateDeserializer.deserialize(fields[1], fields[2], fields[3]);
        return new Position(board, state);
    }
}