package software.ulpgc.chess.infrastructure.adapters;

import io.javalin.http.Context;
import software.ulpgc.chess.architecture.domain.Operation;

public class JavalinContextInput implements Operation.Input {
    private final Context context;

    public JavalinContextInput(Context context) {
        this.context = context;
    }

    @Override
    public String get(String key) {
        return "";
    }
}
