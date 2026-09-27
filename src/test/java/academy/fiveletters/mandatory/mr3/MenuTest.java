package academy.fiveletters.mandatory.mr3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameService;
import academy.fiveletters.support.CliRunner;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: меню и режим автопроверки. */
@DisplayName("MR3. Меню и детерминированный режим")
class MenuTest {

    @Test
    @DisplayName("Некорректный пункт меню не роняет программу")
    void invalidMenuChoiceDoesNotCrash() {
        var result = CliRunner.runWithInput("неверно\n0\n");

        assertEquals(0, result.exitCode());
        assertTrue(result.stdout().contains("Некорректный пункт меню"));
        assertTrue(result.stdout().contains("До свидания!"));
    }

    @Test
    @DisplayName("Можно сыграть несколько партий подряд без перезапуска")
    void severalGamesInARow() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");
        Random seeds = new Random(42L);
        GameService service = new GameService();
        String firstAnswer = service.startGame(dictionary, 6, seeds.nextLong()).getAnswer();
        String secondAnswer = service.startGame(dictionary, 6, seeds.nextLong()).getAnswer();
        String input = "1\n%s\n1\n%s\n0\n".formatted(firstAnswer, secondAnswer);

        var result = CliRunner.runWithInput(input, "--seed", "42");

        assertEquals(0, result.exitCode());
        assertEquals(2, result.stdout().split("Начинаем игру", -1).length - 1);
        assertEquals(2, result.stdout().split("Победа!", -1).length - 1);
        assertTrue(result.stdout().contains("До свидания!"));
    }

    @Test
    @DisplayName("Режим меняет ответы, но не словарь допустимых попыток")
    void selectedModeChangesAnswersOnly() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");
        List<String> easyAnswers = DictionaryLoader.load("src/main/resources/easy-dictionary.txt");
        long gameSeed = new Random(42L).nextLong();
        String answer = new GameService()
                .startGame(dictionary, easyAnswers, 6, gameSeed)
                .getAnswer();

        var result = CliRunner.runWithInput("4\n1\n1\nшоссе\n" + answer + "\n0\n", "--seed", "42");

        assertEquals(0, result.exitCode());
        assertTrue(result.stdout().contains("Выбран режим: Лёгкий"));
        assertTrue(result.stdout().contains(" шоссе"));
        assertTrue(result.stdout().contains("Победа!"));
    }

    @Test
    @DisplayName("Детерминированный режим даёт предсказуемый вывод для автопроверки")
    void deterministicModeProducesPredictableOutput() {
        String input = "1\nарбуз\n";

        var firstRun = CliRunner.runWithInput(input, "--seed", "42");
        var secondRun = CliRunner.runWithInput(input, "--seed", "42");

        assertEquals(0, firstRun.exitCode());
        assertEquals(0, secondRun.exitCode());
        assertEquals(firstRun.stdout(), secondRun.stdout());
        assertTrue(firstRun.stdout().contains(" арбуз"));
    }
}
