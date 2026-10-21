import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

class OpenAIClientWrapper {
    private static final Logger LOGGER = Logger.getLogger(OpenAIClientWrapper.class.getName());
    private static OpenAIClient client;

    OpenAIClientWrapper() {
        String apiKey = Config.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key must not be empty");
        }
        client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
    }

    private static void initClientIfNeeded() {
        if (client == null) {
            synchronized (OpenAIClientWrapper.class) {
                if (client == null) {
                    String apiKey = Config.getApiKey();
                    if (apiKey == null || apiKey.isBlank()) {
                        throw new IllegalArgumentException("API key must not be blank");
                    }
                    client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
                    LOGGER.info("OpenAI client initialized.");
                }
            }
        }
    }

    // Generate Java source code for a Selenium + TestNG test based on the given user story.
    static String generateCodeFromStory(String userStory) {
        if (userStory == null || userStory.isBlank()) {
            throw new IllegalArgumentException("User Story must not be blank");
        }

        initClientIfNeeded();

        // Construct the system prompt
        String systemPrompt = """
                You are an expert Test Automation Engineer. Generate a clean, maintainable Selenium + TestNG
                test in Java. Provide full Java source code with imports, class, method, ChromeDriver setup and
                TearDown, descriptive naming and assertions.
                """;

        // Create chat completion parameters
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder().model(ChatModel.GPT_4_1_MINI)
                .addSystemMessage(systemPrompt).addUserMessage("User Story / Acceptance Criteria:\n\n"
                + userStory).build();

        // Call the API
        ChatCompletion result = client.chat().completions().create(params);

        // Extract content
        String code = null;
        try {
            code = result.choices().get(0).message().content().orElseThrow(() ->
                new IOException("No content in OpenAI response")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return code.trim();
    }

    // Save the generated code to a java file
    static void saveToFile(String code, String fileName) throws IOException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code must not be empty");
        }
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name must not be empty");
        }

        Path path = Path.of(fileName).toAbsolutePath();

        // Ensure parent directory exist
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        LOGGER.info("Saved generated code to: " + path);
    }
}
