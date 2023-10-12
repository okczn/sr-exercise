package okczn.scoreboard;

/**
 * Represents a match to be shown on the match summary.
 */
public record ScoreboardEntry(
        String homeTeam,
        int homeScore,
        String awayTeam,
        int awayScore
) {
    @Override
    public String toString() {
        return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
    }
}
