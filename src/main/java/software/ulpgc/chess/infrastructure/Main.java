package software.ulpgc.chess.infrastructure;

import software.ulpgc.chess.architecture.domain.Game;
import software.ulpgc.chess.architecture.domain.Rank;
import software.ulpgc.chess.architecture.domain.Square;

import static software.ulpgc.chess.architecture.domain.File.*;

public class Main {

    // TODO: clase de room
    // TODO: promociones (implementar bien)
    // TODO: reloj (se necesita un tick-update)

    static void main() {
        Game game = Game.initial();
        game.play(Square.from(A, Rank.R2), Square.from(B, Rank.R2));

        //Javalin.create(Main::init).start(8080);
    }

    /*private static void init(JavalinConfig config) {
        config.routes.get("/", c->execute(c, new RetrieveGameOperation(new FileGameStore(new File("games")))));
        config.routes.put("/", c->execute(c, new UpdateGameOperation()));
        config.routes.post("/", c->execute(c, new CreateGameOperation()));
    }

    private static void execute(Context context, Operation operation) {
        Operation.Output output = operation.execute(new JavalinContextInput(context));
        context.result(output.result());
        context.status(output.status());
    }*/
}
