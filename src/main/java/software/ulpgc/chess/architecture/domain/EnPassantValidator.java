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

    public static boolean isEnPassantLegal(Position position, Move move) {
        Piece victim = position.board().pieceAt(capturedSquare(move.from(), move.to()));
        return isEnPassantMove(position, move.from(), move.to())
                && victim != null && victim.isPawn() && !victim.isOfSide(move.piece().side());
    }

    public static Square capturedSquare(Square from, Square to) {
        return new Square(to.file(), from.rank());
    }
}