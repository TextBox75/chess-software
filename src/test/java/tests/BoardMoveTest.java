package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.ulpgc.chess.domain.model.Board;
import software.ulpgc.chess.domain.model.Square;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static software.ulpgc.chess.domain.model.Piece.*;

@DisplayName("Tests unitarios rigurosos para Board")
class BoardMoveTest {

    @Nested
    @DisplayName("Tablero inicial")
    class InitialBoardTests {

        @Test
        @DisplayName("El tablero inicial debe contener exactamente 32 piezas y posiciones clave")
        void initial_board_should_contain_32_pieces() {
            Board board = Board.initial();

            assertEquals(32, board.pieces().size());
            assertEquals(WhiteRook, board.pieces().get(Square.of("a1")));
            assertEquals(WhiteKnight, board.pieces().get(Square.of("b1")));
            assertEquals(WhiteBishop, board.pieces().get(Square.of("c1")));
            assertEquals(WhiteQueen, board.pieces().get(Square.of("d1")));
            assertEquals(WhiteKing, board.pieces().get(Square.of("e1")));
            assertEquals(BlackKing, board.pieces().get(Square.of("e8")));
            assertEquals(BlackQueen, board.pieces().get(Square.of("d8")));
            assertEquals(BlackRook, board.pieces().get(Square.of("h8")));
        }

