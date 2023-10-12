package okczn.scoreboard.domain;

/**
 * A value object representing a match score. Score is comparable by the total number of goals.
 */
public record Score(int home, int away) implements Comparable<Score> {
    public Score {
        if (home < 0 || away < 0) {
            throw new IllegalArgumentException("Score cannot contain negative values");
        }
    }

    public static Score of(int home, int away) {
        return new Score(home, away);
    }

    /**
     * Returns true if this score can replace 'other'.
     * Goals can be added, never deducted.
     *
     * @param other the score to check against
     * @return true if this score is considered higher, false otherwise
     */
    public boolean isHigherThan(Score other) {
        return !equals(other) && home >= other.home && away >= other.away;
    }

    @Override
    public String toString() {
        return home + ":" + away;
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(home + away, o.home + o.away);
    }
}
