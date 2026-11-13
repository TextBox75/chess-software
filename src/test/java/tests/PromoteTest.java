package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.ulpgc.chess.domain.model.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static software.ulpgc.chess.domain.model.GameStatus.CHECK;
import static software.ulpgc.chess.domain.model.GameStatus.PLAYING;
import static software.ulpgc.chess.domain.model.Piece.*;
import static software.ulpgc.chess.domain.model.Rank.*;

class PromoteTest {

    @Test
    @DisplayName("Unitario: canPromote y defaultPromote en Piece funcionan según color y fila")
    void should_validate_piece_promotion_rules() {
        // Peón blanco solo promociona en R8 a Dama blanca
        assertTrue(WhitePawn.canPromote(R8));
        assertFalse(WhitePawn.canPromote(R7));
        assertFalse(WhitePawn.canPromote(R1));
        assertEquals(WhiteQueen, WhitePawn.defaultPromote());

        // Peón negro solo promociona en R1 a Dama negra
        assertTrue(BlackPawn.canPromote(R1));
        assertFalse(BlackPawn.canPromote(R2));
        assertFalse(BlackPawn.canPromote(R8));
        assertEquals(BlackQueen, BlackPawn.defaultPromote());

        // Otras piezas nunca promocionan
        assertFalse(WhiteKnight.canPromote(R8));
        assertFalse(BlackRook.canPromote(R1));
    }

    @Test
    @DisplayName("Peón blanco corona automáticamente a Dama al avanzar a la octava fila")
    void white_pawn_should_promote_to_queen_on_eighth_rank() {
        Board board = new Board(Map.of(
                Square.of("a7"), WhitePawn,
                Square.of("e1"), WhiteKing,
                Square.of("e8"), BlackKing
        ));

        Board afterMove = board.move(Square.of("a7"), Square.of("a8"));

        assertEquals(WhiteQueen, afterMove.pieces().get(Square.of("a8")));
        assertNull(afterMove.pieces().get(Square.of("a7")));
    }

    @Test
    @DisplayName("Peón negro corona automáticamente a Dama al avanzar a la primera fila")
    void black_pawn_should_promote_to_queen_on_first_rank() {
        Board board = new Board(Map.of(
                Square.of("h2"), BlackPawn,
                Square.of("e1"), WhiteKing,
                Square.of("e8"), BlackKing
        ));

        Board afterMove = board.move(Square.of("h2"), Square.of("h1"));

        assertEquals(BlackQueen, afterMove.pieces().get(Square.of("h1")));
        assertNull(afterMove.pieces().get(Square.of("h2")));
    }

    @Test
    @DisplayName("Peón corona correctamente al capturar en diagonal en la última fila")
    void pawn_should_promote_when_capturing_on_promotion_rank() {
        Board board = new Board(Map.of(
                Square.of("b7"), WhitePawn,
                Square.of("c8"), BlackRook,
                Square.of("e1"), WhiteKing,
                Square.of("e8"), BlackKing
        ));

        Board afterCapture = board.move(Square.of("b7"), Square.of("c8"));

        assertEquals(WhiteQueen, afterCapture.pieces().get(Square.of("c8")));
        assertNull(afterCapture.pieces().get(Square.of("b7")));
    }

    @Test
    @DisplayName("La nueva Dama recién coronada da jaque inmediatamente al rey rival")
    void promoted_pawn_should_immediately_give_check_as_queen() {
        // Al coronar en a8, la nueva dama blanca ataca horizontalmente al rey negro en e8
        Board board = new Board(Map.of(
                Square.of("a7"), WhitePawn,
                Square.of("e1"), WhiteKing,
                Square.of("e8"), BlackKing
        ));

        Game game = new Game(board, Side.WHITE, PLAYING, ChessClock.initial());
        Game afterPromotion = game.play(Square.of("a7"), Square.of("a8"));

        assertEquals(WhiteQueen, afterPromotion.board().pieces().get(Square.of("a8")));
        assertEquals(CHECK, afterPromotion.status(), "La dama recién coronada debe dar jaque en la fila 8");
    }
}