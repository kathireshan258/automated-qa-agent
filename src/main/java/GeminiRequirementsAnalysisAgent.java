import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

/**
 * Reviews a requirement and identifies acceptance criteria, risks, and QA questions.
 * Set GEMINI_API_KEY to a Google AI Studio key before running.
 */
public class GeminiRequirementsAnalysisAgent {
    private static final String MODEL = "gemini-2.5-flash";

    public static void main(String[] args) {
        String requirement = args.length == 0
                ? "A customer can reset a forgotten password using their registered email address."
                : String.join(" ", args);

        String prompt = "You are a senior QA analyst. Analyze this requirement and produce concise markdown with "
                + "these sections: Acceptance Criteria, Positive Test Conditions, Negative Test Conditions, "
                + "Risks, and Questions for Product. Requirement: " + requirement;

        try (Client client = createClient()) {
            GenerateContentResponse response = client.models.generateContent(MODEL, prompt, null);
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