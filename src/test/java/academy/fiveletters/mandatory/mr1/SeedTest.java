package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameService;
import academy.fiveletters.GameSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: одинаковый seed обязан давать одинаковое загаданное слово. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    @Test
    @DisplayName("Одинаковый seed даёт одинаковое загаданное слово")
    void sameSeedProducesSameAnswer() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");

        GameSession game1 = GameService.startGame(dictionary, 3, 67);
        GameSession game2 = GameService.startGame(dictionary, 3, 67);

        assertEquals(game1.getAnswer(), game2.getAnswer());
    }

    @Test
    @DisplayName("Разные seed'ы дают разные слова хотя бы иногда")
    void differentSeedsProduceDifferentAnswers() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");
        Set<String> selectedAnswers = new HashSet<>();

        for (long seed = 0; seed < 100; seed++) {
            selectedAnswers.add(GameService.startGame(dictionary, 3, seed).getAnswer());
        }

        assertTrue(selectedAnswers.size() > 1, "Разные seed всегда выбирают одно и то же слово");
    }
}
