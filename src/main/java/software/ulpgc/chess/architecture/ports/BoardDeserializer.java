package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.architecture.domain.Board;

public interface BoardDeserializer {
    Board deserialize(String state);
}
