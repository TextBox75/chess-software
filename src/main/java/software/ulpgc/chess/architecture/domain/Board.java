package software.ulpgc.chess.architecture.domain;

import java.util.Map;

public record Board(Map<Square, Piece> pieces) {

    public Board(Map<Square, Piece> pieces) {
        this.pieces = Map.copyOf(pieces);
    }

    public boolean isPathClear(Piece piece, Square from, Square to) {
        if (piece.isKnight()) return true;
        return isPathClear(from, to);
    }

    private boolean isPathClear(Square from, Square to) {
        SquareDistance distance = new SquareDistance(from, to);

        int stepX = Integer.signum(distance.X());
        int stepY = Integer.signum(distance.Y());

        int currentFile = from.file().ordinal() + stepX;
        int currentRank = from.rank().ordinal() + stepY;

        while (currentFile != to.file().ordinal() || currentRank != to.rank().ordinal()) {
            if (pieces.containsKey(Square.of(currentFile, currentRank))) return false;
            currentFile += stepX;
            currentRank += stepY;
        }

        return true;
    }

    public boolean canOccupySquare(Piece fromPiece, Square to) {
        Piece toPiece = pieces.get(to);
        return toPiece == null || (!toPiece.isKing() && fromPiece.isWhite() != toPiece.isWhite());
    }

    public boolean isInCheck(Side side) {
        Square kingSquare = findKingSquare(side);
        return isSquareAttacked(kingSquare, side.opposite());
    }

    private Square findKingSquare(Side side) {
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isKing() && entry.getValue().isOfSide(side))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Rey no encontrado en el tablero"));
    }

    public boolean isSquareAttacked(Square targetSquare, Side attackingSide) {
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isOfSide(attackingSide))
                .anyMatch(entry -> {
                    Square originSquare = entry.getKey();
                    Piece piece = entry.getValue();

                    return piece.getBehaviour().isAttackValid(originSquare, targetSquare)
                            && isPathClear(piece, originSquare, targetSquare);
                });
    }

    public Piece pieceAt(Square square) {
        return this.pieces.get(square);
    }
}
