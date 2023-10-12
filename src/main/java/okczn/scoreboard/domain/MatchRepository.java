package okczn.scoreboard.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for the Match aggregate root.
 */
public interface MatchRepository {
    /**
     * Retrieves a Match by its id, as returned by {@link Match#id()}.
     * The result is optional and can be empty if there is no match with this id.
     *
     * @param matchId the entity id
     * @return an optional entity if found, empty optional if not found
     */
    Optional<Match> byId(UUID matchId);

    /**
     * Stores a match. Once stored, the entity must be accessible
     * by {@link #byId(UUID)} with the argument equal to {@link Match#id()}.
     * If the entity already exists in this repository, it must be updated.
     *
     * @param match the entity to store
     */
    void store(Match match);

    /**
     * Retrieves the matches that are not finished, i.e. for which {@link Match#finished()} returns false.
     * Matches must be sorted by highest score (total number of goals) and earliest start time.
     *
     * @return a list of matches, empty if none found, never null
     */
    List<Match> matchesInProgress();
}
