package software.ulpgc.chess.architecture.domain;

public record Position(Board board, State state) {
    public static Position initial() {
        return new Position(StandardBoardFactory.createBoard(), State.initial());
    }
}
