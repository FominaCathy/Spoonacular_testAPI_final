package org.example.Spoonacular;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "aisles",
        "cost",
        "startDate",
        "endDate"
})

public class ShoppingList {

    @JsonProperty("aisles")
    private List<Aisle> aisles;
    @JsonProperty("cost")
    private Double cost;
    @JsonProperty("startDate")
    private Integer startDate;
    @JsonProperty("endDate")
    private Integer endDate;

    @JsonProperty("aisles")
    public List<Aisle> getAisles() {
        return aisles;
    }

    @JsonProperty("aisles")
    public void setAisles(List<Aisle> aisles) {
        this.aisles = aisles;
    }

    @JsonProperty("cost")
    public Double getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(Double cost) {
        this.cost = cost;
    }

    @JsonProperty("startDate")
    public Integer getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public Integer getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

}
