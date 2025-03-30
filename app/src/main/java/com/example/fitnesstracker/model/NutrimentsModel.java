package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrimentsModel {

    @JsonProperty("energy-kcal_100g")
    private double energyKcal = 0.0;

    @JsonProperty("carbohydrates_100g")
    private double carbohydrates = 0.0;

    @JsonProperty("fat_100g")
    private double fat = 0.0;

    @JsonProperty("proteins_100g")
    private double proteins = 0.0;

    // Getter und Setter
    public double getEnergyKcal() {
        return energyKcal;
    }

    public void setEnergyKcal(double energyKcal) {
        this.energyKcal = energyKcal;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

}
