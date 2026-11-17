package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.domain.model.Game;

public interface GameStore {
    Game get(String id);
}
