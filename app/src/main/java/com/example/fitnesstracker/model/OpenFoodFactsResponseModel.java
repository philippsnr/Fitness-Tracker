package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents the response model from the Open Food Facts API.
 * This class encapsulates a list of product data returned from food product queries.
 * It is designed to handle JSON responses with unknown properties gracefully.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsResponseModel {

    private List<ProductModel> products;

    /**
     * Sets the list of products returned from the API response.
     *
     * @param products the list of {@link ProductModel} objects containing food product data
     */
    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    /**
     * Gets the list of products from the API response.
     *
     * @return a list of {@link ProductModel} objects containing detailed food product information,
     *         or null if no products were found in the response
     */
    public List<ProductModel> getProducts() {
        return products;
    }
}