package okczn.scoreboard.domain;

import java.util.List;
import java.util.UUID;

public interface MatchRepository {
    void store(Match match);

    Match byId(UUID matchId);

    List<Match> matchesInProgress();
}
