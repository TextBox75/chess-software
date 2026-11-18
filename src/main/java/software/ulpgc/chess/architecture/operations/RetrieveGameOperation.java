package software.ulpgc.chess.architecture.operations;

import software.ulpgc.chess.architecture.domain.Operation;
import software.ulpgc.chess.architecture.ports.GameStore;

public class RetrieveGameOperation implements Operation {
    private final GameStore store;

    public RetrieveGameOperation(GameStore store) {
        this.store = store;
    }

    @Override
    public Output execute(Input input) {
        return new Output() {
            @Override
            public int status() {
                return 200;
            }

            @Override
            public byte[] result() {
                return store.load(input.get("id")).getBytes();
            }
        };
    }
}
