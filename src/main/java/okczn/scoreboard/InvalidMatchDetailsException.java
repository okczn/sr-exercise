package okczn.scoreboard;

public class InvalidMatchDetailsException extends RuntimeException {
    InvalidMatchDetailsException(Exception cause) {
        super(cause);
    }
}
