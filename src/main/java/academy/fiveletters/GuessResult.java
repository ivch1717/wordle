package academy.fiveletters;

import java.util.List;

public record GuessResult(String guess, List<LetterStatus> letterStatuses) {
    public GuessResult {
        letterStatuses = List.copyOf(letterStatuses);
    }
}