        @Test
        @DisplayName("Debe permitir el avance inicial de un peón a dos casillas")
        void should_allow_pawn_double_step_from_initial_position() {
            Board board = Board.initial();
            Square from = Square.of("e2");
            Square to = Square.of("e4");

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from), "E2 debe quedar vacía");
            assertEquals(WhitePawn, nextBoard.pieces().get(to), "E4 debe tener el peón blanco");
        }

        @Test
        @DisplayName("Debe permitir el avance inicial de un peón negro a dos casillas hacia abajo")
        void should_allow_black_pawn_double_step_from_initial_position() {
            Board board = Board.initial();
            Square from = Square.of("d7");
            Square to = Square.of("d5");

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from));
            assertEquals(BlackPawn, nextBoard.pieces().get(to));
        }

        @Test
        @DisplayName("Debe permitir al caballo saltar los peones en la apertura")
        void should_allow_knight_to_jump_in_opening() {
            Board board = Board.initial();
            Square from = Square.of("b1");
            Square to = Square.of("c3");

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from));
            assertEquals(WhiteKnight, nextBoard.pieces().get(to));
            assertEquals(WhitePawn, nextBoard.pieces().get(Square.of("b2")), "El peón en B2 no debe alterarse");
        }

        @Test
        @DisplayName("No debe permitir mover una torre en la apertura porque los peones la bloquean")
        void should_not_allow_rook_to_move_through_pawns_in_opening() {
            Board board = Board.initial();
            Square from = Square.of("a1");
            Square to = Square.of("a3");

            Board nextBoard = board.move(from, to);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertNull(nextBoard.pieces().get(to));
        }
    }

    @Nested
    @DisplayName("Movimientos simples en tablero despejado por tipo de pieza")
    class PieceMovementTests {

        @Test
        @DisplayName("Dama: debe moverse en las 4 direcciones ortogonales y las 4 diagonales")
        void should_move_queen_in_all_eight_directions() {
            Square center = Square.of("d4");
            Board board = new Board(Map.of(center, WhiteQueen));

            assertNotNull(board.move(center, Square.of("d8")).pieces().get(Square.of("d8")), "Norte");
            assertNotNull(board.move(center, Square.of("d1")).pieces().get(Square.of("d1")), "Sur");
            assertNotNull(board.move(center, Square.of("h4")).pieces().get(Square.of("h4")), "Este");
            assertNotNull(board.move(center, Square.of("a4")).pieces().get(Square.of("a4")), "Oeste");
            assertNotNull(board.move(center, Square.of("g7")).pieces().get(Square.of("g7")), "Noreste");
            assertNotNull(board.move(center, Square.of("a7")).pieces().get(Square.of("a7")), "Noroeste");
            assertNotNull(board.move(center, Square.of("g1")).pieces().get(Square.of("g1")), "Sureste");
            assertNotNull(board.move(center, Square.of("a1")).pieces().get(Square.of("a1")), "Suroeste");
        }

        @Test
        @DisplayName("Alfil: no debe permitir movimientos ortogonales")
        void should_reject_orthogonal_moves_for_bishop() {
            Square from = Square.of("c1");
            Square invalidTo = Square.of("c5");
            Board board = new Board(Map.of(from, WhiteBishop));

            Board nextBoard = board.move(from, invalidTo);

            assertEquals(WhiteBishop, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(invalidTo));
        }

        @Test
        @DisplayName("Torre: no debe permitir movimientos diagonales")
        void should_reject_diagonal_moves_for_rook() {
            Square from = Square.of("a1");
            Square invalidTo = Square.of("d4");
            Board board = new Board(Map.of(from, WhiteRook));

            Board nextBoard = board.move(from, invalidTo);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(invalidTo));
        }

        @Test
        @DisplayName("Rey: solo debe poder moverse exactamente 1 casilla")
        void should_allow_king_only_one_square() {
            Square from = Square.of("e4");
            Board board = new Board(Map.of(from, WhiteKing));

            // Válido: 1 paso diagonal
            Board valid = board.move(from, Square.of("f5"));
            assertEquals(WhiteKing, valid.pieces().get(Square.of("f5")));

            // Inválido: 2 pasos
            Board invalid = board.move(from, Square.of("e6"));
            assertEquals(WhiteKing, invalid.pieces().get(from));
            assertFalse(invalid.pieces().containsKey(Square.of("e6")));
        }

        @Test
        @DisplayName("Caballo: debe poder saltar en sus 8 variantes en L")
        void should_allow_knight_all_eight_l_jumps() {
            Square center = Square.of("d4");
            Board board = new Board(Map.of(center, WhiteKnight));

            String[] targets = {"e6", "f5", "f3", "e2", "c2", "b3", "b5", "c6"};
            for (String target : targets) {
                Square to = Square.of(target);
                Board next = board.move(center, to);
                assertEquals(WhiteKnight, next.pieces().get(to), "Falló salto a " + target);
            }
        }

        @Test
        @DisplayName("Peón blanco: no debe retroceder")
        void should_not_allow_white_pawn_to_move_backwards() {
            Square from = Square.of("e4");
            Square to = Square.of("e3");
            Board board = new Board(Map.of(from, WhitePawn));

            Board nextBoard = board.move(from, to);

            assertEquals(WhitePawn, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(to));
        }

        @Test
        @DisplayName("Peón blanco: no debe avanzar 2 pasos si no está en su fila inicial R2")
        void should_not_allow_white_pawn_double_step_from_rank_other_than_r2() {
            Square from = Square.of("e3");
            Square to = Square.of("e5");
            Board board = new Board(Map.of(from, WhitePawn));

            Board nextBoard = board.move(from, to);

            assertEquals(WhitePawn, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(to));
        }
    }

    @Nested
    @DisplayName("Reglas de ocupación (canOccupySquare)")
    class OccupySquareTests {

        @Test
        @DisplayName("Debe capturar una pieza enemiga y retirarla del mapa")
        void should_capture_enemy_piece() {
            Square from = Square.of("d4");
            Square to = Square.of("d7");

            Board board = new Board(Map.of(
                    from, WhiteRook,
                    to, BlackBishop
            ));

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from));
            assertEquals(WhiteRook, nextBoard.pieces().get(to));
            assertEquals(1, nextBoard.pieces().size(), "Solo debe quedar una pieza tras la captura");
        }

        @Test
        @DisplayName("No debe permitir mover sobre una pieza aliada (fuego amigo)")
        void should_not_allow_capturing_friendly_piece() {
            Square from = Square.of("a1");
            Square to = Square.of("a2");

            Board board = new Board(Map.of(
                    from, WhiteRook,
                    to, WhitePawn
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertEquals(WhitePawn, nextBoard.pieces().get(to));
        }

        @Test
        @DisplayName("No debe permitir capturar directamente al rey rival")
        void should_not_allow_capturing_enemy_king() {
            Square from = Square.of("e1");
            Square to = Square.of("e8");

            Board board = new Board(Map.of(
                    from, WhiteRook,
                    to, BlackKing
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertEquals(BlackKing, nextBoard.pieces().get(to));
        }

        @Test
        @DisplayName("Una pieza negra debe poder capturar a una pieza blanca")
        void should_allow_black_piece_to_capture_white_piece() {
            Square from = Square.of("c5");
            Square to = Square.of("e4");

            Board board = new Board(Map.of(
                    from, BlackKnight,
                    to, WhitePawn
            ));

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from));
            assertEquals(BlackKnight, nextBoard.pieces().get(to));
            assertEquals(1, nextBoard.pieces().size());
        }
    }

    @Nested
    @DisplayName("Obstáculos en el camino (isPathClear)")
    class PathClearTests {

        @Test
        @DisplayName("Torre: no debe moverse si hay una pieza en su trayectoria vertical hacia arriba")
        void should_not_move_rook_if_path_is_blocked_vertically_up() {
            Square from = Square.of("a1");
            Square blocker = Square.of("a3");
            Square to = Square.of("a5");

            Board board = new Board(Map.of(
                    from, WhiteRook,
                    blocker, BlackPawn
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(to));
        }

        @Test
        @DisplayName("Torre: no debe moverse si hay una pieza en su trayectoria horizontal hacia la izquierda")
        void should_not_move_rook_if_path_is_blocked_horizontally_left() {
            Square from = Square.of("h4");
            Square blocker = Square.of("d4");
            Square to = Square.of("b4");

            Board board = new Board(Map.of(
                    from, WhiteRook,
                    blocker, WhiteKnight
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(WhiteRook, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(to));
        }

        @Test
        @DisplayName("Alfil: no debe saltar piezas en diagonal descendente")
        void should_not_move_bishop_if_path_is_blocked_diagonally_down() {
            Square from = Square.of("f6");
            Square blocker = Square.of("d4");
            Square to = Square.of("b2");

            Board board = new Board(Map.of(
                    from, BlackBishop,
                    blocker, WhitePawn
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(BlackBishop, nextBoard.pieces().get(from));
            assertFalse(nextBoard.pieces().containsKey(to));
        }

        @Test
        @DisplayName("Peón: no debe poder hacer doble avance si la casilla intermedia está ocupada")
        void should_not_allow_pawn_double_step_if_intermediate_square_is_blocked() {
            Square from = Square.of("e2");
            Square blocker = Square.of("e3");
            Square to = Square.of("e4");

            Board board = new Board(Map.of(
                    from, WhitePawn,
                    blocker, BlackKnight
            ));

            Board nextBoard = board.move(from, to);

            assertEquals(WhitePawn, nextBoard.pieces().get(from));
            assertEquals(BlackKnight, nextBoard.pieces().get(blocker));
            assertFalse(nextBoard.pieces().containsKey(to));
        }

        @Test
        @DisplayName("Caballo: salta piezas intermedias tanto aliadas como enemigas")
        void should_confirm_knight_ignores_any_obstacle_in_between() {
            Square from = Square.of("d4");
            Square to = Square.of("e6");

            Board board = new Board(Map.of(
                    from, WhiteKnight,
                    Square.of("d5"), WhitePawn,
                    Square.of("e5"), BlackPawn
            ));

            Board nextBoard = board.move(from, to);

            assertFalse(nextBoard.pieces().containsKey(from));
            assertEquals(WhiteKnight, nextBoard.pieces().get(to));
        }
    }

    @Nested
    @DisplayName("Casos de borde y validaciones de entrada")
    class EdgeCasesTests {

        @Test
        @DisplayName("No debe hacer nada si la casilla de origen no tiene pieza (origen nulo/vacío)")
        void should_do_nothing_if_origin_square_is_empty() {
            Board board = new Board(Map.of(Square.of("a1"), WhiteRook));
            Square emptyFrom = Square.of("e4");
            Square to = Square.of("e5");

            assertDoesNotThrow(() -> {
                Board next = board.move(emptyFrom, to);
                assertEquals(1, next.pieces().size());
                assertFalse(next.pieces().containsKey(to));
            });
        }

        @Test
        @DisplayName("No debe hacer nada si la casilla origen es igual a la casilla destino")
        void should_not_modify_board_if_from_equals_to() {
            Square square = Square.of("d4");
            Board board = new Board(Map.of(square, WhiteBishop));

            Board nextBoard = board.move(square, square);

            assertEquals(WhiteBishop, nextBoard.pieces().get(square));
        }
    }

    @Nested
    @DisplayName("Inmutabilidad del Record")
    class ImmutabilityTests {

        @Test
        @DisplayName("Un movimiento válido no debe alterar el tablero original")
        void original_board_should_remain_unchanged_after_move() {
            Board initial = Board.initial();
            Square from = Square.of("d2");
            Square to = Square.of("d4");

            Board nextBoard = initial.move(from, to);

            assertNotSame(initial, nextBoard);
            assertEquals(WhitePawn, initial.pieces().get(from), "El origen en el tablero inicial debe seguir teniendo la pieza");
            assertFalse(initial.pieces().containsKey(to), "El destino en el tablero inicial debe seguir vacío");
        }

        @Test
        @DisplayName("Un movimiento rechazado debe preservar el mapa del tablero intacto")
        void rejected_move_must_preserve_board_state() {
            Square from = Square.of("a1");
            Square to = Square.of("h8"); // Diagonal ilegal para una torre
            Board board = new Board(Map.of(from, WhiteRook));

            Board next = board.move(from, to);

            assertEquals(board.pieces(), next.pieces());
        }
    }
}