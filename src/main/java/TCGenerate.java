import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

class TCGenerate {
    public static void main() {
        String apiKey = Config.getApiKey();
        OpenAIClient client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
        String userStory = "As a registered user, I want to reset my password so that " +
                "I can regain access If I forget it";
        String prompt = "You are a software QA engineer. Given the following user story" +
                ", generate 3 test cases in Gherkin format (Given/When/Then)" +
                "Provide each test cases with:\n" +
                "- Title\n- Preconditions\n- Steps in Gherkin (Given/When/Then)\n" +
                "- Expected result summary\n\nUser Story:\n" + userStory
                + "\n\nRespond only with a JSON array of objects with fields:\n" +
                "- Title\n- Preconditions\n- Gherkin\n Expected.\n";
        String model = "gpt-4.1";
        ResponseCreateParams params = ResponseCreateParams.builder().input(prompt).model(model).build();
        try {
            Response response = client.responses().create(params);
            System.out.println("Raw response object: " + response);
        } catch (Exception e) {
            System.err.println("Error calling OpenAIClient : " + e.getMessage());
        }
    }
}
