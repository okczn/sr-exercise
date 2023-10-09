package okczn.scoreboard.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchTest {
    @ParameterizedTest
    @MethodSource("invalidTeams")
    void shouldFailToStartMatchWithInvalidHomeTeam(String home) {
        var exception = assertThrows(IllegalArgumentException.class, () -> Match.start(home, "Canada"));
        assertEquals(format("Invalid home team name: '%s'", home), exception.getMessage());
    }

    private static Stream<String> invalidTeams() {
        return Stream.of(null, "", " ", "_", "1", "_mexico_", "canada1", "Canada!");
    }

    @ParameterizedTest
    @MethodSource("invalidTeams")
    void shouldFailToStartMatchWithInvalidAwayTeam(String away) {
        var exception = assertThrows(IllegalArgumentException.class, () -> Match.start("Mexico", away));
        assertEquals(format("Invalid away team name: '%s'", away), exception.getMessage());
    }

    @Test
    void shouldStartNewMatch() {
        // when
        var match = Match.start("Mexico", "Canada");

        // then
        assertNotNull(match);
        assertEquals("Mexico", match.homeTeam());
        assertEquals("Canada", match.awayTeam());
        assertEquals(Score.of(0, 0), match.score());
        assertFalse(match.finished());
    }

    @Test
    void shouldStartMatchWithUnicodeCharacters() {
        // when
        var match = Match.start("Śląsk Wrocław", "Breiðablik");

        // then
        assertNotNull(match);
        assertEquals("Śląsk Wrocław", match.homeTeam());
        assertEquals("Breiðablik", match.awayTeam());
        assertEquals(Score.of(0, 0), match.score());
        assertFalse(match.finished());
    }

    @Test
    void shouldFailToSetLowerScore() {
        // given
        var match = Match.start("Spain", "Brazil");
        match.updateScore(1, 1);

        // then
        var exception = assertThrows(IllegalStateException.class, () -> match.updateScore(1, 0));
        assertEquals("1:1 cannot be updated to 1:0", exception.getMessage());
    }

    @Test
    void shouldFailToSetInconsistentScore() {
        // given
        var match = Match.start("Spain", "Brazil");
        match.updateScore(1, 0);

        // then
        var exception = assertThrows(IllegalStateException.class, () -> match.updateScore(0, 1));
        assertEquals("1:0 cannot be updated to 0:1", exception.getMessage());
    }

    @Test
    void shouldAllowToSkipIntermediateScore() {
        // given
        var match = Match.start("Spain", "Brazil");
        match.updateScore(1, 0);

        // when
        match.updateScore(2, 1);

        // then
        assertEquals(Score.of(2, 1), match.score());
    }

    @Test
    void shouldUpdateScore() {
        // given
        var match = Match.start("Mexico", "Canada");

        // when
        match.updateScore(1, 0);

        // then
        assertEquals(Score.of(1, 0), match.score());
    }

    @Test
    void shouldFinish() {
        // given
        var match = Match.start("Mexico", "Canada");

        // when
        match.finish();

        // then
        assertTrue(match.finished());
    }
}
