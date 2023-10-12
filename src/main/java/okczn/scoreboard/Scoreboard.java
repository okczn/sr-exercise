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

    /**
     * Initializes a Scoreboard with default in-memory storage.
     */
    public Scoreboard() {
        this(new InMemoryMatchRepository());
    }

    /**
     * Initializes a Scoreboard with given repository implementation.
     *
     * @param matchRepository the repository to use for storing matches.
     */
    public Scoreboard(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Creates and stores a new Match entity, representing a started match.
     * @param homeTeam the home team name
     * @param awayTeam the away team name
     * @return the new match identifier to reference this match in further calls
     */
    public UUID startMatch(String homeTeam, String awayTeam) {
        var match = Match.start(homeTeam, awayTeam);
        matchRepository.store(match);
        return match.id();
    }

    /**
     * Updates the current match score for a match referred by the id.
     * The score must be consistent with previous score, which means
     * that nether home nor away score can be lower than previously set.
     *
     * @param matchId the id of the match to update
     * @param home the home score to write
     * @param away the away score to write
     */
    public void updateScore(UUID matchId, int home, int away) {
        var match = matchRepository.byId(matchId).orElseThrow();
        match.updateScore(home, away);
        matchRepository.store(match);
    }

    /**
     * Marks a match as finished. Finished matches will not be returned
     * in the match summary.
     *
     * @param matchId the if of the match to finish
     */
    public void finishMatch(UUID matchId) {
        var match = matchRepository.byId(matchId).orElseThrow();
        match.finish();
        matchRepository.store(match);
    }

    /**
     * Returns the current ongoing match scores.
     *
     * @return a list of entries, each representing an ongoing match
     */
    public List<ScoreboardEntry> matchSummary() {
        return matchRepository.matchesInProgress().stream()
                .map(match -> new ScoreboardEntry(
                        match.homeTeam(), match.score().home(), match.awayTeam(), match.score().away()))
                .toList();
    }
}
