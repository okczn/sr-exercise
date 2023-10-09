package okczn.scoreboard.domain;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class Match {
    private static final String TEAM_REGEX = "(\\p{L}+|\\s)+";

    private final String homeTeam;
    private final String awayTeam;
    private Score score;
    private boolean finished;

    private Match(String homeTeam, String awayTeam, Score score) {
        validateTeam("home", homeTeam);
        validateTeam("away", awayTeam);

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = requireNonNull(score);
    }

    private void validateTeam(String label, String team) {
        if (StringUtils.isBlank(team) || !team.matches(TEAM_REGEX)) {
            throw new IllegalArgumentException(
                    format("Invalid %s team name: '%s'", label, team));
        }
    }

    public String homeTeam() {
        return homeTeam;
    }

    public String awayTeam() {
        return awayTeam;
    }

    public Score score() {
        return score;
    }

    public void updateScore(int home, int away) {
        var newScore = Score.of(home, away);
        if (!newScore.isHigherThan(score)) {
            throw new IllegalStateException(score + " cannot be updated to " + newScore);
        }
        score = newScore;
    }

    public boolean finished() {
        return finished;
    }

    public void finish() {
        finished = true;
    }

    public static Match start(String homeTeam, String awayTeam) {
        return new Match(homeTeam, awayTeam, Score.of(0, 0));
    }
}
