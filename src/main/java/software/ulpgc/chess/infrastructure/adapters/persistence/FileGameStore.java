package software.ulpgc.chess.infrastructure.adapters.persistence;

import software.ulpgc.chess.architecture.ports.GameStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class FileGameStore implements GameStore {
    private final File root;

    public FileGameStore(File root) {
        this.root = root;
    }


    @Override
    public String load(String id) {
        try {
            byte[] data = Files.readAllBytes(toFile(id).toPath());
            return new String(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String id, String game) {
        try {
            Files.write(toFile(id).toPath(), game.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File toFile(String id) {
        File current = this.root;
        for (int i = 0; i < 9; i += 3)
            current = new File(current, id.substring(0, i + 3));
        return new File(current, id + ".json");
    }
}
