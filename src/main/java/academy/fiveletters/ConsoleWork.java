package academy.fiveletters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public final class ConsoleWork {
    private static final int MAX_ATTEMPTS = 6;
    private static final String RESET = "\u001B[0m";

    private final String path;
    private final Map<String, String> answerDictionaries;
    private final Random random;
    private final Scanner scanner;
    private String selectedMode = "Обычный";
    private String selectedAnswersPath;
    private String textColor = "";
    private final GameStatistic statistic = new GameStatistic();
    private final GameService gameService = new GameService();

    public ConsoleWork(String path, Map<String, String> answerDictionaries, Random random) {
        this.path = path;
        this.answerDictionaries = Collections.unmodifiableMap(new LinkedHashMap<>(answerDictionaries));
        this.selectedAnswersPath = path;
        this.random = random;
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    private void print(String text) {
        if (textColor.isEmpty()) {
            System.out.print(text);
        } else {
            System.out.print(textColor + text + RESET);
        }
    }

    private void println(String text) {
        print(text + System.lineSeparator());
    }

    void startGame() {
        label:
        while (true) {
            println("1. Новая игра");
            println("2. Выбор цвета вывода");
            println("3. Статистика");
            println("4. Выбрать режим сложности/категорию слов");
            println("0. Выход");
            print("Выберите пункт: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    println("Начинаем игру");
                    newGame();
                    break;
                case "2":
                    changeColor();
                    break;
                case "3":
                    println(statistic.getStatistic());
                    break;
                case "4":
                    changeMode();
                    break;
                case "0":
                    println("До свидания!");
                    break label;
                case null:
                default:
                    println("Некорректный пункт меню");
                    break;
            }
        }
    }

    void changeColor() {
        println("0. Обычный");
        println("1. Зелёный");
        println("2. Синий");
        println("3. Фиолетовый");
        println("4. Красный");
        print("Выберите цвет: ");

        if (!scanner.hasNextLine()) {
            return;
        }

        String choice = scanner.nextLine();
        switch (choice) {
            case "0" -> textColor = "";
            case "1" -> textColor = "\u001B[32m";
            case "2" -> textColor = "\u001B[34m";
            case "3" -> textColor = "\u001B[35m";
            case "4" -> textColor = "\u001B[31m";
            default -> {
                println("Некорректный цвет");
                return;
            }
        }

        println("Цвет изменён");
    }

    void changeMode() {
        List<Map.Entry<String, String>> modes = new ArrayList<>(answerDictionaries.entrySet());
        for (int i = 0; i < modes.size(); i++) {
            println((i + 1) + ". " + modes.get(i).getKey());
        }
        println("0. Назад");
        print("Выберите режим (сейчас: " + selectedMode + "): ");

        if (!scanner.hasNextLine()) {
            return;
        }

        String choice = scanner.nextLine();
        if ("0".equals(choice)) {
            return;
        }
        for (int i = 0; i < modes.size(); i++) {
            if (Integer.toString(i + 1).equals(choice)) {
                selectedMode = modes.get(i).getKey();
                selectedAnswersPath = modes.get(i).getValue();
                println("Выбран режим: " + selectedMode);
                return;
            }
        }
        println("Некорректный режим");
    }

    void newGame() {
        GameSession session;
        int hintPosition;

        try {
            long gameSeed = random.nextLong();
            session = gameService.startGame(
                    DictionaryLoader.load(path), DictionaryLoader.load(selectedAnswersPath), MAX_ATTEMPTS, gameSeed);
            hintPosition = new Random(gameSeed).nextInt(session.getAnswer().length());
        } catch (IllegalArgumentException | IOException exception) {
            println(String.valueOf(exception.getMessage()));
            return;
        }

        boolean hintUsed = false;

        while (session.getStatus() == GameStatus.IN_PROGRESS) {
            try {
                print("Введите слово (? — подсказка): ");

                if (!scanner.hasNextLine()) {
                    return;
                }
                String input = scanner.nextLine();

                if ("?".equals(input)) {
                    if (hintUsed) {
                        println("Подсказка уже использована");
                    } else {
                        char letter = session.getAnswer().charAt(hintPosition);
                        println("Подсказка: буква №" + (hintPosition + 1) + " — " + letter);
                        hintUsed = true;
                    }
                    continue;
                }

                GuessResult result = gameService.applyGuess(session, input);

                for (LetterStatus st : result.letterStatuses()) {
                    print(st.symbol());
                }
                println(" " + input);
                println("Осталось попыток: " + (session.getMaxAttempts() - session.getAttemptsUsed()));

            } catch (IllegalArgumentException exception) {
                println(String.valueOf(exception.getMessage()));
            }
        }

        statistic.updateStatistic(session);
        if (session.getStatus() == GameStatus.LOSE) {
            println("Неудача. Загаданное слово: " + session.getAnswer());
        } else {
            println("Победа! Слово угадано за " + session.getAttemptsUsed() + " попыток");
        }
    }
}
