package software.ulpgc.chess.domain.model;

public record Game(
        Board board,
        Side color,
        GameStatus status,
        ChessClock clock
) {
    public static Game initial() {
        return new Game(
                Board.initial(),
                Side.WHITE,
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }

    public Game play(Square from, Square to) {
        // TODO: aquí, en algún lugar, se ha de implementar la comprobación de jaque-mate
        return new Game(
                this.board.move(from, to),
                this.color.opposite(),
                GameStatus.PLAYING,
                ChessClock.initial()
        );
    }
}
