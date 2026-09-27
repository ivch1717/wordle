package academy.fiveletters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public final class GameService {
    private List<String> dictionaryWords = List.of();

    public GameSession startGame(List<String> dictionary, int maxAttempts, long seed) {
        return startGame(dictionary, dictionary, maxAttempts, seed);
    }

    public GameSession startGame(
            List<String> dictionaryWords, List<String> dictionaryAnswers, int maxAttempts, long seed) {
        if (dictionaryWords == null
                || dictionaryWords.isEmpty()
                || dictionaryAnswers == null
                || dictionaryAnswers.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым");
        }
        if (!dictionaryWords.containsAll(dictionaryAnswers)) {
            throw new IllegalArgumentException("Ответы должны входить в словарь допустимых слов");
        }
        Random random = new Random(seed);
        int index = random.nextInt(dictionaryAnswers.size());
        String word = dictionaryAnswers.get(index);
        this.dictionaryWords = List.copyOf(dictionaryWords);

        return new GameSession(word, maxAttempts);
    }

    private boolean isWordInDictionary(String word) {
        return dictionaryWords.contains(word);
    }

    private void correctnessCheck(GameSession session, String guess) {
        if (guess == null || guess.length() != session.getAnswer().length()) {
            throw new IllegalArgumentException(
                    "Слово должно состоять из " + session.getAnswer().length() + " букв");
        }

        guess = guess.toLowerCase(Locale.ROOT);

        for (int i = 0; i < guess.length(); i++) {
            if (!Character.isLetter(guess.charAt(i))) {
                throw new IllegalArgumentException("Должны быть только буквы");
            }
        }

        if (dictionaryWords.isEmpty() || !isWordInDictionary(guess)) {
            throw new IllegalArgumentException("Слова нет в словаре");
        }

        if (session.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Игра уже завершена");
        }
    }

    private GuessResult calculateRes(GameSession session, String guess) {
        Map<Character, Integer> letters = new HashMap<>();
        for (Character c : session.getAnswer().toCharArray()) {
            letters.put(c, letters.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < session.getAnswer().length(); i++) {
            if (session.getAnswer().charAt(i) == guess.charAt(i)) {
                letters.put(guess.charAt(i), letters.getOrDefault(guess.charAt(i), 0) - 1);
            }
        }

        List<LetterStatus> res = new ArrayList<>();

        for (int i = 0; i < session.getAnswer().length(); i++) {
            if (session.getAnswer().charAt(i) != guess.charAt(i)) {
                if (letters.getOrDefault(guess.charAt(i), 0) > 0) {
                    res.add(LetterStatus.PRESENT);
                    letters.put(guess.charAt(i), letters.getOrDefault(guess.charAt(i), 0) - 1);
                } else {
                    res.add(LetterStatus.ABSENT);
                }
            } else {
                res.add(LetterStatus.CORRECT);
            }
        }

        return new GuessResult(guess, res);
    }

    public GuessResult applyGuess(GameSession session, String guess) {
        correctnessCheck(session, guess);

        guess = guess.toLowerCase(Locale.ROOT);

        session.nextStep(guess);

        if (guess.equals(session.getAnswer())) {
            session.win();
        } else if (session.getMaxAttempts() == session.getAttemptsUsed()) {
            session.lose();
        }

        return calculateRes(session, guess);
    }
}
