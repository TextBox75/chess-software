package software.ulpgc.chess.architecture.domain;

import java.util.Arrays;
import java.util.List;

public record Square(File file, Rank rank) {
    public static Square from(File file, Rank rank) {
        return null;
    }

    public static Square of(String value) {
        return new Square(fileIn(value), rankIn(value));
    }

    public static Square of(int i, int j) {
        return new Square(File.values()[i], Rank.values()[j]);
    }

    private static File fileIn(String value) {
        return fileIn(value.charAt(0));
    }

    private static File fileIn(char c) {
        return Arrays.stream(File.values())
                .filter(file -> file.symbol() == c)
                .findFirst()
                .orElse(null);
    }

    private static Rank rankIn(String value) {
        return rankIn(value.charAt(1));
    }

    private static Rank rankIn(char c) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.symbol() == c)
                .findFirst()
                .orElse(null);
    }

    public static List<Square> allSquares() {
        return Arrays.stream(File.values())
                .flatMap(file -> Arrays.stream(Rank.values()).map(rank -> new Square(file, rank)))
                .toList();
    }
}