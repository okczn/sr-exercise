package okczn.scoreboard.domain;

import java.util.List;
import java.util.UUID;

public interface MatchRepository {
    Match byId(UUID matchId);

    void store(Match match);

    List<Match> matchesInProgress();
}
