package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.architecture.domain.Position;

public interface PositionDeserializer {
    Position deserialize(String fen);
}
