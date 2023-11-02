package Spoonacular;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.example.Spoonacular.Equipment;
import org.example.Spoonacular.EquipmentRecipe;
import org.example.Spoonacular.Taste;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic(value = "Тестирование API https://spoonacular.com/food-api/")
public class TestRecipe extends AbstractTestSpoo {
    private static HashMap<String, String> recipe;
    private static final Logger logger = LoggerFactory.getLogger(TestRecipe.class);

    public TestRecipe() throws FileNotFoundException {

        logger.info("Получение данных для запросов Recipe из yaml-файла");//
        InputStream inputStream = new FileInputStream(new File("src/main/resources/recipe.yml"));
        Yaml yaml = new Yaml();
        recipe = yaml.loadAs(inputStream, HashMap.class);

    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Search Recipes using advanced filtering")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void searchRecipes() {
        logger.info("Тест GET search Recipes запущен");//

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", recipe.get("query_recipes"))
                .header("Content-Type", "application/json")
                .when()
                .get(getUrl() + "recipes/complexSearch")
                .then().log().body()
                .statusCode(200)

                .assertThat()
                .body("results.title", hasItem(containsStringIgnoringCase(recipe.get("query_recipes"))))
        ;
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Search Recipes by Nutrients")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void searchRecipesByNutrients() {
        logger.info("Тест GET search Recipes By Nutrients запущен");//

        int maxCalory = 500;

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", recipe.get("number"))
                .queryParam("maxCalories", maxCalory)
                .when()
                .get(getUrl() + "recipes/findByNutrients")
                .then().log().body()
                .statusCode(200)
                .assertThat()
                .body("calories", everyItem(lessThanOrEqualTo(maxCalory)))
        ;
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Get Random Recipes")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void getRandomRecipes() {
        logger.info("Тест GET getRandomRecipes запущен");//

        String tags = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("tags", "vegetarian")
                .when()
                .get(getUrl() + "recipes/random")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("recipes.vegetarian")
                .toString();

        Assertions.assertTrue(tags.contains("true"));

    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Autocomplete Recipe Search")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.CRITICAL)
    void autocompleteRecipeSearch() {
        logger.info("Тест GET autocompleteRecipeSearch запущен");//

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "chick")
                .queryParam("number", 5)
                .when()
                .get(getUrl() + "recipes/autocomplete")
                .then()
                .statusCode(200)
                .assertThat()
                .body("title", hasItem(containsStringIgnoringCase("chick")))
                .body("title", hasSize(lessThanOrEqualTo(5)));


    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Taste by ID")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.MINOR)
    void tasteByID() {
        logger.info("Тест GET tasteByID запущен");//

        Taste taste = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/json")
                .when()
                .get(getUrl() + "recipes/69095/tasteWidget.json")
                .then()
                .statusCode(200)
                .extract()
                .as(Taste.class);

        assertThat(taste.getSweetness(), equalTo(48.35F));
        assertThat(taste.getSaltiness(), equalTo(45.48F));
        assertThat(taste.getBitterness(), equalTo(19.25F));
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Equipment by ID")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void equipmentByID() {
        logger.info("Тест GET equipmentByID запущен");//

        EquipmentRecipe equipmentRecipe = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getUrl() + "recipes/1003464/equipmentWidget.json")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EquipmentRecipe.class);

        List<String> listExp = new ArrayList<>();
        for (Equipment equipment : equipmentRecipe.getEquipment()) {
            listExp.add(equipment.getName().toString());
        }

        assertThat(listExp, containsInAnyOrder("oven", "pie form", "bowl", "frying pan"));
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Price Breakdown by ID")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.MINOR)
    void priceBreakdownByID() {
        logger.info("Тест GET priceBreakdownByID запущен");//

        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getUrl() + "recipes/1003464/priceBreakdownWidget.json")
                .then()
                .assertThat()
                .statusCode(200)
                .time(lessThan(2000L))
                .body("totalCost", equalTo(832.45f))
                .body("totalCostPerServing", equalTo(104.06f))
                .body("ingredients[0].price", equalTo(174.43f));

    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Nutrition by ID")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void nutritionByID() {
        logger.info("Тест GET nutritionByID запущен");//

        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getUrl() + "recipes/1003464/nutritionWidget.json")
                .then()
                .assertThat()
                .statusCode(200)
                .body("nutrients[0].amount", equalTo(899.16f))
                .body("fat", equalTo("45g"))
                .body("carbs", equalTo("111g"));
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Ingredients by ID")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void ingredientsByID() {
        logger.info("Тест GET ingredientsByID запущен");//

        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getUrl() + "recipes/1003464/ingredientWidget.json")
                .then()
                .assertThat()
                .statusCode(200)
                .time(lessThan(2000L))
                .contentType(ContentType.JSON)
                .body("ingredients[0].name", equalTo("blueberries"))
                .body("ingredients[0].amount.metric.value", equalTo(222.0f));

    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Analyze Recipe")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void analyzeRecipe() {
        logger.info("Тест POST analyzeRecipe запущен");//

        String title = given()
                .queryParam("apiKey", getApiKey())
                .body("{\n" +
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

                        "}")
                .when()
                .post(getUrl() + "recipes/analyze")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("title")
                .toString();

        assertThat(title, equalTo("Spaghetti Carbonara"));
    }

    @Test
    @Owner("Fomina Kat")
    @DisplayName("Summarize Recipe")
    @Story(value = "Recipes")
    @Severity(SeverityLevel.NORMAL)
    void summarizeRecipe() {
        logger.info("Тест GET summarizeRecipe запущен");//

        given()
                .when()
                .request(Method.GET, getUrl() + "recipes/4632/summary" + "?apiKey={apiKey}", getApiKey())
                .then()
                .assertThat()
                .statusCode(200)
                .body("title", equalTo("Soy-and-Ginger-Glazed Salmon with Udon Noodles"));
    }

}


