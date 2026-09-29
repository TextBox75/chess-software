package software.ulpgc.chess;

import java.text.ParseException;
import java.util.Objects;

public record Square(File file, Rank rank) {
    public static Square from(File file, Rank rank) {
        return null;
    }

    private static class ParseException extends Exception {
        public ParseException(String s) {

        }
    }
}