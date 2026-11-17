package software.ulpgc.chess.architecture;

public interface Operation {
    Output execute(Input input);

    interface Input {

    }

    interface Output {
        int result();
        byte[] status();
    }
}
