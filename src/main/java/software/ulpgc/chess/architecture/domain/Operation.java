package software.ulpgc.chess.architecture.domain;

public interface Operation {
    Output execute(Input input);

    interface Input {
        String get(String key);
    }

    interface Output {
        int status();
        byte[] result();

    }
}
