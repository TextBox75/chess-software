package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.domain.model.Board;

public interface BoardDeserializer {
    Board deserialize(String string);
}
