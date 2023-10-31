
package org.example.Spoonacular;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "recipeNutrients"
})

public class ListRecipe {

    @JsonProperty("recipeNutrients")
    private List<RecipeNutrients> listRecipe;

    @JsonProperty("recipeNutrients")
    public List<RecipeNutrients> getListRecipe() {
        return listRecipe;
    }

    @JsonProperty("recipeNutrients")
    public void setListRecipe(List<RecipeNutrients> listRecipe) {
        this.listRecipe = listRecipe;
    }

}