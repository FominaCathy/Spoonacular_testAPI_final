package Spoonacular;

import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractTestSpoo {
    private static String apiKey;
    private static String urlSpoo;
    private static String hash_test;
    private static String username;



    @BeforeAll
    static void setUp() throws IOException {
        Properties props = new Properties();
        InputStream dataFile = new FileInputStream("src/main/resources/spoonacular.properties");
        props.load(dataFile);
        apiKey = props.getProperty("apiKey");
        urlSpoo = props.getProperty("url");
        hash_test = props.getProperty("hash_spoo");
        username = props.getProperty("username");

    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getUrl() {
        return urlSpoo;
    }

    public static String getHash() {
        return hash_test;
    }

    public static String getUsername(){
        return username;
    }

}
