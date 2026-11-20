package software.ulpgc.chess.architecture.domain;

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
            return dist.isVertical() && dist.y() > 0 && dist.y() <= (from.rank() == Rank.R2 ? 2 : 1);
        }

        @Override
        public boolean isAttackValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isDiagonal() && dist.y() == 1;
        }
    },

    BlackPawnBehaviour {
        @Override
        public boolean isMoveValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isVertical() && dist.y() < 0 && dist.y() >= (from.rank() == Rank.R7 ? -2 : -1);
        }

        @Override
        public boolean isAttackValid(Square from, Square to) {
            SquareDistance dist = new SquareDistance(from, to);
            return dist.isDiagonal() && dist.y() == -1;
        }
    }
}