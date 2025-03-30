package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductModel {

        @JsonProperty("product_name")
        private String productName = "Keine Angabe";

        @JsonProperty("image_url")
        private String imageUrl = null;

        @JsonProperty("nutriments")
        private NutrimentsModel nutriments;

        // Getter und Setter
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public NutrimentsModel getNutriments() {
            return nutriments;
        }

        public void setNutriments(NutrimentsModel nutriments) {
            this.nutriments = nutriments;
        }
    }
