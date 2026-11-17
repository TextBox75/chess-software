package software.ulpgc.chess.infrastructure;

import io.javalin.config.JavalinConfig;
import software.ulpgc.chess.architecture.Operation;
import software.ulpgc.chess.architecture.usecase.CreateGameOperation;
import software.ulpgc.chess.architecture.usecase.RetrieveGameOperation;
import software.ulpgc.chess.architecture.usecase.UpdateGameOperation;
import software.ulpgc.chess.domain.model.Game;
import software.ulpgc.chess.domain.model.Rank;
import software.ulpgc.chess.domain.model.Square;

import static software.ulpgc.chess.domain.model.File.A;
import static software.ulpgc.chess.domain.model.File.B;

public class Main {
    static void main() {
        Game game = Game.initial();
        game.play(Square.from(A, Rank.R2), Square.from(B, Rank.R3));

        // TODO: desacoplar board
        // TODO: en passant

        // TODO: clase de room
        // TODO: promociones (al completo, se necesita interfaz primero)
        // TODO: reloj
    }

    public static void init(JavalinConfig config) {
        config.routes.get("/", c->execute(c, new RetrieveGameOperation()));
        config.routes.get("/", c->execute(c, new UpdateGameOperation()));
        config.routes.post("/", c->execute(c, new CreateGameOperation()));
    }

    private static Object execute(Object context, Operation operation) {
        Operation.Output output = operation.execute(new JavalinContextInput());
        context.result(output.result());
        context.status(output.status());
        return c;
    }
}
