package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceMovement;

public enum StandardPieceMovement implements PieceMovement {

    KnightMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return (distance.absX() == 1 && distance.absY() == 2) || (distance.absX() == 2 && distance.absY() == 1);
        }
    },

    BishopMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isDiagonal();
        }
    },

    RookMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isOrthogonal();
        }
    },

    QueenMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isOrthogonal() || distance.isDiagonal();
        }
    },

    KingMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return Math.max(distance.absX(), distance.absY()) == 1;
        }
    },

    WhitePawnMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            if (dist.dx() == 0 && dist.dy() == 1) return true;

            if (dist.dx() == 0 && dist.dy() == 2) {
                return from.rank() == Rank.R2;
            }

            return false;
        }
    },

    BlackPawnMovement {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);

            if (dist.dx() == 0 && dist.dy() == -1) return true;

            if (dist.dx() == 0 && dist.dy() == -2) {
                return from.rank() == Rank.R7;
            }

            return false;
        }
    }
}