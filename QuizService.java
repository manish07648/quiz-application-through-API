import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class QuizService {
    public List<Question> fetchQuestions(int amount, String difficulty) {
        List<Question> questions = new ArrayList<>();

        try {
            String apiUrl = "https://opentdb.com/api.php?amount=" + amount + "&difficulty=" + difficulty + "&type=multiple";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
            JSONArray results = (JSONArray) jsonResponse.get("results");

            for (Object obj : results) {
                JSONObject questionObj = (JSONObject) obj;
                String question = htmlDecode((String) questionObj.get("question"));
                String correctAnswer = htmlDecode((String) questionObj.get("correct_answer"));

                JSONArray incorrect = (JSONArray) questionObj.get("incorrect_answers");
                List<String> options = new ArrayList<>();
                for (Object option : incorrect)
                    options.add(htmlDecode(option.toString()));
                options.add(correctAnswer);

                Collections.shuffle(options);
                questions.add(new Question(question, options, correctAnswer));
            }

        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }

        return questions;
    }

    private String htmlDecode(String text) {
        return text.replaceAll("&quot;", "\"")
                   .replaceAll("&#039;", "'")
                   .replaceAll("&amp;", "&")
                   .replaceAll("&eacute;", "é")
                   .replaceAll("&rsquo;", "’");
    }
}
