package com.example.fitnesstracker.model;

public class NutritiondayNutritionAssignment {
    private final int id;
    private final int nutritiondayId;
    private final String time;
    private final String nutritionNameEnglish;
    private String nutritionNameGerman;
    private final int nutritionMass;
    private final int nutritionCals;
    private final int nutritionCarbs;
    private final int nutritionFats;
    private final int nutritionProteins;

    public NutritiondayNutritionAssignment ( int id, int nutritiondayId, String time, String nutritionNameEnglish, int nutritionMass, int nutritionCals, int nutritionCarbs, int nutritionFats, int nutritionProteins) {
        this.id = id;
        this.nutritiondayId = nutritiondayId;
        this.time = time;
        this.nutritionNameEnglish = nutritionNameEnglish;
        this.nutritionMass = nutritionMass;
        this.nutritionCals = nutritionCals;
        this.nutritionCarbs = nutritionCarbs;
        this.nutritionFats = nutritionFats;
        this.nutritionProteins = nutritionProteins;
    }
    public void setNutritionNameGerman (String nutritionNameGerman) { this.nutritionNameGerman = nutritionNameGerman; }

    public int getId() { return id; }
    public int getNutritiondayId() { return nutritiondayId; }
    public String getTime() { return time; }
    public String getNutritionNameEnglish() { return nutritionNameEnglish; }
    public String getNutritionNameGerman() { return nutritionNameGerman; }
    public int getNutritionMass() { return nutritionMass; }
    public int getNutritionCals() { return nutritionCals; }
    public int getNutritionCarbs() { return nutritionCarbs; }
    public int getNutritionFats() { return nutritionFats; }
    public int getNutritionProteins() { return nutritionProteins; }
}
