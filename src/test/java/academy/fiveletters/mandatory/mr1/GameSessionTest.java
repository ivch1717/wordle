package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.GameService;
import academy.fiveletters.GameSession;
import academy.fiveletters.GameStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Дополнительные тесты начального состояния игровой сессии. */
@DisplayName("MR1. Игровая сессия")
class GameSessionTest {

    @Test
    @DisplayName("Новая сессия содержит правильное начальное состояние")
    void newSessionHasExpectedInitialState() {
        List<String> dictionary = List.of("озеро", "арбуз", "школа");

        GameService gameService = new GameService();
        GameSession session = gameService.startGame(dictionary, 6, 42L);

        assertTrue(dictionary.contains(session.getAnswer()));
        assertEquals(6, session.getMaxAttempts());
        assertEquals(0, session.getAttemptsUsed());
        assertTrue(session.getAttemptsHistory().isEmpty());
        assertEquals(GameStatus.IN_PROGRESS, session.getStatus());
    }

    @Test
    @DisplayName("Неположительное число попыток отклоняется")
    void nonPositiveMaxAttemptsIsRejected() {
        List<String> dictionary = List.of("озеро");

        GameService gameService1 = new GameService();
        GameService gameService2 = new GameService();

        assertThrows(IllegalArgumentException.class, () -> gameService1.startGame(dictionary, 0, 42L));
        assertThrows(IllegalArgumentException.class, () -> gameService2.startGame(dictionary, -1, 42L));
    }
}
