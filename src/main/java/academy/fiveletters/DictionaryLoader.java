package academy.fiveletters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DictionaryLoader {
    private static final int WORD_LENGTH = 5;

    private DictionaryLoader() {}

    public static List<String> load(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);

        List<String> dict = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty() && line.charAt(0) != '#') {
                if (line.length() != WORD_LENGTH) {
                    throw new IllegalArgumentException("длина слова не соответствует заданной");
                }
                dict.add(line);
            }
        }

        return dict;
    }
}
