package okczn.scoreboard;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldUpdateScore() {
        // given
        var matchId = randomUUID();
        var match = new Match();
        given(matchRepository.byId(matchId)).willReturn(match);

        // when
        scoreboard.updateScore(matchId, 1, 0);

        // then
        verify(matchRepository).store(matchCaptor.capture());
    }

    @Test
    void shouldFinishAndStoreMatch() {
        // given
        var matchId = randomUUID();

        // when
        scoreboard.finishMatch(matchId);

        // then
        verify(matchRepository).store(matchCaptor.capture());
    }

    @Test
    void shouldGetMatchSummary() {
        // given
        var matches = List.of(
                new Match(),
                new Match(),
                new Match(),
                new Match(),
                new Match()
        );
        given(matchRepository.matchesInProgress()).willReturn(matches);

        // when
        List<Match> summary = scoreboard.matchSummary();

        // then
        assertEquals(5, summary.size());
    }
}
