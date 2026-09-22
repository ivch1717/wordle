package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: словарь. */
@DisplayName("MR1. Словарь")
class DictionaryTest {

    @Test
    @DisplayName("Словарь содержит не меньше 50 слов")
    void dictionaryContainsAtLeastFiftyWords() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");

        int len = dictionary.size();

        assertTrue(len >= 50);
    }

    @Test
    @DisplayName("Все слова словаря состоят ровно из 5 букв")
    void allWordsAreExactlyFiveLettersLong() throws IOException {
        List<String> dictionary = DictionaryLoader.load("src/main/resources/dictionary.txt");

        for (String word : dictionary) {
            assertEquals(5, word.length(), "Неверная длина слова: " + word);
        }
    }

    @Test
    @DisplayName("Пустой словарь приводит к ошибке, а не к запуску игры без слова")
    void emptyDictionaryIsRejected() {
        List<String> emptyDictionary = List.of();

        GameService gameService = new GameService();

        assertThrows(IllegalArgumentException.class, () -> gameService.startGame(emptyDictionary, 6, 42L));
    }
}
