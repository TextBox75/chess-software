package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.ulpgc.chess.architecture.domain.Board;
import software.ulpgc.chess.architecture.domain.CastlingRights;
import software.ulpgc.chess.architecture.domain.MoveValidator;
import software.ulpgc.chess.architecture.domain.Piece;
import software.ulpgc.chess.architecture.domain.Position;
import software.ulpgc.chess.architecture.domain.Side;
import software.ulpgc.chess.architecture.domain.Square;
import software.ulpgc.chess.architecture.domain.StandardBoardFactory;
import software.ulpgc.chess.architecture.domain.State;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static software.ulpgc.chess.architecture.domain.Piece.*;

@DisplayName("Tests unitarios rigurosos para MoveValidator sobre Position")
class BoardMoveTest {

    // ---- Helpers: Position = Board + State(side, rights) ----

    private static Position positionOf(Map<Square, Piece> pieces) {
        return positionOf(pieces, Side.WHITE);
    }

    private static Position positionOf(Map<Square, Piece> pieces, Side sideToMove) {
        return new Position(new Board(pieces), new State(sideToMove, CastlingRights.initial()));
    }

    private static Position initialPosition(Side sideToMove) {
        return new Position(StandardBoardFactory.createBoard(), State.initial());
    }

    private static Piece at(Position position, Square square) {
        return position.board().pieces().get(square);
    }

    private static int pieceCount(Position position) {
        return position.board().pieces().size();
    }

    @Nested
    @DisplayName("Tablero inicial")
    class InitialBoardTests {

