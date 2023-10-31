package SpoonacularMok;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.Spoonacular.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Spoonacular.AbstractTestSpoo.getApiKey;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestRecipeMok extends AbstractTestSpooMok {
    private static HashMap<String, String> recipe;

    public TestRecipeMok() throws FileNotFoundException {
        //данные для запросов Recipe
        InputStream inputStream = new FileInputStream(new File("src/main/resources/recipe.yml"));
        Yaml yaml = new Yaml();
        recipe = yaml.loadAs(inputStream, HashMap.class);

    }

    //
    @Test
    @Disabled
    void searchRecipes() throws IOException, URISyntaxException {
        Result resultOne = new Result();
        resultOne.setTitle("Pasta carbonara");
        Result resultTwo = new Result();
        resultTwo.setTitle("Pasta With Gorgonzola Sauce");
        Result resultThree = new Result();
        resultThree.setTitle("Pasta With Italian Sausage");
        List<Result> listResult = new ArrayList<>();
        listResult.add(resultOne);
        listResult.add(resultTwo);
        listResult.add(resultThree);
        SearchResipes bodyResponse = new SearchResipes();
        bodyResponse.setResults(listResult);

        ObjectMapper mapper = new ObjectMapper();

        stubFor(get(urlPathEqualTo("/recipes/complexSearch"))
                .withQueryParam("query", WireMock.equalTo(recipe.get("query_recipes")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(bodyResponse))
                ));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "/recipes/complexSearch");

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("query", recipe.get("query_recipes"))
                .build();
        request.setURI(uri);

        HttpResponse responseRecipes = httpClient.execute(request);

        verify(getRequestedFor(urlPathMatching("/recipes/complexSearch")));
        assertEquals(200, responseRecipes.getStatusLine().getStatusCode());
        SearchResipes bodyResponseRecipes =
                mapper.readValue(responseRecipes.getEntity().getContent(), SearchResipes.class);
        for (Result item : bodyResponseRecipes.getResults()) {
            assertThat(item.getTitle(), containsStringIgnoringCase(recipe.get("query_recipes")));
        }
    }


    @Test
    @Disabled
    void searchRecipesByNutrients() throws URISyntaxException, IOException {
        int maxCalory = 500;
        ObjectMapper mapper = new ObjectMapper();
        ListRecipe recipes = new ListRecipe();

        List<RecipeNutrients> listRecipe = new ArrayList<>();
        RecipeNutrients recipeBanana = new RecipeNutrients();
        recipeBanana.setCalories(186);
        RecipeNutrients recipeCream = new RecipeNutrients();
        recipeCream.setCalories(434);
        listRecipe.add(recipeBanana);
        listRecipe.add(recipeCream);

        recipes.setListRecipe(listRecipe);

        stubFor(get(urlPathEqualTo("/recipes/findByNutrients"))
                .withQueryParam("maxCalories", equalTo("500"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(recipes))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/findByNutrients");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("maxCalories", "500")
                .build();
        request.setURI(uri);
        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathEqualTo("/recipes/findByNutrients")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        ListRecipe checkBody = mapper.readValue(response.getEntity().getContent(), ListRecipe.class);
        for (RecipeNutrients item : checkBody.getListRecipe()) {
            assertTrue(item.getCalories() < 500);
        }

    }


    @Test
    @Disabled
    void getRandomRecipes() throws URISyntaxException, IOException {

        stubFor(get(urlPathEqualTo("/recipes/random"))
                .withQueryParam("tags", equalTo("vegetarian"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("\"vegetarian\": true")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/random");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("tags", "vegetarian")
                .build();
        request.setURI(uri);
        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlPathEqualTo("/recipes/random")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("\"vegetarian\": true", convertResponseToString(response));

    }


    @Test
    void autocompleteRecipeSearch() throws IOException, URISyntaxException {
        Result resultOne = new Result();
        resultOne.setTitle("chicken");
        Result resultTwo = new Result();
        resultTwo.setTitle("chickaritos");
        Result resultThree = new Result();
        resultThree.setTitle("chicken pie");
        List<Result> listResult = new ArrayList<>();
        listResult.add(resultOne);
        listResult.add(resultTwo);
        listResult.add(resultThree);
        SearchResipes bodyResponse = new SearchResipes();
        bodyResponse.setResults(listResult);

        ObjectMapper mapper = new ObjectMapper();

        stubFor(get(urlPathEqualTo("/recipes/autocomplete"))
                .withQueryParam("query", equalTo("chick"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(bodyResponse))
                ));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "/recipes/autocomplete");

        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .addParameter("query", "chick")
                .build();
        request.setURI(uri);

        HttpResponse responseRecipes = httpClient.execute(request);

        verify(getRequestedFor(urlPathMatching("/recipes/autocomplete")));
        assertEquals(200, responseRecipes.getStatusLine().getStatusCode());
        SearchResipes bodyResponseRecipes =
                mapper.readValue(responseRecipes.getEntity().getContent(), SearchResipes.class);
        for (Result item : bodyResponseRecipes.getResults()) {
            assertThat(item.getTitle(), containsStringIgnoringCase("chick"));
        }
    }

    @Test
    @Disabled
    void tasteByID() throws IOException, URISyntaxException {
        //создадим мок объект
        ObjectMapper mapper = new ObjectMapper();
        Taste bodyResponse = new Taste();
        bodyResponse.setSweetness(48.35F);
        bodyResponse.setSaltiness(45.48F);
        bodyResponse.setBitterness(19.25F);

        stubFor(get(urlPathEqualTo("/recipes/69095/tasteWidget.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(bodyResponse))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getUrl() + "recipes/69095/tasteWidget.json");
        URI uriTaste = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .build();
        request.setURI(uriTaste);

        HttpResponse responseTaste = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/recipes/69095/tasteWidget.json")));

        Taste bodyResponseTaste = mapper.readValue(responseTaste.getEntity().getContent(), Taste.class);
        assertEquals(200, responseTaste.getStatusLine().getStatusCode());
        assertEquals(48.35F, bodyResponseTaste.getSweetness());
        assertEquals(45.48F, bodyResponseTaste.getSaltiness());
        assertEquals(19.25F, bodyResponseTaste.getBitterness());

    }


    @Test
    @Disabled
    void equipmentByID() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Equipment oven = new Equipment();
        oven.setName("oven");
        Equipment pieForm = new Equipment();
        pieForm.setName("pie form");
        Equipment bowl = new Equipment();
        bowl.setName("bowl");
        Equipment frying = new Equipment();
        frying.setName("frying pan");

        List<Equipment> listRecipe = new ArrayList<>();
        listRecipe.add(oven);
        listRecipe.add(pieForm);
        listRecipe.add(bowl);
        listRecipe.add(frying);

        EquipmentRecipe equipmentRecipe = new EquipmentRecipe();
        equipmentRecipe.setEquipment(listRecipe);

        stubFor(get(urlPathEqualTo("/recipes/1003464/equipmentWidget.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(equipmentRecipe))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/1003464/equipmentWidget.json");
        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlEqualTo("/recipes/1003464/equipmentWidget.json")));
        EquipmentRecipe bodyResponse = mapper.readValue(response.getEntity().getContent(), EquipmentRecipe.class);

        List<String> listExp = new ArrayList<>();
        for (Equipment equipment : bodyResponse.getEquipment()) {
            listExp.add(equipment.getName());
        }

        assertThat(listExp, containsInAnyOrder("oven", "pie form", "bowl", "frying pan"));
    }


    @Test
    @Disabled
    void priceBreakdownByID() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Ingredient ingredient = new Ingredient();
        ingredient.setPrice(174.43f);
        Ingredients bodyResponse = new Ingredients();
        List<Ingredient> listIngredient = new ArrayList<>();
        listIngredient.add(ingredient);
        bodyResponse.setIngredients(listIngredient);
        bodyResponse.setTotalCost(832.45f);
        bodyResponse.setTotalCostPerServing(104.06f);

        stubFor(get(urlPathEqualTo("/recipes/1003464/priceBreakdownWidget.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mapper.writeValueAsString(bodyResponse))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/1003464/priceBreakdownWidget.json");
        HttpResponse httpResponse = httpClient.execute(request);
        Ingredients bodyCheck = mapper.readValue(httpResponse.getEntity().getContent(), Ingredients.class);

        verify(getRequestedFor(urlPathEqualTo("/recipes/1003464/priceBreakdownWidget.json")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(832.45f, bodyCheck.getTotalCost());
        assertEquals(104.06f, bodyCheck.getTotalCostPerServing());
        assertEquals(174.43f, bodyCheck.getIngredients().get(0).getPrice());
    }


    @Test
    void nutritionByID() throws IOException {

        RecipeNutrients recipe = new RecipeNutrients();
        recipe.setCalories(899);
        recipe.setFat("45g");
        recipe.setCarbs("111g");

        ObjectMapper mapper = new ObjectMapper();


        stubFor(get(urlPathEqualTo("/recipes/1003464/nutritionWidget.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(recipe))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/1003464/nutritionWidget.json");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        RecipeNutrients checkBody = mapper.readValue(response.getEntity().getContent(), RecipeNutrients.class);
        verify(getRequestedFor(urlPathEqualTo("/recipes/1003464/nutritionWidget.json")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(899, checkBody.getCalories());
        assertEquals("45g", checkBody.getFat());
        assertEquals("111g", checkBody.getCarbs());
    }


    @Test
    void ingredientsByID() throws IOException {
        Ingredients ingredients = new Ingredients();
        Ingredient blueberries = new Ingredient();
        blueberries.setName("blueberries");
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(blueberries);
        ingredients.setIngredients(ingredientList);

        ObjectMapper mapper = new ObjectMapper();

        stubFor(get(urlPathEqualTo("/recipes/1003464/ingredientWidget.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(ingredients))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getUrl() + "recipes/1003464/ingredientWidget.json");

        HttpResponse response = httpClient.execute(request);

        verify(1, getRequestedFor(urlPathEqualTo("/recipes/1003464/ingredientWidget.json")));
        assertEquals("blueberries",
                mapper.readValue(response.getEntity().getContent(), Ingredients.class).getIngredients().get(0).getName());

    }

    @Test
    void analyzeRecipe() throws IOException {

        stubFor(post(urlPathEqualTo("/recipes/analyze"))
                .withRequestBody(containing("Spaghetti Carbonara"))
                .withRequestBody(containing("Bring a large pot of water to a boil and season generously with salt"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("\"title\": \"Spaghetti Carbonara\"")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getUrl() + "recipes/analyze");
        request.setEntity(new StringEntity(
                "{\n" +
                        "    \"title\": \"Spaghetti Carbonara\",\n" +
                        "    \"servings\": 2,\n" +
                        "    \"ingredients\": [\n" +
                        "        \"1 lb spaghetti\",\n" +
                        "        \"3.5 oz pancetta\",\n" +
                        "        \"2 Tbsps olive oil\",\n" +
                        "        \"1  egg\",\n" +
                        "        \"0.5 cup parmesan cheese\"\n" +
                        "    ],\n" +
                        "    \"instructions\": " +
                        "\"Bring a large pot of water to a boil and season generously with salt. " +
                        "Add the pasta to the water once boiling and cook until al dente. " +
                        "Reserve 2 cups of cooking water and drain the pasta. \"\n" +

                        "}"
        ));

        HttpResponse response = httpClient.execute(request);

        verify(1, postRequestedFor(urlPathEqualTo("/recipes/analyze")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(convertResponseToString(response).contains("Spaghetti Carbonara"));
    }


    @Test
    @Disabled
    void summarizeRecipe() throws URISyntaxException, IOException {
        stubFor(get(urlPathEqualTo("/recipes/4632/summary"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("\"title\": \"Soy-and-Ginger-Glazed Salmon with Udon Noodles\"")
                ));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getUrl() + "recipes/4632/summary");
        URI uriTaste = new URIBuilder(request.getURI())
                .addParameter("apiKey", getApiKey())
                .build();
        request.setURI(uriTaste);

        HttpResponse response = httpClient.execute(request);
        String strResponse = convertResponseToString(response);
        verify(getRequestedFor(urlPathEqualTo("/recipes/4632/summary")));

        assertThat(strResponse, containsString("Soy-and-Ginger-Glazed Salmon with Udon Noodles"));

    }

}


