package witalij.lingvistvocabulary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class LingvistParser {
    private static LingvistParser instance = new LingvistParser();

    private LingvistParser(){
    }

    public static LingvistParser getInstance() {
        return instance;
    }

    public List<String> extract(File sourceDir) throws RuntimeException{
        List<String> words = new Vector<>();

        File events = Arrays.stream(sourceDir.listFiles())
                .filter(f -> f.getName().equals("events"))
                .findFirst().orElse(null);

        for (File event : events.listFiles()) {
            words.addAll(getWordsFromFile(event));
        }

        return words;
    }

    private static List<String> getWordsFromFile(File file) {
        List<String> words = new Vector<>();

        try {
            String fileJSON = String.join("\n", Files.readAllLines(file.toPath()));
            JSONArray jsonFile = new JSONArray(fileJSON);
            for (Object item : jsonFile) {
                try {
                    JSONObject jsonObject = (JSONObject) item;
                    JSONObject event = jsonObject.getJSONObject("event");
                    JSONObject data = event.getJSONObject("data");
                    JSONObject evaluation_criteria = data.getJSONObject("evaluation_criteria");
                    JSONObject interpreted_criteria = evaluation_criteria.getJSONObject("interpreted_criteria");
                    String correct_answer = interpreted_criteria.getString("correct_answer");
                    words.add(correct_answer);
                } catch (Exception ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        return words;
    }
}