package okczn.scoreboard.infrastructure;

import okczn.scoreboard.domain.Match;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void shouldListMatchesInProgress() throws Exception {
        // given
        var nedEng = Match.start("Netherlands", "England");
        Thread.sleep(1);
        var sweCro = Match.start("Sweden", "Croatia");
        Thread.sleep(1);
        var mexCan = Match.start("Mexico", "Canada");
        Thread.sleep(1);
        var spaBra = Match.start("Spain", "Brazil");
        Thread.sleep(1);
        var gerFra = Match.start("Germany", "France");
        Thread.sleep(1);
        var uruIta = Match.start("Uruguay", "Italy");
        Thread.sleep(1);
        var argAus = Match.start("Argentina", "Australia");

        mexCan.updateScore(0, 5);
        spaBra.updateScore(10, 2);
        gerFra.updateScore(2, 2);
        uruIta.updateScore(6, 6);
        argAus.updateScore(3, 1);

        nedEng.finish();
        sweCro.finish();

        repository.store(argAus);
        repository.store(uruIta);
        repository.store(gerFra);
        repository.store(spaBra);
        repository.store(mexCan);
        repository.store(sweCro);
        repository.store(nedEng);

        // when
        var matchesInProgress = repository.matchesInProgress();

        // then
        assertEquals(List.of(uruIta, spaBra, mexCan, argAus, gerFra), matchesInProgress);
    }
}
