package software.ulpgc.chess.domain.model;

public record ChessClock(ColorClock whiteClock, ColorClock blackClock) {
    public static ChessClock initial() {
        return new ChessClock(
                new ColorClock(15, 0),
                new ColorClock(15, 0)
        );
    }
}
