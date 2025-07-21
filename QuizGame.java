import java.util.*;

public class QuizGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎮 Welcome to the Java Quiz Game!");
        System.out.print("Enter number of questions: ");
        int numQuestions = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Choose difficulty (easy / medium / hard): ");
        String difficulty = scanner.nextLine().toLowerCase();

        QuizService quizService = new QuizService();
        List<Question> questions = quizService.fetchQuestions(numQuestions, difficulty);

        int score = 0;
        int questionNumber = 1;

        for (Question q : questions) {
            System.out.println("\nQ" + questionNumber + ": " + q.getQuestionText());
            List<String> options = q.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }

            int userChoice = -1;
            while (userChoice < 1 || userChoice > 4) {
                System.out.print("Your answer (1-4): ");
                if (scanner.hasNextInt()) {
                    userChoice = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    scanner.nextLine();
                    System.out.println("Please enter a valid option (1-4).");
                }
            }

            String userAnswer = options.get(userChoice - 1);
            if (userAnswer.equals(q.getCorrectAnswer())) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong! Correct answer: " + q.getCorrectAnswer());
            }

            questionNumber++;
        }

        System.out.println("\nGame Over! Your score: " + score + " out of " + questions.size());
        scanner.close();
    }
}
