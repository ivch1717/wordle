package academy.fiveletters;

import java.util.ArrayList;
import java.util.List;

public final class GameSession {
    private static final int WORD_LENGTH = 5;
    private final String answer;
    private GameStatus status;
    private final int maxAttempts;
    private int attemptsUsed;
    private final List<String> attemptsHistory;

    public GameSession(String answer, int maxAttempts) {
        if (answer == null || answer.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Слово должно быть из " + WORD_LENGTH + " букв");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Количество ходов должно быть положительным числом");
        }
        this.answer = answer;
        this.status = GameStatus.IN_PROGRESS;
        this.maxAttempts = maxAttempts;
        this.attemptsUsed = 0;
        this.attemptsHistory = new ArrayList<>();
    }

    public List<String> getAttemptsHistory() {
        return List.copyOf(attemptsHistory);
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttemptsUsed() {
        return attemptsUsed;
    }
}
