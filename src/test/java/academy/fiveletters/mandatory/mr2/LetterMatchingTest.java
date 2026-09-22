package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.fiveletters.GameService;
import academy.fiveletters.GameSession;
import academy.fiveletters.LetterStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: раскраска букв. */
@DisplayName("MR2. Проверка букв")
class LetterMatchingTest {

    @Test
    @DisplayName("Базовый случай: загадано \"озеро\", ввод \"арбуз\" -> ❌🟡❌❌🟡")
    void basicCase() {
        GameService service = createService(List.of("озеро", "арбуз"));
        GameSession session = new GameSession("озеро", 6);

        var result = service.applyGuess(session, "арбуз");

        assertEquals(
                List.of(
                        LetterStatus.ABSENT,
                        LetterStatus.PRESENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.PRESENT),
                result.letterStatuses());
    }

    @Test
    @DisplayName("Полное совпадение: загадано \"озеро\", ввод \"озеро\" -> ✅✅✅✅✅")
    void exactMatch() {
        GameService service = createService(List.of("озеро"));
        GameSession session = new GameSession("озеро", 6);

        var result = service.applyGuess(session, "озеро");

        assertEquals(
                List.of(
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT,
                        LetterStatus.CORRECT),
                result.letterStatuses());
    }

    @Test
    @DisplayName("Повторяющиеся буквы: загадано \"сорок\", ввод \"оооом\" -> ❌✅❌✅❌")
    void repeatedLettersAreNotDoubleCounted() {
        GameService service = createService(List.of("сорок", "оооом"));
        GameSession session = new GameSession("сорок", 6);

        var result = service.applyGuess(session, "оооом");

        assertEquals(
                List.of(
                        LetterStatus.ABSENT,
                        LetterStatus.CORRECT,
                        LetterStatus.ABSENT,
                        LetterStatus.CORRECT,
                        LetterStatus.ABSENT),
                result.letterStatuses());
    }

    @Test
    @DisplayName("Ни одна буква не подошла: все позиции ❌")
    void noMatchingLetters() {
        GameService service = createService(List.of("озеро", "тыква"));
        GameSession session = new GameSession("озеро", 6);

        var result = service.applyGuess(session, "тыква");

        assertEquals(
                List.of(
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT,
                        LetterStatus.ABSENT),
                result.letterStatuses());
    }

    private static GameService createService(List<String> dictionary) {
        GameService service = new GameService();
        service.startGame(dictionary, 6, 42L);
        return service;
    }
}
