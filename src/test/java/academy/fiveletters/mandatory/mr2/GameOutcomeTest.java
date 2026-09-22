package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import academy.fiveletters.GameService;
import academy.fiveletters.GameSession;
import academy.fiveletters.GameStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: завершение партии. */
@DisplayName("MR2. Победа и поражение")
class GameOutcomeTest {

    @Test
    @DisplayName("Угаданное слово переводит сессию в статус WIN")
    void correctGuessWinsTheGame() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        service.applyGuess(session, "озеро");

        assertEquals(GameStatus.WIN, session.getStatus());
    }

    @Test
    @DisplayName("После 6 неудачных попыток сессия переходит в статус LOSE")
    void sixFailedAttemptsLoseTheGame() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        for (int attempt = 0; attempt < 6; attempt++) {
            service.applyGuess(session, "арбуз");
        }

        assertEquals(GameStatus.LOSE, session.getStatus());
    }

    @Test
    @DisplayName("При поражении показывается загаданное слово")
    void answerIsRevealedOnLoss() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);

        for (int attempt = 0; attempt < 6; attempt++) {
            service.applyGuess(session, "арбуз");
        }

        assertEquals("озеро", session.getAnswer());
    }

    @Test
    @DisplayName("Завершённая партия больше не принимает попытки")
    void finishedGameRejectsFurtherGuesses() {
        GameService service = createService();
        GameSession session = new GameSession("озеро", 6);
        service.applyGuess(session, "озеро");

        assertThrows(IllegalArgumentException.class, () -> service.applyGuess(session, "арбуз"));
    }

    private static GameService createService() {
        GameService service = new GameService();
        service.startGame(List.of("озеро", "арбуз"), 6, 42L);
        return service;
    }
}
