package org.example.Spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "price"})
@JsonIgnoreProperties({"image", "amount"})

public class Ingredient {

    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private float price;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonProperty("price")
    public float getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(float price) {
        this.price = price;
    }


}