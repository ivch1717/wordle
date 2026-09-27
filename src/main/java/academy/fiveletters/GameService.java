package academy.fiveletters;

import java.util.List;
import java.util.Random;

public final class GameService {
    private GameService() {}

    public static GameSession startGame(List<String> dict, int maxAttempts, long seed) {
        if (dict == null || dict.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым");
        }
        Random random = new Random(seed);
        int index = random.nextInt(dict.size());
        String word = dict.get(index);
        return new GameSession(word, maxAttempts);
    }
}
