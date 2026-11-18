package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.architecture.domain.Game;

public interface GameDeserializer {
    Game deserialize(String data);
}
