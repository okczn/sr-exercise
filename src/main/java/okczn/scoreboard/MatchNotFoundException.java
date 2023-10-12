package okczn.scoreboard;

import java.util.UUID;

public class MatchNotFoundException extends RuntimeException {
    MatchNotFoundException(UUID matchId) {
        super(matchId == null ? "null" : matchId.toString());
    }
}
