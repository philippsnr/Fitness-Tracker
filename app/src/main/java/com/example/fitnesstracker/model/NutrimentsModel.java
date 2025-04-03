package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the nutritional information of a food product per 100 grams.
 * This model is designed to work with JSON data from nutrition APIs or databases,
 * using Jackson annotations for property mapping.
 */
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

    /**
     * Gets the energy content in kilocalories per 100 grams.
     *
     * @return the energy value in kcal/100g
     */
    public double getEnergyKcal() {
        return energyKcal;
    }

    /**
     * Sets the energy content in kilocalories per 100 grams.
     *
     * @param energyKcal the energy value in kcal/100g to set
     */
    public void setEnergyKcal(double energyKcal) {
        this.energyKcal = energyKcal;
    }

    /**
     * Gets the carbohydrates content per 100 grams.
     *
     * @return the carbohydrates value in grams/100g
     */
    public double getCarbohydrates() {
        return carbohydrates;
    }

    /**
     * Sets the carbohydrates content per 100 grams.
     *
     * @param carbohydrates the carbohydrates value in grams/100g to set
     */
    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    /**
     * Gets the fat content per 100 grams.
     *
     * @return the fat value in grams/100g
     */
    public double getFat() {
        return fat;
    }

    /**
     * Sets the fat content per 100 grams.
     *
     * @param fat the fat value in grams/100g to set
     */
    public void setFat(double fat) {
        this.fat = fat;
    }

    /**
     * Gets the protein content per 100 grams.
     *
     * @return the protein value in grams/100g
     */
    public double getProteins() {
        return proteins;
    }

    /**
     * Sets the protein content per 100 grams.
     *
     * @param proteins the protein value in grams/100g to set
     */
    public void setProteins(double proteins) {
        this.proteins = proteins;
    }
}