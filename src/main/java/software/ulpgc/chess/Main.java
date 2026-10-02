package software.ulpgc.chess;

import static software.ulpgc.chess.File.*;
import static software.ulpgc.chess.Rank.*;

public class Main {
    static void main() {
        Board gameBoard = Board.initial();
        gameBoard.move(new Square(A, R7), new Square(D, R4));
    }
}
