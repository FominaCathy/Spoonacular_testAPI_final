
package org.example.Spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "image",
        "imageType",
        "calories",
        "protein",
        "fat",
        "carbs"
})
@JsonIgnoreProperties(
        {"bad", "good", "nutrients", "properties", "flavonoids", "ingredients", "caloricBreakdown",
                "weightPerServing", "expires", "isStale"}
)

public class RecipeNutrients extends Result{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("image")
    private String image;
    @JsonProperty("imageType")
    private String imageType;
    @JsonProperty("calories")
    private Integer calories;
    @JsonProperty("protein")
    private String protein;
    @JsonProperty("fat")
    private String fat;
    @JsonProperty("carbs")
    private String carbs;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("imageType")
    public String getImageType() {
        return imageType;
    }

    @JsonProperty("imageType")
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @JsonProperty("calories")
    public Integer getCalories() {
        return calories;
    }

    @JsonProperty("calories")
    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @JsonProperty("protein")
    public String getProtein() {
        return protein;
    }

    @JsonProperty("protein")
    public void setProtein(String protein) {
        this.protein = protein;
    }

    @JsonProperty("fat")
    public String getFat() {
        return fat;
    }

    @JsonProperty("fat")
    public void setFat(String fat) {
        this.fat = fat;
    }

    @JsonProperty("carbs")
    public String getCarbs() {
        return carbs;
    }

    @JsonProperty("carbs")
    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

}
