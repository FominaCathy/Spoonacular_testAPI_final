package SpoonacularMok;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.Spoonacular.Aisle;
import org.example.Spoonacular.Item;
import org.example.Spoonacular.ShoppingList;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMealPlanningMok extends AbstractTestSpooMok {
    private int calories = 2000;
    private static final Logger logger = LoggerFactory.getLogger(TestMealPlanningMok.class);


    @Test
    void addToMealPlan() throws IOException, URISyntaxException {
        logger.info("тест: addToMealPlan - запущен" );
        logger.debug("создание мока для POST /mealplanner/username/items");

        stubFor(post(urlPathEqualTo("/mealplanner/" + getUsername() + "/items"))
                .withRequestBody(containing("INGREDIENTS"))
                .withRequestBody(containing("banana"))
                .willReturn(aResponse()
                        .withStatus(200)
                ));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getUrl() + "mealplanner/" + getUsername() + "/items");
        request.setEntity(new StringEntity("{\n" +
                "    \"date\": 1589500800,\n" +
                "    \"slot\": 1,\n" +
                "    \"position\": 0,\n" +
                "    \"type\": \"INGREDIENTS\",\n" +
                "    \"value\": {\n" +
                "        \"ingredients\": [\n" +
                "            {\n" +
                "                \"name\": \"1 banana\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}"));

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, postRequestedFor(urlPathEqualTo("/mealplanner/" + getUsername() + "/items")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    void getMealPlanDay() throws IOException, URISyntaxException {
        logger.info("тест: getMealPlanDay - запущен" );

        logger.debug("создание мока для GET /mealplanner/username/day/2020-05-15");

        stubFor(get(urlPathMatching("/mealplanner/.*"))
                .willReturn(aResponse().withStatus(200)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "mealplanner/" + getUsername() + "/day/2020-05-15");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathMatching("/mealplanner/.*")));
        assertEquals(200, response.getStatusLine().getStatusCode());


    }

    @Test
    void clearMealPlanDay() throws URISyntaxException, IOException {
        logger.info("тест: clearMealPlanDay - запущен" );

        logger.debug("создание мока для DELETE /mealplanner/username/day/2020-05-15");

        stubFor(delete(urlPathMatching("/mealplanner/.*"))
                .willReturn(aResponse().withStatus(200)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete request = new HttpDelete(getUrl() + "mealplanner/" + getUsername() + "/day/2020-05-15");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, deleteRequestedFor(urlPathMatching("/mealplanner/.*")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    void generateMealPlan() throws URISyntaxException, IOException {
        logger.info("тест: generateMealPlan - запущен" );

        logger.debug("создание мока для GET /mealplanner/generate");

        stubFor(get(urlPathEqualTo("/mealplanner/generate"))
                .withQueryParam("timeFrame", WireMock.equalTo("day"))
                .willReturn(aResponse()
                        .withStatus(200)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "mealplanner/generate");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("timeFrame", "day")
                .addParameter("targetCalories", "2000")
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathEqualTo("/mealplanner/generate")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    void generateShoppingList() throws URISyntaxException, IOException {
        logger.info("тест: generateShoppingList - запущен" );

        logger.debug("создание мока для GET /mealplanner/username/shopping-list/2020-05-15/2020-05-15");

        stubFor(get(urlPathMatching("/mealplanner/" + getUsername() + "/shopping-list/.*"))
                .willReturn(aResponse()
                        .withStatus(200)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/2020-05-15/2020-05-15");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathMatching("/mealplanner/" + getUsername() + "/shopping-list/.*")));
        assertEquals(200, response.getStatusLine().getStatusCode());

    }

    @Test
    void addToShoppingList() throws IOException, URISyntaxException {
        logger.info("тест: addToShoppingList - запущен" );
        logger.debug("создание мока для POST /mealplanner/username/shopping-list/items");


        stubFor(post(urlPathEqualTo("/mealplanner/" + getUsername() + "/shopping-list/items"))
                .withRequestBody(containing("baking"))
                .willReturn(aResponse()
                        .withStatus(200)
                ));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items");
        request.setEntity(new StringEntity("{\n" +
                "\t\"item\": \"1 package baking powder\",\n" +
                "\t\"aisle\": \"Baking\",\n" +
                "\t\"parse\": true\n" +
                "}"));

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, postRequestedFor(urlPathEqualTo("/mealplanner/" + getUsername() + "/shopping-list/items")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    void getShoppingList() throws URISyntaxException, IOException {
        logger.info("тест: getShoppingList - запущен" );

        ObjectMapper mapper = new ObjectMapper();
        Item baking = new Item();
        baking.setAisle("Baking");
        baking.setName("baking powder");
        baking.setCost(0.71);

        logger.info("созданы данные для заглушки. тип: " + Item.class);
        logger.debug("создание мока для GET /mealplanner/username/shopping-list/items");

        stubFor(get(urlPathEqualTo("/mealplanner/" + getUsername() + "/shopping-list/items"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(baking))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items");

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathEqualTo("/mealplanner/" + getUsername() + "/shopping-list/items")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        Item checkBody = mapper.readValue(response.getEntity().getContent(), Item.class);
        assertEquals("baking powder", checkBody.getName());
        assertEquals(0.71, checkBody.getCost());

    }


    @Test
    void computeShoppingList() throws URISyntaxException, IOException {
        logger.info("тест: computeShoppingList - запущен" );

        ObjectMapper mapper = new ObjectMapper();
        ShoppingList shoppingList = new ShoppingList();
        List<Aisle> aisles = new ArrayList<>();
        Aisle tomatoes = new Aisle();
        Aisle oile = new Aisle();
        aisles.add(tomatoes);
        aisles.add(oile);
        shoppingList.setAisles(aisles);

        logger.info("созданы данные для заглушки. тип: " + ShoppingList.class);
        logger.debug("создание мока для POST /mealplanner/username/shopping-list/compute");

        stubFor(post(urlPathEqualTo("/mealplanner/shopping-list/compute"))
                .withRequestBody(containing("tomatoes"))
                .withRequestBody(containing("Olive Oil"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(shoppingList))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getUrl() + "mealplanner/shopping-list/compute");
        request.setEntity(new StringEntity("{\n" +
                "    \"items\": [\n" +
                "        \"4 lbs tomatoes\",\n" +
                "        \"10 tomatoes\",\n" +
                "        \"20 Tablespoons Olive Oil\",\n" +
                "        \"6 tbsp Olive Oil\"\n" +
                "    ]\n" +
                "}"));

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("hash", getHash())
                .build();

        request.setURI(uri);
        logger.debug("создан http клиент");

        HttpResponse response = httpClient.execute(request);

        verify(1, postRequestedFor(urlPathEqualTo("/mealplanner/shopping-list/compute")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        ShoppingList checkBody = mapper.readValue(response.getEntity().getContent(), ShoppingList.class);
        assertEquals(2, checkBody.getAisles().size());

    }

}
