package software.ulpgc.chess.domain.model;

import static software.ulpgc.chess.domain.model.File.*;
import static software.ulpgc.chess.domain.model.Rank.*;

public class Main {
    static void main() {
        Board gameBoard = Board.initial();
        gameBoard.move(new Square(C, R8), new Square(A, R6));

        // TODO: conquistas
        // TODO: estado de juego
    }
}
