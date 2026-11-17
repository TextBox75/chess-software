package software.ulpgc.chess.infrastructure;

import software.ulpgc.chess.architecture.ports.GameDeserializer;
import software.ulpgc.chess.architecture.ports.GameStore;
import software.ulpgc.chess.domain.model.Game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileGameStore implements GameStore {
    private final File root;
    private final GameDeserializer deserializer;

    public FileGameStore(File root, GameDeserializer deserializer) {
        this.root = root;
        this.deserializer = deserializer;
    }

    @Override
    public Game get(String id) {
        File file = toFile(id);
        try {
            byte[] data = Files.readAllBytes(toFile(id).toPath());
            return deserializer.deserialize(new String(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File toFile(String id) {
        File current = this.root;

        for (int i = 0; i < 9; i += 3) {
            current = new File(current, id.substring(current));
        }
        return null;
    }
}
