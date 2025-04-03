package com.example.fitnesstracker.model;

/**
 * Represents the association between a nutrition day entry and specific nutrition data.
 * This class tracks nutritional intake for a specific time of day, including
 * detailed macronutrient information and food item details.
 */
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
    private String nutritionPicturePath;

    /**
     * Constructs a new nutrition assignment with complete nutritional data.
     *
     * @param id                   the unique identifier for this assignment
     * @param nutritiondayId       the ID of the associated nutrition day
     * @param time                 the time of consumption (e.g., "08:00" for breakfast)
     * @param nutritionNameEnglish the English name of the food item
     * @param nutritionMass        the mass/quantity consumed in grams
     * @param nutritionCals        the calorie content in kcal
     * @param nutritionCarbs       the carbohydrates content in grams
     * @param nutritionFats        the fats content in grams
     * @param nutritionProteins    the proteins content in grams
     */
    public NutritiondayNutritionAssignment(int id, int nutritiondayId, String time,
                                           String nutritionNameEnglish, int nutritionMass,
                                           int nutritionCals, int nutritionCarbs,
                                           int nutritionFats, int nutritionProteins) {
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

    /**
     * Sets the German name of the food item.
     * @param nutritionNameGerman the localized German name of the nutrition item
     */
    public void setNutritionNameGerman(String nutritionNameGerman) {
        this.nutritionNameGerman = nutritionNameGerman;
    }

    /**
     * Sets the path to the food item's image.
     * @param nutritionPicturePath the file path or URL to the nutrition item's image
     */
    public void setNutritionPicturePath(String nutritionPicturePath) {
        this.nutritionPicturePath = nutritionPicturePath;
    }

    /**
     * @return the unique identifier of this assignment
     */
    public int getId() { return id; }

    /**
     * @return the ID of the associated nutrition day
     */
    public int getNutritiondayId() { return nutritiondayId; }

    /**
     * @return the time of consumption in "HH:mm" format
     */
    public String getTime() { return time; }

    /**
     * @return the English name of the food item
     */
    public String getNutritionNameEnglish() { return nutritionNameEnglish; }

    /**
     * @return the German name of the food item (localized)
     */
    public String getNutritionNameGerman() { return nutritionNameGerman; }

    /**
     * @return the consumed mass/quantity in grams
     */
    public int getNutritionMass() { return nutritionMass; }

    /**
     * @return the calorie content in kcal
     */
    public int getNutritionCals() { return nutritionCals; }

    /**
     * @return the carbohydrates content in grams
     */
    public int getNutritionCarbs() { return nutritionCarbs; }

    /**
     * @return the fats content in grams
     */
    public int getNutritionFats() { return nutritionFats; }

    /**
     * @return the proteins content in grams
     */
    public int getNutritionProteins() { return nutritionProteins; }

    /**
     * @return the file path or URL to the nutrition item's image
     */
    public String getNutritionPicturePath() { return nutritionPicturePath; }
}