package SpoonacularMok;


import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class AbstractTestSpooMok {
    private static String apiKey = "b4bf740b7513429fb25f3c9f2d95fb24";
    private static String urlSpoo = "https://api.spoonacular.com/";
    private static String hash_test = "34f8f504752c1d1df2eb6b22df45e36701961310";
    private static String username = "testuser271";
    private static int port = 8080;
    static WireMockServer wireMockServer = new WireMockServer();
    private static final Logger logger = LoggerFactory.getLogger(AbstractTestSpooMok.class);

    @BeforeAll
    static void startServer() throws IOException {
        wireMockServer.start();
        logger.info("Start WireMockServer, localhost = {}", port);
        configureFor("localhost", port);
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
        logger.info("Stop WireMockServer, localhost = {}", port);
    }


    public static String getUrl() {
        return "http://localhost:8080/";
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getHash() {
        return hash_test;
    }

    public static String getUsername() {
        return username;
    }

    public String convertResponseToString(HttpResponse response) throws IOException {

        try (InputStream responseStream = response.getEntity().getContent();
             Scanner scanner = new Scanner(responseStream, "UTF-8");) {
            String responseString = scanner.useDelimiter("\\Z").next();
            return responseString;
        }
    }
}
