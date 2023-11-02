package Spoonacular;


import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestSpoo {
    private static String apiKey;
    private static String urlSpoo;
    private static String hash_test;
    private static String username;
    private static final Logger logger = LoggerFactory.getLogger(AbstractTestSpoo.class);


    @BeforeAll

    static void setUp() throws IOException {
        Properties props = new Properties();
        InputStream dataFile = new FileInputStream("src/main/resources/spoonacular.properties");
        props.load(dataFile);
        apiKey = props.getProperty("apiKey");
        urlSpoo = props.getProperty("url");
        hash_test = props.getProperty("hash_spoo");
        username = props.getProperty("username");
        logger.info("Загрузка данных из файла .properties");

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
