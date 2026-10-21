import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class GenerateTestFromStory {
    public static void main(String[] args) {
        String userStory = """
            As a registered user,
            I want to login to the website with valid credentials
            so that I can access my dashboard.
            
            Acceptance Criteria:
            - Navigate to https://opensource-demo.orangehrmlive.com/
            - verify page title
            - Enter valid username and password
            - Verify successful login by checking dashboard visibility or page title
            """;

        try {
            System.out.println("Sending user story to AI");
            String generatedCode = OpenAIClientWrapper.generateCodeFromStory(userStory);
            System.out.println("\nAI Generated Test Code:\n");
//            System.out.println(generatedCode);

            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMHH_HHMMSS"));
            String fileName = "generated-test/AI_GeneratedTest_" + timeStamp+".java";
            OpenAIClientWrapper.saveToFile(generatedCode, fileName);
            System.out.println("\nSaved Generated File: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
