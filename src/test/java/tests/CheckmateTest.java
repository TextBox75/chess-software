package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.ulpgc.chess.domain.model.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static software.ulpgc.chess.domain.model.GameStatus.*;
import static software.ulpgc.chess.domain.model.Piece.*;

@DisplayName("Tests para Jaque y Jaque Mate")
class CheckmateTest {

    @Nested
    @DisplayName("Escenarios de Jaque Mate definitivo")
    class CheckmateScenarios {

        @Test
        @DisplayName("Mate del Pasillo (Back-Rank Mate): la torre da mate al rey atrapado tras sus peones")
        void should_detect_back_rank_checkmate() {
            // El rey negro está en g8 bloqueado por sus peones en f7, g7, h7.
            // La torre blanca entra a d8 dando jaque mate.
            Board board = new Board(Map.of(
                    Square.of("d1"), WhiteRook,
                    Square.of("g8"), BlackKing,
                    Square.of("f7"), BlackPawn,
                    Square.of("g7"), BlackPawn,
                    Square.of("h7"), BlackPawn,
                    Square.of("a1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            // La torre blanca desciende a la octava fila
            Game afterMove = game.play(Square.of("d1"), Square.of("d8"));

            assertEquals(CHECKMATE, afterMove.status(), "Debe ser Jaque Mate");
            // Un movimiento posterior no debe cambiar el estado
            Game afterOver = afterMove.play(Square.of("g8"), Square.of("h8"));
            assertEquals(afterMove, afterOver);
        }

        @Test
        @DisplayName("Mate del Pastor (Scholar's Mate) desde la posición final")
        void should_detect_scholars_mate() {
            // Posición previa: Dama blanca en h5 y Alfil en c4 amenazan f7.
            // La dama captura en f7 protegida por el alfil en c4.
            Board board = new Board(Map.of(
                    Square.of("h5"), WhiteQueen,
                    Square.of("c4"), WhiteBishop,
                    Square.of("e8"), BlackKing,
                    Square.of("e1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            Game afterMove = game.play(Square.of("h5"), Square.of("f7"));

            assertEquals(CHECKMATE, afterMove.status(), "La dama da mate apoyada por el alfil");
        }
    }

    @Nested
    @DisplayName("Escenarios de Jaque que NO son Mate (Defensas posibles)")
    class NotCheckmateScenarios {

        @Test
        @DisplayName("No es mate si el rey puede huir a una casilla segura")
        void should_be_check_not_mate_when_king_can_escape() {
            // Torre en e1 da jaque al rey en e8, pero el rey puede escapar a d8 o f8
            Board board = new Board(Map.of(
                    Square.of("a1"), WhiteRook,
                    Square.of("e8"), BlackKing,
                    Square.of("h1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            Game afterMove = game.play(Square.of("a1"), Square.of("e1"));

            assertEquals(CHECK, afterMove.status(), "Solo es Jaque porque el rey puede huir");
        }

        @Test
        @DisplayName("No es mate si una pieza aliada puede capturar a la pieza atacante")
        void should_be_check_not_mate_when_attacker_can_be_captured() {
            // Torre blanca ataca al rey negro en d8, pero un alfil negro en g5 puede comer a la torre
            Board board = new Board(Map.of(
                    Square.of("d1"), WhiteRook,
                    Square.of("d8"), BlackKing,
                    Square.of("g5"), BlackBishop,
                    Square.of("a1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            Game afterMove = game.play(Square.of("d1"), Square.of("d8"));

            assertEquals(CHECK, afterMove.status(), "El alfil negro puede comer en d8 salvando el mate");
        }

        @Test
        @DisplayName("No es mate si una pieza aliada puede interponerse (bloquear) la línea de ataque")
        void should_be_check_not_mate_when_piece_can_block_path() {
            // Torre en e1 da jaque al rey en e8, pero el caballo en c6 puede bloquear en e7
            Board board = new Board(Map.of(
                    Square.of("e2"), WhiteRook,
                    Square.of("e8"), BlackKing,
                    Square.of("c6"), BlackKnight,
                    Square.of("a1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            Game afterMove = game.play(Square.of("e2"), Square.of("e1"));

            assertEquals(CHECK, afterMove.status(), "El caballo puede saltar a e7 para bloquear");
        }
    }
}