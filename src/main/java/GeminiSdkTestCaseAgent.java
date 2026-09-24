import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

/**
 * Generates functional Gherkin test cases through the official Google GenAI SDK.
 * Set GEMINI_API_KEY to a Google AI Studio key before running.
 */
public class GeminiSdkTestCaseAgent {
    private static final String MODEL = "gemini-2.5-flash";

    public static void main(String[] args) {
        String userStory = args.length == 0
                ? "As a registered user, I want to reset my password so that I can regain access if I forget it."
                : String.join(" ", args);

        String prompt = "You are a software QA engineer. Generate exactly 3 functional test cases for this "
                + "user story. Return only a JSON array where every object has title, preconditions, gherkin, "
                + "and expectedResult fields. Use Given/When/Then in gherkin. User story: " + userStory;

        try (Client client = createClient()) {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseMimeType("application/json")
                    .build();
            GenerateContentResponse response = client.models.generateContent(MODEL, prompt, config);
            System.out.println(response.text());
        }
    }

    private static Client createClient() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Set the GEMINI_API_KEY environment variable before running this agent.");
        }
        return Client.builder().apiKey(apiKey).build();
    }
}