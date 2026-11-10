package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.domain.model.Square;

public interface PieceBehaviour {
    boolean isMoveValid(Square from, Square to);
    default boolean isAttackValid(Square from, Square to) {
        return isMoveValid(from, to);
    }
}
