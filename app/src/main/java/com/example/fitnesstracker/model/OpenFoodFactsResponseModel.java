package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsResponseModel {

    private List<ProductModel> products;

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public List<ProductModel> getProducts() {
        return products;
    }
}
