package software.ulpgc.chess.architecture.operations;

import software.ulpgc.chess.architecture.domain.Operation;
import software.ulpgc.chess.architecture.ports.GameStore;

import java.util.UUID;

public class CreateGameOperation implements Operation {
    private final GameStore store;

    public CreateGameOperation(GameStore store) {
        this.store = store;
    }

    @Override
    public Output execute(Input input) {
        String id = UUID.randomUUID().toString();
        store.save(id, "");
        return new Output() {
            @Override
            public int status() {
                return 200;
            }

            @Override
            public byte[] result() {
                return id.getBytes();
            }
        };
    }
}
