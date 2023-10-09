package okczn.scoreboard.domain;

public record Score(int home, int away) implements Comparable<Score> {
    public Score {
        if (home < 0 || away < 0) {
            throw new IllegalArgumentException("Score cannot contain negative values");
        }
    }

    public static Score of(int home, int away) {
        return new Score(home, away);
    }

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
