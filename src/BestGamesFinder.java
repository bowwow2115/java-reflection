import java.util.*;
import java.util.stream.Collectors;

import static annotations.Annotations.*;

public class BestGamesFinder {
    private Database database = new Database();

    @Operation("All-Games")
    public List<String> getAllGames() {
        return database.readAllGames();
    }

    @Operation("Game-To-Price")
    public Map<String, Float> getGameToPrice(@DependsOn("All-Games") List<String> allGames){
        return database.readGameToPrice(allGames);
    }

    @Operation("Game-To-Rating")
    public Map<String, Float> getGameToRating(@DependsOn("All-Games") List<String> allGames) {
        return database.readGameToRatings(allGames);
    }

    @Operation("Game-To-Score")
    public SortedMap<String, Double> scoreGames(@DependsOn("Game-To-Price") Map<String, Float> gameToPrice,
                                                @DependsOn("Game-To-Rating") Map<String, Float> gameToRating) {
        SortedMap<String, Double> gameToScore = new TreeMap<>(Collections.reverseOrder());
        for (String gameName : gameToPrice.keySet()) {
            double score = (double) gameToRating.get(gameName) / gameToPrice.get(gameName);
            gameToScore.put(gameName, score);
        }

        return gameToScore;
    }

    @FinalResult
    public List<String> getTopGames(@DependsOn("Game-To-Score") SortedMap<String, Double> gameToScore) {
        return new ArrayList<>(gameToScore.keySet());
    }


    /**
     * Simulates database
     */
    private class Database {
        private final Random random = new Random(1);
        public List<String> readAllGames() {
            return Arrays.asList("Fortnite", "Minecraft", "League Of Legends", "Ace Combat", "StarCraft", "Burnout");
        }

        public Map<String, Float> readGameToRatings(List<String> games) {
            return games.stream().collect(Collectors.toMap(game -> game, game -> random.nextFloat() * 4.0f + 1));
        }

        public Map<String, Float> readGameToPrice(List<String> games) {
            return games.stream().collect(Collectors.toMap(game -> game, game -> random.nextFloat() * 90f + 10));
        }
    }
}

