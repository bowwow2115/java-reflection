package data;

import java.util.Arrays;

public class GameConfig {
    private int releaseYear;
    private String gameName;
    private String [] characterNames;
    private double price;

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public String getGameName() {
        return this.gameName;
    }

    public String[] getCharacterNames() {
        return this.characterNames;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "releaseYear=" + releaseYear +
                ", gameName='" + gameName + '\'' +
                ", characterNames=" + Arrays.toString(characterNames) +
                ", price=" + price +
                '}';
    }
}
