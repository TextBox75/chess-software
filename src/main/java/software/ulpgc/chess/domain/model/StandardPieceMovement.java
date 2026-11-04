package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceMovement;

public enum StandardPieceMovement implements PieceMovement {

    Knight {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return (distance.absX() == 1 && distance.absY() == 2) || (distance.absX() == 2 && distance.absY() == 1);
        }
    },

    Bishop {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return distance.isDiagonal();
        }
    },

    Rook {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return distance.isOrthogonal();
        }
    },

    Queen {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return distance.isOrthogonal() || distance.isDiagonal();
        }
    },

    King {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return Math.max(distance.absX(), distance.absY()) == 1;
        }
    },

    Pawn {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = from.distanceTo(to);
            return distance.absX() == 0 && (distance.absY() == 1 || distance.absY() == 2);
        }
    };
}