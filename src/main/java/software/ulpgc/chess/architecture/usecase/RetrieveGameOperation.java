package software.ulpgc.chess.architecture.usecase;

import software.ulpgc.chess.architecture.Operation;
import software.ulpgc.chess.architecture.ports.GameStore;

public class RetrieveGameOperation implements Operation {
    private final GameStore store;

    public RetrieveGameOperation(GameStore store) {
        this.store = store;
    }

    @Override
    public Output execute(Input input) {
        String id = input.get("id")
        return null;
    }
}
