package com.example.fitnesstracker.model;

import java.util.List;

public class OpenFoodFactsResponseModel {

        public int count;
        public int page;
        public int page_count;
        public int page_size;
        public List<Product> products;
        public int skip;

    public static class Product {
        public String product_name;
        public String image_url;
        public String code;
    }
}
