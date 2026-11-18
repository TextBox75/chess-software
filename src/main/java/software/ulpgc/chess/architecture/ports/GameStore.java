package software.ulpgc.chess.architecture.ports;

public interface GameStore {
    String load(String id);
    void save(String id, String game);
}
