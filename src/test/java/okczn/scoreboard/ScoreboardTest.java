package okczn.scoreboard;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;
import okczn.scoreboard.domain.Score;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class ScoreboardTest {
    Scoreboard scoreboard;

    @Mock
    MatchRepository matchRepository;
    @Captor
    ArgumentCaptor<Match> matchCaptor;

    AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = openMocks(this);
        scoreboard = new Scoreboard(matchRepository);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    @Test
    void shouldStartMatch() {
        // when
        scoreboard.startMatch("Mexico", "Canada");

        // then
        verify(matchRepository).store(matchCaptor.capture());
    }

    @Test
    void shouldThrowExceptionWhenUpdatedMatchNotFound() {
        assertThrows(NoSuchElementException.class,
                () -> scoreboard.updateScore(randomUUID(), 1, 1));
    }

    @Test
    void shouldUpdateScore() {
        // given
        var matchId = randomUUID();
        var match = Match.start("Germany", "France");
        given(matchRepository.byId(matchId)).willReturn(Optional.of(match));

        // when
        scoreboard.updateScore(matchId, 1, 0);

        // then
        verify(matchRepository).store(matchCaptor.capture());
    }

    @Test
    void shouldThrowExceptionWhenFinishedMatchNotFound() {
        assertThrows(NoSuchElementException.class,
                () -> scoreboard.finishMatch(randomUUID()));
    }

    @Test
    void shouldFinishAndStoreMatch() {
        // given
        var match = Match.start("Germany", "France");
        given(matchRepository.byId(match.id())).willReturn(Optional.of(match));

        // when
        scoreboard.finishMatch(match.id());

        // then
        verify(matchRepository).store(matchCaptor.capture());
    }

    @Test
    void shouldGetMatchSummary() {
        // given
        var matches = List.of(
                Match.resume(randomUUID(), now(), "Belgium", "Netherlands", Score.of(2, 2), false),
                Match.resume(randomUUID(), now(), "Italy", "Argentina", Score.of(3, 1), false),
                Match.resume(randomUUID(), now(), "Scotland", "Spain", Score.of(0, 1), false)
        );
        given(matchRepository.matchesInProgress()).willReturn(matches);

        // when
        List<ScoreboardEntry> summary = scoreboard.matchSummary();

        // then
        assertNotNull(summary);
        assertEquals(3, summary.size());
        assertEquals("Belgium 2 - Netherlands 2", summary.get(0).toString());
        assertEquals("Italy 3 - Argentina 1", summary.get(1).toString());
        assertEquals("Scotland 0 - Spain 1", summary.get(2).toString());
    }
}
