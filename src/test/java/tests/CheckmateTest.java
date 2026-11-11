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
                    Square.of("g1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            // La torre blanca penetra a la octava fila dando jaque en la fila trasera
            Game afterMove = game.play(Square.of("d1"), Square.of("d8"));

            assertEquals(CHECKMATE, afterMove.status(), "Debe ser Jaque Mate");

            // Un movimiento posterior con la partida ya finalizada no debe alterar el estado
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
                    Square.of("d8"), BlackQueen, // <-- Bloquea la casilla de escape d8
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
            // Torre blanca entra a d8 atacando al rey negro en e8, pero el alfil en c7 (o g6) puede capturarla
            // Si queremos mantener alfil en g5 comiendo en d8: distancia de g5 a d8 es diagonal de 3 pasos (g5 -> f6 -> e7 -> d8)
            // Entonces el rey negro debe estar por ejemplo en e8 o d7 para recibir el jaque sin estar en la casilla de llegada.
            Board board = new Board(Map.of(
                    Square.of("d1"), WhiteRook,
                    Square.of("e8"), BlackKing,   // Rey en e8 (no en d8)
                    Square.of("g5"), BlackBishop, // Ataca la diagonal d8-g5 (d8, e7, f6, g5)
                    Square.of("a1"), WhiteKing
            ));

            Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());

            // La torre mueve a d8 dando jaque horizontal al rey en e8
            Game afterMove = game.play(Square.of("d1"), Square.of("d8"));

            assertEquals(CHECK, afterMove.status(), "El alfil negro en g5 puede comer en d8 salvando el mate");
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