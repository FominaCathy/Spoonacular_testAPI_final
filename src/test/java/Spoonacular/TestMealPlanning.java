package Spoonacular;

import io.qameta.allure.*;
import org.example.Spoonacular.ShoppingList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Epic(value = "Тестирование API https://spoonacular.com/food-api/")
public class TestMealPlanning extends AbstractTestSpoo {
    private int calories = 2000;
    String idBanana;
    private static final Logger logger = LoggerFactory.getLogger(TestMealPlanning.class);


    @Test
    @Order(1)
    @Owner("Fomina Kat")
    @DisplayName("Add to Meal Plan")
    @Description("Add an item to the user's meal plan.")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.BLOCKER)
    void addToMealPlan() {
        logger.info("тест POST Add to Meal Plan - запущен");
        idBanana = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\n" +
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
                        "}")
                .when()
                .post(getUrl() + "mealplanner/" + getUsername() + "/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    @Order(2)
    @DisplayName("Get Meal Plan Day")
    @Description("Retrieve a meal planned day for the given user. ")
    @Owner("Fomina Kat")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.NORMAL)
    void getMealPlanDay() {
        logger.info("тест GET  Get Meal Plan Day - запущен");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .get(getUrl() + "mealplanner/" + getUsername() + "/day/2020-05-15")
                .then()
                .statusCode(200);

    }

    @Test
    @Order(3)
    @Owner("Fomina Kat")
    @DisplayName("Clear Meal Plan Day")
    @Description("Delete all planned items from the user's meal plan for a specific day.")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.NORMAL)
    void clearMealPlanDay() {
        logger.info("тест DELETE  Clear Meal Plan Day - запущен");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .delete(getUrl() + "mealplanner/" + getUsername() + "/day/2020-05-15")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    @DisplayName("Generate Meal Plan")
    @Description("Generate a meal plan with three meals per day (breakfast, lunch, and dinner).")
    @Owner("Fomina Kat")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.NORMAL)
    void generateMealPlan() {
        logger.info("тест GET  Generate Meal Plan - запущен");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("timeFrame", "day")
                .queryParam("targetCalories", calories)
                .when()
                .get(getUrl() + "mealplanner/generate")
                .then()
                .statusCode(200);
    }


    @Test
    @Order(5)
    @DisplayName("Generate Shopping List")
    @Owner("Fomina Kat")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.NORMAL)
    void generateShoppingList() {
        logger.info("тест POST Generate Shopping List - запущен");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .post(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/2020-05-15/2020-05-15")
                .then()
                .statusCode(200)
        ;
    }

    @Test
    @Order(6)
    @Owner("Fomina Kat")
    @DisplayName("Add To Shopping List")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.NORMAL)
    void addToShoppingList() {
        logger.info("тест POST Add to Shopping List - запущен");

        String id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\n" +
                        "\t\"item\": \"1 package baking powder\",\n" +
                        "\t\"aisle\": \"Baking\",\n" +
                        "\t\"parse\": true\n" +
                        "}")
                .when()
                .post(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .delete(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items/" + id)
                .then()
                .statusCode(200);

    }

    @Test
    @Order(7)
    @Owner("Fomina Kat")
    @DisplayName("Get Shopping List")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.CRITICAL)
    void getShoppingList() {
        logger.info("тест GET Get Shopping List - запущен");

        String id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\n" +
                        "\t\"item\": \"1 package baking powder\",\n" +
                        "\t\"aisle\": \"Baking\",\n" +
                        "\t\"parse\": true\n" +
                        "}")
                .when()
                .post(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        // вариант с jackson
        ShoppingList shoppingList = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .get(getUrl() + "mealplanner/" + getUsername() + "/shopping-list")
                .body()
                .as(ShoppingList.class);

//        assertThat(shoppingList.getCost(), equalTo(0.71));
        assertThat(shoppingList.getAisles().get(0).getItems().get(0).getName(), equalTo("baking powder"));
        assertThat(shoppingList.getAisles().get(0).getItems().get(0).getMeasures().getOriginal().getAmount(), equalTo(1.0));

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .delete(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items/" + id)
                .then()
                .statusCode(200);
    }


    @Test
    @Order(8)
    @Owner("Fomina Kat")
    @DisplayName("Compute Shopping List")
    @Story(value = "Meal Planning")
    @Severity(SeverityLevel.CRITICAL)
    void computeShoppingList() {
        logger.info("тест POST Compute Shopping List - запущен");

        given()
                .queryParam("apiKey", getApiKey())
                .body("{\n" +
                        "    \"items\": [\n" +
                        "        \"4 lbs tomatoes\",\n" +
                        "        \"10 tomatoes\",\n" +
                        "        \"20 Tablespoons Olive Oil\",\n" +
                        "        \"6 tbsp Olive Oil\"\n" +
                        "    ]\n" +
                        "}")
                .when()
                .post(getUrl() + "mealplanner/shopping-list/compute")
                .then()
                .statusCode(200)
                .assertThat()
                .body("aisles.items.name", hasSize(2));
    }

}
