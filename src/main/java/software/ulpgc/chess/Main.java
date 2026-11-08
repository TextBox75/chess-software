package software.ulpgc.chess;

import software.ulpgc.chess.domain.model.Game;
import software.ulpgc.chess.domain.model.Rank;
import software.ulpgc.chess.domain.model.Square;

import static software.ulpgc.chess.domain.model.File.A;
import static software.ulpgc.chess.domain.model.File.B;

public class Main {
    static void main() {
        Game game = Game.initial();
        game.play(Square.from(A, Rank.R2), Square.from(B, Rank.R3));
        // TODO: jaque-mates
        // TODO: excepción del peón
        // TODO: reloj
        // TODO: clase de room
    }
}
