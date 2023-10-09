package okczn.scoreboard.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreTest {
    @ParameterizedTest
    @MethodSource("higherThan10")
    void shouldBeHigher(Score score) {
        // given
        var otherScore = Score.of(1, 0);

        // then
        assertTrue(score.isHigherThan(otherScore));
    }

    static Stream<Score> higherThan10() {
        return Stream.of(
                Score.of(2, 0),
                Score.of(1, 1),
                Score.of(3, 2),
                Score.of(5, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("notHigherThan21")
    void shouldNotBeHigherIfEqual(Score score) {
        // given
        var otherScore = Score.of(2, 1);

        // then
        assertFalse(score.isHigherThan(otherScore));
    }

    static Stream<Score> notHigherThan21() {
        return Stream.of(
                Score.of(2, 1),
                Score.of(1, 2),
                Score.of(5, 0),
                Score.of(1, 1),
                Score.of(1, 5)
        );
    }

    @Test
    void shouldNotBeHigherThanItself() {
        // given
        var score = Score.of(1, 2);

        // then
        assertFalse(score.isHigherThan(score));
    }

    @Test
    void shouldCompareAsEqual() {
        // give
        var score = Score.of(1, 0);
        var otherScore = Score.of(0, 1);

        // then
        assertEquals(0, score.compareTo(otherScore));
    }

    @Test
    void shouldCompareAsGreater() {
        // give
        var score = Score.of(1, 1);
        var otherScore = Score.of(0, 1);

        // then
        assertEquals(1, score.compareTo(otherScore));
    }

    @Test
    void shouldCompareAsLesser() {
        // give
        var score = Score.of(1, 1);
        var otherScore = Score.of(2, 1);

        // then
        assertEquals(-1, score.compareTo(otherScore));
    }
}
