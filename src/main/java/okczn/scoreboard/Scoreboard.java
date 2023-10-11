package okczn.scoreboard;

import okczn.scoreboard.domain.Match;
import okczn.scoreboard.domain.MatchRepository;
import okczn.scoreboard.infrastructure.InMemoryMatchRepository;

import java.util.List;
import java.util.UUID;

/**
 * The scoreboard entry point.
 * Provides methods per use cases for the scoreboard library.
 */
public class Scoreboard {
    private final MatchRepository matchRepository;

    public Scoreboard() {
        this(new InMemoryMatchRepository());
    }

    public Scoreboard(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public UUID startMatch(String homeTeam, String awayTeam) {
        var match = Match.start(homeTeam, awayTeam);
        matchRepository.store(match);
        return match.id();
    }

    public void updateScore(UUID matchId, int home, int away) {
        var match = matchRepository.byId(matchId).orElseThrow();
        match.updateScore(home, away);
        matchRepository.store(match);
    }

    public void finishMatch(UUID matchId) {
        var match = matchRepository.byId(matchId).orElseThrow();
        match.finish();
        matchRepository.store(match);
    }

    public List<ScoreboardEntry> matchSummary() {
        return matchRepository.matchesInProgress().stream()
                .map(match -> new ScoreboardEntry(
                        match.homeTeam(), match.score().home(), match.awayTeam(), match.score().away()))
                .toList();
    }
}
