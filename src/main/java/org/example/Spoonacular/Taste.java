
package org.example.Spoonacular;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sweetness",
        "saltiness",
        "sourness",
        "bitterness",
        "savoriness",
        "fattiness",
        "spiciness"
})

public class Taste {

    @JsonProperty("sweetness")
    private float sweetness;
    @JsonProperty("saltiness")
    private float saltiness;
    @JsonProperty("sourness")
    private float sourness;
    @JsonProperty("bitterness")
    private float bitterness;
    @JsonProperty("savoriness")
    private float savoriness;
    @JsonProperty("fattiness")
    private float fattiness;
    @JsonProperty("spiciness")
    private float spiciness;

    @JsonProperty("sweetness")
    public float getSweetness() {
        return sweetness;
    }

    @JsonProperty("sweetness")
    public void setSweetness(float sweetness) {
        this.sweetness = sweetness;
    }

    @JsonProperty("saltiness")
    public float getSaltiness() {
        return saltiness;
    }

    @JsonProperty("saltiness")
    public void setSaltiness(float saltiness) {
        this.saltiness = saltiness;
    }

    @JsonProperty("sourness")
    public float getSourness() {
        return sourness;
    }

    @JsonProperty("sourness")
    public void setSourness(float sourness) {
        this.sourness = sourness;
    }

    @JsonProperty("bitterness")
    public float getBitterness() {
        return bitterness;
    }

    @JsonProperty("bitterness")
    public void setBitterness(float bitterness) {
        this.bitterness = bitterness;
    }

    @JsonProperty("savoriness")
    public float getSavoriness() {
        return savoriness;
    }

    @JsonProperty("savoriness")
    public void setSavoriness(float savoriness) {
        this.savoriness = savoriness;
    }

    @JsonProperty("fattiness")
    public float getFattiness() {
        return fattiness;
    }

    @JsonProperty("fattiness")
    public void setFattiness(float fattiness) {
        this.fattiness = fattiness;
    }

    @JsonProperty("spiciness")
    public float getSpiciness() {
        return spiciness;
    }

    @JsonProperty("spiciness")
    public void setSpiciness(float spiciness) {
        this.spiciness = spiciness;
    }

}
