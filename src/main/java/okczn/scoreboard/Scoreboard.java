package okczn.scoreboard;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;

import java.util.List;
import java.util.UUID;

/**
 * The scoreboard entry point.
 * Provides methods per use cases for the scoreboard library.
 */
public class Scoreboard {
    private final MatchRepository matchRepository;

    public Scoreboard(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void startMatch(String homeTeam, String awayTeam) {

    }

    public void updateScore(UUID matchId, int home, int away) {

    }

    public void finishMatch(UUID matchId) {

    }

    public List<Match> matchSummary() {
        return null;
    }
}
