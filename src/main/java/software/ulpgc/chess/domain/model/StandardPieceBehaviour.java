package software.ulpgc.chess.domain.model;

import software.ulpgc.chess.architecture.ports.PieceBehaviour;

public enum StandardPieceBehaviour implements PieceBehaviour {

    KnightBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return (distance.absX() == 1 && distance.absY() == 2) ||
                    (distance.absX() == 2 && distance.absY() == 1);
        }
    },

    BishopBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isDiagonal();
        }
    },

    RookBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isOrthogonal();
        }
    },

    QueenBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return distance.isOrthogonal() || distance.isDiagonal();
        }
    },

    KingBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance distance = new SquareDistance(from, to);
            return Math.max(distance.absX(), distance.absY()) == 1;
        }
    },

    WhitePawnBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isVertical() && dist.Y() > 0 && dist.Y() <= (from.rank() == Rank.R2 ? 2 : 1);
        }

        @Override
        public boolean isAttackValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isDiagonal() && dist.Y() == 1;
        }
    },

    BlackPawnBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isVertical() && dist.Y() < 0 && dist.Y() >= (from.rank() == Rank.R7 ? -2 : -1);
        }

        @Override
        public boolean isAttackValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isDiagonal() && dist.Y() == -1;
        }
    }
}