package Spoonacular;

import org.example.Spoonacular.ShoppingList;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class TestMealPlanning extends AbstractTestSpoo {
    private int calories = 2000;
    private String idBanana;


    @Test
    @Order(1)
    void addToMealPlan() {

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
    void getMealPlanDay() {
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
    void clearMealPlanDay() {
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
    void generateMealPlan() {

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
    void generateShoppingList() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .get(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/2020-05-15/2020-05-15")
                .then()
                .statusCode(200)
        ;
    }

//    @Test
//    @Disabled
//    void deleteShoppingList() {
//        given()
//                .queryParam("apiKey", getApiKey())
//                .queryParam("hash", getHash())
//                .when()
//                .delete(getUrl() + "mealplanner/" + getUsername() + "/shopping-list/items/" + 1726144)
//                .then()
//                .statusCode(200);
//    }

    @Test
    @Order(6)
    void addToShoppingList() {
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
    void getShoppingList() {
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
        //вариант с json
//        JsonPath response = given()
//                .queryParam("apiKey", getApiKey())
//                .queryParam("hash", getHash())
//                .when()
//                .get(getUrl() + "mealplanner/" + getUsername() + "/shopping-list")
//                .body()
//                .jsonPath();
//
//        assertThat(response.get("cost"), equalTo(0.71F));
//        assertThat(response.get("aisles[0].items[0].name"), equalTo("baking powder"));
//        assertThat(response.get("aisles[0].items[0].measures.original.amount"), equalTo(1.0f));

        // вариант с jackson
        ShoppingList shoppingList = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when()
                .get(getUrl() + "mealplanner/" + getUsername() + "/shopping-list")
                .body()
                .as(ShoppingList.class);

        assertThat(shoppingList.getCost(), equalTo(0.71));
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
    void computeShoppingList() {
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
