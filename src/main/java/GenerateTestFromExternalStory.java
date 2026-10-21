class GenerateTestFromExternalStory {
    static void main() {
        String resourcePath = "user-story.txt";
        try {
            System.out.println("Reading user story from resource: " + resourcePath);
            String story = UserStoryReader.readFromClassPath(resourcePath);

            System.out.println("User Story content:\n" + story + "\n");
            System.out.println("Sending requirements to AI");

            String generatedCode = OpenAIClientWrapper.generateCodeFromStory(story);
            System.out.println("\nGenerated AI Test Code\n");

            System.out.println(generatedCode);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
}
