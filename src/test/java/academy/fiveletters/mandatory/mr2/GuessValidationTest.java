package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.GameService;
import academy.fiveletters.GameSession;
import academy.fiveletters.GuessResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: валидация ввода. */
@DisplayName("MR2. Валидация ввода")
class GuessValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"дом", "домики", ""})
    @DisplayName("Слово не из 5 букв отклоняется: \"{0}\"")
    void wordOfWrongLengthIsRejected(String guess) {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        assertThrows(IllegalArgumentException.class, () -> service.applyGuess(session, guess));
    }

    @ParameterizedTest
    @ValueSource(strings = {"дом12", "дом!!", "до ма"})
    @DisplayName("Ввод с не-буквами отклоняется: \"{0}\"")
    void nonLetterInputIsRejected(String guess) {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        assertThrows(IllegalArgumentException.class, () -> service.applyGuess(session, guess));
    }

    @Test
    @DisplayName("Слово, которого нет в словаре, отклоняется")
    void wordOutsideDictionaryIsRejected() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        assertThrows(IllegalArgumentException.class, () -> service.applyGuess(session, "метла"));
    }

    @Test
    @DisplayName("Некорректный ввод не тратит попытку")
    void invalidInputDoesNotConsumeAttempt() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        assertThrows(IllegalArgumentException.class, () -> service.applyGuess(session, "дом"));

        assertEquals(0, session.getAttemptsUsed());
        assertTrue(session.getAttemptsHistory().isEmpty());
    }

    @Test
    @DisplayName("Ввод не зависит от регистра: \"ОЗЕРО\" и \"озеро\" обрабатываются одинаково")
    void inputIsCaseInsensitive() {
        GameService service = createService();

        GuessResult upperCase = service.applyGuess(new GameSession("озеро", 6), "ОЗЕРО");
        GuessResult lowerCase = service.applyGuess(new GameSession("озеро", 6), "озеро");

        assertEquals(lowerCase, upperCase);
    }

    private static GameService createService() {
        GameService service = new GameService();
        service.startGame(List.of("озеро", "арбуз"), 6, 42L);
        return service;
    }
}
