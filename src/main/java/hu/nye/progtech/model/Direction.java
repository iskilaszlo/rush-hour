package hu.nye.progtech.model;

/**
 * Egy lépés iránya.
 * Minden irányhoz tartozik egy szó (ezt írja be a játékos), egy tengely
 * (milyen irányú jármű mozoghat így) és egy előjel (+1: jobbra vagy le, -1: balra vagy fel).
 */
public enum Direction {
    UP("fel", Orientation.VERTICAL, -1),
    DOWN("le", Orientation.VERTICAL, 1),
    LEFT("balra", Orientation.HORIZONTAL, -1),
    RIGHT("jobbra", Orientation.HORIZONTAL, 1);

    private final String word;
    private final Orientation orientation;
    private final int sign;

    Direction(String word, Orientation orientation, int sign) {
        this.word = word;
        this.orientation = orientation;
        this.sign = sign;
    }

    public String getWord() {
        return word;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getSign() {
        return sign;
    }

    /**
     * Megkeresi az irányt a szava alapján (pl. "le"). Ha nincs ilyen, null-t ad vissza.
     */
    public static Direction fromWord(String word) {
        for (Direction direction : values()) {
            if (direction.word.equals(word)) {
                return direction;
            }
        }
        return null;
    }
}
