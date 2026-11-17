package hu.nye.progtech.model;

import java.util.Objects;

/**
 * A ranglista egy sora: a játékos neve és a győzelmei száma.
 */
public final class HighScore {

    private final String name;
    private final int wins;

    public HighScore(String name, int wins) {
        this.name = name;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HighScore highScore = (HighScore) o;
        return wins == highScore.wins && name.equals(highScore.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, wins);
    }

    @Override
    public String toString() {
        return "HighScore{name=" + name + ", wins=" + wins + "}";
    }
}
