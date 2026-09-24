import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Generates Gherkin test cases through the Gemini API.
 * Set GEMINI_API_KEY to an API key created in Google AI Studio.
 */
public class GeminiTestCaseAgent {
    private static final String MODEL = "gemini-2.5-flash";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
            + MODEL + ":generateContent?key=";

    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Set the GEMINI_API_KEY environment variable before running this agent.");
        }

        String userStory = args.length == 0
                ? "As a registered user, I want to reset my password so that I can regain access if I forget it."
                : String.join(" ", args);

        String prompt = "You are a software QA engineer. Given the following user story, generate exactly 3 "
                + "functional test cases. Return only a JSON array. Each object must have the fields "
                + "title, preconditions, gherkin, and expectedResult. The gherkin field must use "
                + "Given/When/Then steps. User story: " + userStory;

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(prompt)
                + "\"}]}],\"generationConfig\":{\"responseMimeType\":\"application/json\"}}";

        HttpRequest request = HttpRequest.newBuilder(URI.create(API_URL + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Gemini API request failed with status " + response.statusCode() + ": "
                    + response.body());
        }

        System.out.println(extractGeneratedText(response.body()));
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static String extractGeneratedText(String responseBody) throws IOException {
        int textStart = responseBody.indexOf("\"text\":\"");
        if (textStart < 0) {
            throw new IOException("Gemini response did not contain generated text: " + responseBody);
        }

        int index = textStart + "\"text\":\"".length();
        StringBuilder text = new StringBuilder();
        boolean escaped = false;

        while (index < responseBody.length()) {
            char character = responseBody.charAt(index++);
            if (escaped) {
                switch (character) {
                    case 'n' -> text.append('\n');
                    case 'r' -> text.append('\r');
                    case 't' -> text.append('\t');
                    case '"' -> text.append('"');
                    case '\\' -> text.append('\\');
                    case '/' -> text.append('/');
                    default -> text.append(character);
                }
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '"') {
                return text.toString();
            } else {
                text.append(character);
            }
        }

        throw new IOException("Gemini response contained an unterminated text value.");
    }
}