package okczn.scoreboard.infrastructure;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;

import java.util.List;
import java.util.UUID;

public class InMemoryMatchRepository implements MatchRepository {
    @Override
    public void store(Match match) {

    }

    @Override
    public Match byId(UUID matchId) {
        return null;
    }

    @Override
    public List<Match> matchesInProgress() {
        return null;
    }
}
