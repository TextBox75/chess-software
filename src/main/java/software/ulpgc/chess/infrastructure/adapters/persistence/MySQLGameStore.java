package software.ulpgc.chess.infrastructure.adapters.persistence;

import software.ulpgc.chess.architecture.ports.GameStore;

public class MySQLGameStore implements GameStore {
    @Override
    public String load(String id) {
        return null;
    }

    @Override
    public void save(String id, String game) {

    }
}