        @Test
        @DisplayName("El tablero inicial debe contener exactamente 32 piezas y posiciones clave")
        void initial_board_should_contain_32_pieces() {
            Board board = StandardBoardFactory.createBoard();

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
            Position position = initialPosition(Side.WHITE);
            Square from = Square.of("e2");
            Square to = Square.of("e4");

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from), "E2 debe quedar vacía");
            assertEquals(WhitePawn, at(next, to), "E4 debe tener el peón blanco");
        }

        @Test
        @DisplayName("Debe permitir el avance inicial de un peón negro a dos casillas hacia abajo")
        void should_allow_black_pawn_double_step_from_initial_position() {
            Position position = initialPosition(Side.BLACK);
            Square from = Square.of("d7");
            Square to = Square.of("d5");

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from));
            assertEquals(BlackPawn, at(next, to));
        }

        @Test
        @DisplayName("Debe permitir al caballo saltar los peones en la apertura")
        void should_allow_knight_to_jump_in_opening() {
            Position position = initialPosition(Side.WHITE);
            Square from = Square.of("b1");
            Square to = Square.of("c3");

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from));
            assertEquals(WhiteKnight, at(next, to));
            assertEquals(WhitePawn, at(next, Square.of("b2")), "El peón en B2 no debe alterarse");
        }

        @Test
        @DisplayName("No debe permitir mover una torre en la apertura porque los peones la bloquean")
        void should_not_allow_rook_to_move_through_pawns_in_opening() {
            Position position = initialPosition(Side.WHITE);
            Square from = Square.of("a1");
            Square to = Square.of("a3");

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhiteRook, at(next, from));
            assertNull(at(next, to));
        }
    }

    @Nested
    @DisplayName("Movimientos simples en tablero despejado por tipo de pieza")
    class PieceMovementTests {

        @Test
        @DisplayName("Dama: debe moverse en las 4 direcciones ortogonales y las 4 diagonales")
        void should_move_queen_in_all_eight_directions() {
            Square center = Square.of("d4");
            Position position = positionOf(Map.of(center, WhiteQueen));

            assertNotNull(at(MoveValidator.move(position, center, Square.of("d8")), Square.of("d8")), "Norte");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("d1")), Square.of("d1")), "Sur");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("h4")), Square.of("h4")), "Este");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("a4")), Square.of("a4")), "Oeste");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("g7")), Square.of("g7")), "Noreste");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("a7")), Square.of("a7")), "Noroeste");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("g1")), Square.of("g1")), "Sureste");
            assertNotNull(at(MoveValidator.move(position, center, Square.of("a1")), Square.of("a1")), "Suroeste");
        }

        @Test
        @DisplayName("Alfil: no debe permitir movimientos ortogonales")
        void should_reject_orthogonal_moves_for_bishop() {
            Square from = Square.of("c1");
            Square invalidTo = Square.of("c5");
            Position position = positionOf(Map.of(from, WhiteBishop));

            Position next = MoveValidator.move(position, from, invalidTo);

            assertEquals(WhiteBishop, at(next, from));
            assertNull(at(next, invalidTo));
        }

        @Test
        @DisplayName("Torre: no debe permitir movimientos diagonales")
        void should_reject_diagonal_moves_for_rook() {
            Square from = Square.of("a1");
            Square invalidTo = Square.of("d4");
            Position position = positionOf(Map.of(from, WhiteRook));

            Position next = MoveValidator.move(position, from, invalidTo);

            assertEquals(WhiteRook, at(next, from));
            assertNull(at(next, invalidTo));
        }

        @Test
        @DisplayName("Rey: solo debe poder moverse exactamente 1 casilla")
        void should_allow_king_only_one_square() {
            Square from = Square.of("e4");
            Position position = positionOf(Map.of(from, WhiteKing));

            // Válido: 1 paso diagonal
            Position valid = MoveValidator.move(position, from, Square.of("f5"));
            assertEquals(WhiteKing, at(valid, Square.of("f5")));

            // Inválido: 2 pasos
            Position invalid = MoveValidator.move(position, from, Square.of("e6"));
            assertEquals(WhiteKing, at(invalid, from));
            assertNull(at(invalid, Square.of("e6")));
        }

        @Test
        @DisplayName("Caballo: debe poder saltar en sus 8 variantes en L")
        void should_allow_knight_all_eight_l_jumps() {
            Square center = Square.of("d4");
            Position position = positionOf(Map.of(center, WhiteKnight));

            String[] targets = {"e6", "f5", "f3", "e2", "c2", "b3", "b5", "c6"};
            for (String target : targets) {
                Square to = Square.of(target);
                Position next = MoveValidator.move(position, center, to);
                assertEquals(WhiteKnight, at(next, to), "Falló salto a " + target);
            }
        }

        @Test
        @DisplayName("Peón blanco: no debe retroceder")
        void should_not_allow_white_pawn_to_move_backwards() {
            Square from = Square.of("e4");
            Square to = Square.of("e3");
            Position position = positionOf(Map.of(from, WhitePawn));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhitePawn, at(next, from));
            assertNull(at(next, to));
        }

        @Test
        @DisplayName("Peón blanco: no debe avanzar 2 pasos si no está en su fila inicial R2")
        void should_not_allow_white_pawn_double_step_from_rank_other_than_r2() {
            Square from = Square.of("e3");
            Square to = Square.of("e5");
            Position position = positionOf(Map.of(from, WhitePawn));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhitePawn, at(next, from));
            assertNull(at(next, to));
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

            Position position = positionOf(Map.of(
                    from, WhiteRook,
                    to, BlackBishop
            ));

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from));
            assertEquals(WhiteRook, at(next, to));
            assertEquals(1, pieceCount(next), "Solo debe quedar una pieza tras la captura");
        }

        @Test
        @DisplayName("No debe permitir mover sobre una pieza aliada (fuego amigo)")
        void should_not_allow_capturing_friendly_piece() {
            Square from = Square.of("a1");
            Square to = Square.of("a2");

            Position position = positionOf(Map.of(
                    from, WhiteRook,
                    to, WhitePawn
            ));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhiteRook, at(next, from));
            assertEquals(WhitePawn, at(next, to));
        }

        @Test
        @DisplayName("No debe permitir capturar directamente al rey rival")
        void should_not_allow_capturing_enemy_king() {
            Square from = Square.of("e1");
            Square to = Square.of("e8");

            Position position = positionOf(Map.of(
                    from, WhiteRook,
                    to, BlackKing
            ));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhiteRook, at(next, from));
            assertEquals(BlackKing, at(next, to));
        }

        @Test
        @DisplayName("Una pieza negra debe poder capturar a una pieza blanca")
        void should_allow_black_piece_to_capture_white_piece() {
            Square from = Square.of("c5");
            Square to = Square.of("e4");

            Position position = positionOf(Map.of(
                    from, BlackKnight,
                    to, WhitePawn
            ), Side.BLACK);

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from));
            assertEquals(BlackKnight, at(next, to));
            assertEquals(1, pieceCount(next));
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

            Position position = positionOf(Map.of(
                    from, WhiteRook,
                    blocker, BlackPawn
            ));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhiteRook, at(next, from));
            assertNull(at(next, to));
        }

        @Test
        @DisplayName("Torre: no debe moverse si hay una pieza en su trayectoria horizontal hacia la izquierda")
        void should_not_move_rook_if_path_is_blocked_horizontally_left() {
            Square from = Square.of("h4");
            Square blocker = Square.of("d4");
            Square to = Square.of("b4");

            Position position = positionOf(Map.of(
                    from, WhiteRook,
                    blocker, WhiteKnight
            ));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhiteRook, at(next, from));
            assertNull(at(next, to));
        }

        @Test
        @DisplayName("Alfil: no debe saltar piezas en diagonal descendente")
        void should_not_move_bishop_if_path_is_blocked_diagonally_down() {
            Square from = Square.of("f6");
            Square blocker = Square.of("d4");
            Square to = Square.of("b2");

            Position position = positionOf(Map.of(
                    from, BlackBishop,
                    blocker, WhitePawn
            ), Side.BLACK);

            Position next = MoveValidator.move(position, from, to);

            assertEquals(BlackBishop, at(next, from));
            assertNull(at(next, to));
        }

        @Test
        @DisplayName("Peón: no debe poder hacer doble avance si la casilla intermedia está ocupada")
        void should_not_allow_pawn_double_step_if_intermediate_square_is_blocked() {
            Square from = Square.of("e2");
            Square blocker = Square.of("e3");
            Square to = Square.of("e4");

            Position position = positionOf(Map.of(
                    from, WhitePawn,
                    blocker, BlackKnight
            ));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(WhitePawn, at(next, from));
            assertEquals(BlackKnight, at(next, blocker));
            assertNull(at(next, to));
        }

        @Test
        @DisplayName("Caballo: salta piezas intermedias tanto aliadas como enemigas")
        void should_confirm_knight_ignores_any_obstacle_in_between() {
            Square from = Square.of("d4");
            Square to = Square.of("e6");

            Position position = positionOf(Map.of(
                    from, WhiteKnight,
                    Square.of("d5"), WhitePawn,
                    Square.of("e5"), BlackPawn
            ));

            Position next = MoveValidator.move(position, from, to);

            assertNull(at(next, from));
            assertEquals(WhiteKnight, at(next, to));
        }
    }

    @Nested
    @DisplayName("Casos de borde y validaciones de entrada")
    class EdgeCasesTests {

        @Test
        @DisplayName("No debe hacer nada si la casilla de origen no tiene pieza (origen nulo/vacío)")
        void should_do_nothing_if_origin_square_is_empty() {
            Position position = positionOf(Map.of(Square.of("a1"), WhiteRook));
            Square emptyFrom = Square.of("e4");
            Square to = Square.of("e5");

            assertDoesNotThrow(() -> {
                Position next = MoveValidator.move(position, emptyFrom, to);
                assertEquals(1, pieceCount(next));
                assertNull(at(next, to));
                assertEquals(position, next);
            });
        }

        @Test
        @DisplayName("No debe hacer nada si la casilla origen es igual a la casilla destino")
        void should_not_modify_position_if_from_equals_to() {
            Square square = Square.of("d4");
            Position position = positionOf(Map.of(square, WhiteBishop));

            Position next = MoveValidator.move(position, square, square);

            assertEquals(WhiteBishop, at(next, square));
            assertEquals(position, next);
        }
    }

    @Nested
    @DisplayName("Estado de la posición (State) tras el movimiento")
    class StateTests {

        @Test
        @DisplayName("Un movimiento válido cambia el turno al otro bando")
        void valid_move_should_switch_side() {
            Position position = initialPosition(Side.WHITE);

            Position next = MoveValidator.move(position, Square.of("e2"), Square.of("e4"));

            assertEquals(Side.BLACK, next.state().side());
        }

        @Test
        @DisplayName("Un movimiento rechazado no cambia el turno ni ningún otro dato de la posición")
        void rejected_move_should_not_change_state() {
            Position position = initialPosition(Side.WHITE);

            Position next = MoveValidator.move(position, Square.of("a1"), Square.of("a3"));

            assertEquals(Side.WHITE, next.state().side());
            assertEquals(position, next);
        }

        @Test
        @DisplayName("Mover el rey elimina ambos derechos de enroque de su bando y conserva los del rival")
        void king_move_should_remove_own_castling_rights_only() {
            Position position = positionOf(Map.of(
                    Square.of("e1"), WhiteKing,
                    Square.of("e8"), BlackKing
            ));

            CastlingRights rights = MoveValidator
                    .move(position, Square.of("e1"), Square.of("e2"))
                    .state().castlingRights();

            assertFalse(rights.canCastle(Side.WHITE, true));
            assertFalse(rights.canCastle(Side.WHITE, false));
            assertTrue(rights.canCastle(Side.BLACK, true));
            assertTrue(rights.canCastle(Side.BLACK, false));
        }

        @Test
        @DisplayName("Mover una torre elimina solo el derecho de enroque de su flanco")
        void rook_move_should_remove_only_its_side_right() {
            Position position = positionOf(Map.of(
                    Square.of("e1"), WhiteKing,
                    Square.of("h1"), WhiteRook,
                    Square.of("a1"), WhiteRook,
                    Square.of("e8"), BlackKing
            ));

            CastlingRights rights = MoveValidator
                    .move(position, Square.of("h1"), Square.of("h2"))
                    .state().castlingRights();

            assertFalse(rights.canCastle(Side.WHITE, true));
            assertTrue(rights.canCastle(Side.WHITE, false));
        }
    }

    @Nested
    @DisplayName("Inmutabilidad del Record")
    class ImmutabilityTests {

        @Test
        @DisplayName("Un movimiento válido no debe alterar la posición original")
        void original_position_should_remain_unchanged_after_move() {
            Position initial = initialPosition(Side.WHITE);
            Square from = Square.of("d2");
            Square to = Square.of("d4");

            Position next = MoveValidator.move(initial, from, to);

            assertNotSame(initial, next);
            assertEquals(WhitePawn, at(initial, from), "El origen en la posición inicial debe seguir teniendo la pieza");
            assertNull(at(initial, to), "El destino en la posición inicial debe seguir vacío");
            assertEquals(Side.WHITE, initial.state().side(), "El turno de la posición original no debe cambiar");
        }

        @Test
        @DisplayName("Un movimiento rechazado debe preservar el mapa del tablero intacto")
        void rejected_move_must_preserve_board_state() {
            Square from = Square.of("a1");
            Square to = Square.of("h8"); // Diagonal ilegal para una torre
            Position position = positionOf(Map.of(from, WhiteRook));

            Position next = MoveValidator.move(position, from, to);

            assertEquals(position.board().pieces(), next.board().pieces());
        }
    }
}