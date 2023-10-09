package okczn.scoreboard.infrastructure;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryMatchRepository implements MatchRepository {
    private Map<UUID, Match> storage = new HashMap<>();

    @Override
    public void store(Match match) {
        storage.put(match.id(), match);
    }

    @Override
    public Optional<Match> byId(UUID matchId) {
        return Optional.ofNullable(storage.get(matchId));
    }

    @Override
    public List<Match> matchesInProgress() {
        return null;
    }
}
