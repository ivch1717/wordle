package academy.fiveletters;

public class GameStatistic {

    private int countWins = 0;
    private int countLose = 0;
    private double averageNumberAttempts = 0;
    private int totalWinningAttempts = Integer.MAX_VALUE;

    void updateStatistic(GameSession session) {
        if (session.getStatus() == GameStatus.WIN) {
            countWins += 1;
            averageNumberAttempts += session.getAttemptsUsed();
            totalWinningAttempts = Math.min(totalWinningAttempts, session.getAttemptsUsed());
        } else if (session.getStatus() == GameStatus.LOSE) {
            countLose += 1;
        } else {
            throw new IllegalArgumentException("Игра ещё не завершена");
        }
    }

    String getStatistic() {
        if (countWins == 0) {
            return "Количество побед: " + countWins + "\nКоличество поражений:" + countLose;
        }
        return "Количество побед: " + countWins + "\nКоличество поражений: " + countLose
                + "\nСреднее количество попыток на угадывание: "
                + (averageNumberAttempts / countWins) + "\nМинимальное количество попыток на угадывание: "
                + totalWinningAttempts;
    }
}
