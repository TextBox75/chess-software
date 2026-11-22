package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.architecture.domain.State;

public interface StateDeserializer {
    State deserialize(String state);
}
