package software.ulpgc.chess.architecture.domain;

public interface PieceBehaviour {
    boolean isMoveValid(Square from, Square to);
    default boolean isAttackValid(Square from, Square to) {
        return isMoveValid(from, to);
    }
}
