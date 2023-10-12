package okczn.scoreboard.integration;

import okczn.scoreboard.InvalidMatchDetailsException;
import okczn.scoreboard.Scoreboard;
import okczn.scoreboard.ScoreboardEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreboardIT {
    @Test
    void shouldAddUpdateAndGetScores() {
        // given
        var scoreboard = new Scoreboard();

        var id = scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore(id, 0, 5);

        id = scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore(id, 10, 2);

        id = scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore(id, 2, 2);

        id = scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore(id, 6, 6);

        id = scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore(id, 3, 1);

        id = scoreboard.startMatch("Netherlands", "England");
        scoreboard.updateScore(id, 1, 1);
        scoreboard.finishMatch(id);

        // when
        var summary = scoreboard.matchSummary();

        // then
        assertEquals(5, summary.size());
        assertEntry("Uruguay", "Italy", 6, 6, summary.get(0));
        assertEntry("Spain", "Brazil", 10, 2, summary.get(1));
        assertEntry("Mexico", "Canada", 0, 5, summary.get(2));
        assertEntry("Argentina", "Australia", 3, 1, summary.get(3));
        assertEntry("Germany", "France", 2, 2, summary.get(4));
    }

    private static void assertEntry(
            String homeTeam, String awayTeam, int homeScore, int awayScore, ScoreboardEntry entry) {

        var expected = List.of(
                homeTeam,
                String.valueOf(homeScore),
                awayTeam,
                String.valueOf(awayScore)
        );

        var actual = List.of(
                entry.homeTeam(),
                String.valueOf(entry.homeScore()),
                entry.awayTeam(),
                String.valueOf(entry.awayScore())
        );

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionOnInvalidTeamNames() {
        // given
        var scoreboard = new Scoreboard();

        // then
        assertThrows(InvalidMatchDetailsException.class, () -> scoreboard.startMatch("", ""));
    }

    @Test
    void shouldThrowExceptionOnFailedScoreUpdate() {
        // given
        var scoreboard = new Scoreboard();
        var id = scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore(id, 2, 2);

        // then
        assertThrows(InvalidMatchDetailsException.class, () -> scoreboard.updateScore(id, 1, 0));
    }
}
