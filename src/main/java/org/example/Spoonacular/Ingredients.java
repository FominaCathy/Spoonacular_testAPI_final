package org.example.Spoonacular;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ingredients",
        "totalCost",
        "totalCostPerServing"
})

public class Ingredients {

    @JsonProperty("ingredients")
    private List<Ingredient> ingredients;
    @JsonProperty("totalCost")
    private float totalCost;
    @JsonProperty("totalCostPerServing")
    private float totalCostPerServing;

    @JsonProperty("ingredients")
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @JsonProperty("ingredients")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @JsonProperty("totalCost")
    public float getTotalCost() {
        return totalCost;
    }

    @JsonProperty("totalCost")
    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    @JsonProperty("totalCostPerServing")
    public float getTotalCostPerServing() {
        return totalCostPerServing;
    }

    @JsonProperty("totalCostPerServing")
    public void setTotalCostPerServing(float totalCostPerServing) {
        this.totalCostPerServing = totalCostPerServing;
    }

}
