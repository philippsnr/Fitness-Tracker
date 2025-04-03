package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a food product model from the Open Food Facts API.
 * This class maps the product data structure returned by the API,
 * including product name, image URL, and nutritional information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductModel {

    @JsonProperty("product_name")
    private String productName = "Keine Angabe";

    @JsonProperty("image_url")
    private String imageUrl = null;

    @JsonProperty("nutriments")
    private NutrimentsModel nutriments;

    /**
     * Gets the product name from the API response.
     * Defaults to "Keine Angabe" (Not specified) if no name is provided.
     *
     * @return the product name or default value if not specified
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     *
     * @param productName the name of the product to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the URL of the product image.
     *
     * @return the product image URL, or null if no image is available
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL of the product image.
     *
     * @param imageUrl the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the nutritional information model for this product.
     *
     * @return the {@link NutrimentsModel} containing detailed nutritional data
     */
    public NutrimentsModel getNutriments() {
        return nutriments;
    }

    /**
     * Sets the nutritional information model for this product.
     *
     * @param nutriments the {@link NutrimentsModel} to set
     */
    public void setNutriments(NutrimentsModel nutriments) {
        this.nutriments = nutriments;
    }
}