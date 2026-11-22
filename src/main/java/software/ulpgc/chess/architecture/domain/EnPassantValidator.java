package software.ulpgc.chess.architecture.domain;

public class EnPassantValidator {
    public static boolean isEnPassantMove(Position position, Square from, Square to) {
        Board board = position.board();
        Side side = position.state().side();
        Piece piece = board.pieceAt(from);

        if (piece == null || !piece.isPawn() || !piece.isOfSide(side)) return false;

        SquareDistance distance = new SquareDistance(from, to);

        int expectedRow = (side == Side.WHITE) ? 4 : 3;

        return from.rank().ordinal() == expectedRow
                && distance.isDiagonal()
                && board.pieceAt(to) == null;
    }

    public static boolean isEnPassantLegal(Position position, Move currentMove, Move lastMove) {
        if (!isEnPassantMove(position, currentMove.from(), currentMove.to()) || lastMove == null) return false;

        Side side = position.state().side();
        Square capturedPawnSquare = Square.from(currentMove.from().file(), currentMove.to().rank());
        SquareDistance lastDistance = new SquareDistance(lastMove.from(), lastMove.to());

        return lastMove.piece().isPawn()
                && !lastMove.piece().isOfSide(side)
                && lastMove.to().equals(capturedPawnSquare)
                && lastDistance.absY() == 2;
    }

    public static Square capturedSquare(Square from, Square to) {
        return new Square(to.file(), from.rank());
    }
}