package okczn.scoreboard.domain;

public record Score(int home, int away) {
    public static Score of(int home, int away) {
        return new Score(home, away);
    }
}
