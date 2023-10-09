package okczn.scoreboard.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {
    Optional<Match> byId(UUID matchId);

    void store(Match match);

    List<Match> matchesInProgress();
}
