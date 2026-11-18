package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.ulpgc.chess.architecture.domain.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static software.ulpgc.chess.architecture.domain.Piece.*;

class CastlingTest {

    /** Game -> Position(Board, State(side, rights)), con los derechos de enroque iniciales. */
    private static Game gameOf(Map<Square, Piece> pieces, Side sideToMove) {
        Board board = new Board(pieces);
        State state = new State(sideToMove, CastlingRights.initial());
        return new Game(new Position(board, state));
    }

    @Test
    @DisplayName("Enroque corto blanco (O-O): mueve rey a g1 y torre a f1")
    void white_kingside_castling_should_move_king_and_rook() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        Game afterMove = game.play(Square.of("e1"), Square.of("g1"));

        Map<Square, Piece> pieces = afterMove.position().board().pieces();
        assertEquals(WhiteKing, pieces.get(Square.of("g1")));
        assertEquals(WhiteRook, pieces.get(Square.of("f1")));
        assertNull(pieces.get(Square.of("e1")));
        assertNull(pieces.get(Square.of("h1")));
    }

    @Test
    @DisplayName("Tras enrocar cambia el turno y se pierden ambos derechos de enroque del bando")
    void castling_should_switch_side_and_remove_rights() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("a1"), WhiteRook,
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        State state = game.play(Square.of("e1"), Square.of("g1")).position().state();

        assertEquals(Side.BLACK, state.side());
        assertFalse(state.rights().canCastle(Side.WHITE, true));
        assertFalse(state.rights().canCastle(Side.WHITE, false));
    }

    @Test
    @DisplayName("Enroque largo negro (O-O-O): mueve rey a c8 y torre a d8")
    void black_queenside_castling_should_move_king_and_rook() {
        Game game = gameOf(Map.of(
                Square.of("e8"), BlackKing,
                Square.of("a8"), BlackRook,
                Square.of("e1"), WhiteKing
        ), Side.BLACK);

        Game afterMove = game.play(Square.of("e8"), Square.of("c8"));

        Map<Square, Piece> pieces = afterMove.position().board().pieces();
        assertEquals(BlackKing, pieces.get(Square.of("c8")));
        assertEquals(BlackRook, pieces.get(Square.of("d8")));
        assertNull(pieces.get(Square.of("e8")));
        assertNull(pieces.get(Square.of("a8")));
    }

    @Test
    @DisplayName("No se permite enrocar si hay piezas bloqueando el camino entre el rey y la torre")
    void should_not_castle_when_path_is_blocked() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("f1"), WhiteBishop, // Bloquea el paso
                Square.of("h1"), WhiteRook,
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        Game afterMove = game.play(Square.of("e1"), Square.of("g1"));

        // La posición completa (tablero y estado) no debe haber cambiado
        assertEquals(game.position(), afterMove.position());
    }

    @Test
    @DisplayName("No se permite enrocar si el rey está actualmente en jaque")
    void should_not_castle_while_in_check() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("e7"), BlackRook, // Da jaque directo al rey en e1
                Square.of("a8"), BlackKing
        ), Side.WHITE);

        Game afterMove = game.play(Square.of("e1"), Square.of("g1"));

        assertEquals(game.position(), afterMove.position());
    }

    @Test
    @DisplayName("No se permite enrocar si el rey cruza por una casilla atacada")
    void should_not_castle_through_attacked_square() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("f7"), BlackRook, // Ataca f1 (casilla de paso del enroque corto)
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        Game afterMove = game.play(Square.of("e1"), Square.of("g1"));

        assertEquals(game.position(), afterMove.position());
    }

    @Test
    @DisplayName("No se permite enrocar si el rey ya se había movido antes aunque regrese a e1")
    void should_not_castle_if_king_already_moved() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        // 1. El rey blanco se mueve a e2 y vuelve a e1
        game = game.play(Square.of("e1"), Square.of("e2"));
        game = game.play(Square.of("e8"), Square.of("d8"));
        game = game.play(Square.of("e2"), Square.of("e1"));
        game = game.play(Square.of("d8"), Square.of("e8"));

        // El derecho se perdió en el State, aunque las piezas estén como al inicio
        assertFalse(game.position().state().rights().canCastle(Side.WHITE, true));

        // 2. Intenta enrocar después de haber perdido el derecho
        Game afterAttempt = game.play(Square.of("e1"), Square.of("g1"));

        Map<Square, Piece> pieces = afterAttempt.position().board().pieces();
        assertEquals(WhiteKing, pieces.get(Square.of("e1")));
        assertNull(pieces.get(Square.of("g1")));
    }

    @Test
    @DisplayName("No se permite enrocar por un flanco si esa torre ya se movió, pero sí por el otro flanco")
    void should_lose_kingside_castling_if_kingside_rook_moved_but_keep_queenside() {
        Game game = gameOf(Map.of(
                Square.of("e1"), WhiteKing,
                Square.of("h1"), WhiteRook,
                Square.of("a1"), WhiteRook,
                Square.of("e8"), BlackKing
        ), Side.WHITE);

        // La torre de h1 se mueve y regresa a su sitio
        game = game.play(Square.of("h1"), Square.of("h2"));
        game = game.play(Square.of("e8"), Square.of("d8"));
        game = game.play(Square.of("h2"), Square.of("h1"));
        game = game.play(Square.of("d8"), Square.of("e8"));

        assertFalse(game.position().state().rights().canCastle(Side.WHITE, true));
        assertTrue(game.position().state().rights().canCastle(Side.WHITE, false));

        // Enroque corto (hacia g1) debe fallar
        Game failedKingside = game.play(Square.of("e1"), Square.of("g1"));
        assertEquals(WhiteKing, failedKingside.position().board().pieces().get(Square.of("e1")));

        // Enroque largo (hacia c1) sigue estando permitido
        Game validQueenside = game.play(Square.of("e1"), Square.of("c1"));
        Map<Square, Piece> pieces = validQueenside.position().board().pieces();
        assertEquals(WhiteKing, pieces.get(Square.of("c1")));
        assertEquals(WhiteRook, pieces.get(Square.of("d1")));
    }
}