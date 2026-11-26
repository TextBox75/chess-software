package software.ulpgc.chess.architecture.domain;

import java.util.Map;

record Room(RoomCode code, Map<Side, PlayerId> players, Game game) {
    Room join(PlayerId player) {
        return null;
    }

    Room play(PlayerId player, Square from, Square to) {
        return null;
    }
}