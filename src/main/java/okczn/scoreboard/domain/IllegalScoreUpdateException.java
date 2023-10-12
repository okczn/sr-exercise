package okczn.scoreboard.domain;

import static java.lang.String.format;

public class IllegalScoreUpdateException extends RuntimeException {
    IllegalScoreUpdateException(Score from, Score to) {
        super(format("Cannot update score from %s to %s; new score must be higher", from, to));
    }
}
