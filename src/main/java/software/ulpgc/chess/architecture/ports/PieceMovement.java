package software.ulpgc.chess.architecture.ports;

import software.ulpgc.chess.domain.model.Square;

public interface PieceMovement {
    boolean isMoveValid(Square from, Square to);
}
