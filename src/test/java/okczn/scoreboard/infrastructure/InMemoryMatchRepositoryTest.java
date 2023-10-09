package okczn.scoreboard.infrastructure;

import okczn.scoreboard.domain.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryMatchRepositoryTest {
    InMemoryMatchRepository repository = new InMemoryMatchRepository();

    @Test
    void shouldStoreAndRead() {
        // given
        var match = Match.start("Portugal", "San Marino");

        // then
        repository.store(match);
        assertEquals(match, repository.byId(match.id()).orElseThrow());
    }
}
