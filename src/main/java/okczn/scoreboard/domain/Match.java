package okczn.scoreboard.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

public class Match {
    private static final String TEAM_REGEX = "(\\p{L}+|\\s)+";

    private final UUID id;
    private final LocalDateTime startTime;
    private final String homeTeam;
    private final String awayTeam;
    private Score score;
    private boolean finished;

    private Match(UUID id, LocalDateTime startTime, String homeTeam, String awayTeam, Score score, boolean finished) {
        if (id == null) throw new IllegalArgumentException("Non-null id required");
        if (score == null) throw new IllegalArgumentException("Non-null score required");
        validateTeam("home", homeTeam);
        validateTeam("away", awayTeam);

        this.id = id;
        this.startTime = startTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = score;
        this.finished = finished;
    }

    private void validateTeam(String label, String team) {
        if (StringUtils.isBlank(team) || !team.matches(TEAM_REGEX)) {
            throw new IllegalArgumentException(
                    format("Invalid %s team name: '%s'", label, team));
        }
    }

    public UUID id() {
        return id;
    }

    public LocalDateTime startTime() {
        return startTime;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Match otherMatch) {
            return id.equals(otherMatch.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return format("%s %s %s%s", homeTeam, score, awayTeam, finished ? " (finished)" : "");
    }

    public static Match start(String homeTeam, String awayTeam) {
        return new Match(randomUUID(), LocalDateTime.now(), homeTeam, awayTeam, Score.of(0, 0), false);
    }

    /**
     * Allows to recreate a Match in a repository that needs to restore an entity
     * from another form, e.g. serialized.
     */
    public static Match resume(
            UUID id, LocalDateTime startTime, String homeTeam, String awayTeam, Score score, boolean finished) {
        return new Match(id, startTime, homeTeam, awayTeam, score, finished);
    }
}
