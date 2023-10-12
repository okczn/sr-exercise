package okczn.scoreboard.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        assertNotNull(match.id());
        assertEquals("Mexico", match.homeTeam());
        assertEquals("Canada", match.awayTeam());
        assertEquals(Score.of(0, 0), match.score());
        assertFalse(match.finished());
    }

    @Test
    void shouldAllowUnicodeCharacters() {
        // when
        var match = Match.start("Śląsk Wrocław", "Breiðablik");

        // then
        assertNotNull(match);
        assertEquals("Śląsk Wrocław", match.homeTeam());
        assertEquals("Breiðablik", match.awayTeam());
    }

    @Test
    void shouldFailToResumeMatchWithoutId() {
        assertThrows(IllegalArgumentException.class,
                () -> Match.resume(null, now(), "Mexico", "Canada", Score.of(1, 1), false));
    }

    @ParameterizedTest
    @MethodSource("invalidTeams")
    void shouldFailToResumeWithInvalidHomeTeam(String home) {
        assertThrows(IllegalArgumentException.class,
                () -> Match.resume(randomUUID(), now(), home, "Canada", Score.of(1, 1), false));
    }

    @ParameterizedTest
    @MethodSource("invalidTeams")
    void shouldFailToResumeWithInvalidAwayTeam(String away) {
        assertThrows(IllegalArgumentException.class,
                () -> Match.resume(randomUUID(), now(), "Mexico", away, Score.of(1, 1), false));
    }

    @Test
    void shouldResumeMatch() {
        // given
        var id = randomUUID();

        // when
        var match = Match.resume(id, now(), "Mexico", "Canada", Score.of(3, 2), false);

        // then
        assertNotNull(match);
        assertEquals(id, match.id());
        assertEquals("Mexico", match.homeTeam());
        assertEquals("Canada", match.awayTeam());
        assertEquals(Score.of(3, 2), match.score());
    }

    @Test
    void shouldFailToSetLowerScore() {
        // given
        var match = Match.start("Spain", "Brazil");
        match.updateScore(1, 1);

        // then
        var exception = assertThrows(IllegalScoreUpdateException.class, () -> match.updateScore(1, 0));
        assertEquals("Cannot update score from 1:1 to 1:0; new score must be higher", exception.getMessage());
    }

    @Test
    void shouldFailToSetInconsistentScore() {
        // given
        var match = Match.start("Spain", "Brazil");
        match.updateScore(1, 0);

        // then
        var exception = assertThrows(IllegalScoreUpdateException.class, () -> match.updateScore(0, 1));
        assertEquals("Cannot update score from 1:0 to 0:1; new score must be higher", exception.getMessage());
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

    @Test
    void shouldBeEqualWhenIdIsEqual() {
        // given
        var match1 = Match.start("Mexico", "Canada");
        var match2 = Match.resume(match1.id(), now(), "Canada", "Mexico", Score.of(1, 1), false);

        // then
        assertEquals(match1, match2);
    }

    @Test
    void shouldBeDifferentWhenIdIsDifferent() {
        // given
        var match1 = Match.start("Mexico", "Canada");
        var match2 = Match.start("Mexico", "Canada");

        // then
        assertNotEquals(match1, match2);
    }

    @Test
    void shouldHaveSameHashCodesWhenIdEqual() {
        // given
        var match1 = Match.start("Mexico", "Canada");
        var match2 = Match.resume(match1.id(), now(), "Canada", "Mexico", Score.of(1, 1), false);

        // then
        assertEquals(match1.hashCode(), match2.hashCode());
    }
}
