package okczn.scoreboard.domain;

public class Match {
    public static Match start(String homeTeam, String awayTeam) {
        return new Match();
    }

    public String homeTeam() {
        return null;
    }

    public String awayTeam() {
        return null;
    }

    public Score score() {
        return null;
    }

    public void updateScore(int home, int away) {

    }

    public boolean finished() {
        return false;
    }

    public void finish() {

    }
}
