package academy.fiveletters;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Точка входа в игру «5 букв». */
public final class Main {
    private static final String DICTIONARY_PATH = "src/main/resources/dictionary.txt";

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        Random random;

        if (args.length == 0) {
            random = new Random(); // обычная игра
        } else if (args.length == 2 && args[0].equals("--seed")) {
            random = new Random(Long.parseLong(args[1]));
        } else {
            System.out.println("Использование: --seed ЧИСЛО");
            return;
        }

        Map<String, String> answerDictionaries = new LinkedHashMap<>();
        answerDictionaries.put("Лёгкий", "src/main/resources/easy-dictionary.txt");
        answerDictionaries.put("Обычный", DICTIONARY_PATH);
        answerDictionaries.put("Сложный", "src/main/resources/hard-dictionary.txt");
        answerDictionaries.put("Животные", "src/main/resources/animals-dictionary.txt");
        answerDictionaries.put("Еда", "src/main/resources/food-dictionary.txt");
        answerDictionaries.put("Природа", "src/main/resources/nature-dictionary.txt");

        new ConsoleWork(DICTIONARY_PATH, answerDictionaries, random).startGame();
    }
}
