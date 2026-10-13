package software.ulpgc.chess.domain.model;

public enum Rank {
    R1('1'),
    R2('2'),
    R3('3'),
    R4('4'),
    R5('5'),
    R6('6'),
    R7('7'),
    R8('8');

    private final char symbol;

    Rank(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return this.symbol;
    }
}
