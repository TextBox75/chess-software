package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.domain.model.Game;

public interface GameDeserializer {
    Game deserialize(String data);
}
