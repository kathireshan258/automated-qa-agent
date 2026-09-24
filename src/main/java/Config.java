import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class Config {
    static String getApiKey() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config.propeties"));
            return prop.getProperty("openai_api_key");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from config file", e);
        }
    }
}
