package com.example.fitnesstracker.ui.nutrition;

import android.util.Log;

import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CallOpenFoodFactsApi {

    private final String basicUrl = "https://world.openfoodfacts.org/cgi/search.pl?";
    protected void callOpenFoodFactsApiForNutritionNames(String nutritionName) {
        String apiUrlToAdd = "search_terms=" + nutritionName + "&search_simple=1&action=process&json=1&fields=product_name,image_url,code";
        String apiUrl = basicUrl + apiUrlToAdd;
        Log.d("Nutrition", apiUrl);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executeApiCall(executor, apiUrl);
    }

    private void executeApiCall(ExecutorService executor, String apiUrl) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonResponse = apiCall(apiUrl);
                    if ( jsonResponse != null ) {
                        OpenFoodFactsResponseModel products = parseJsonResponseToObject(jsonResponse);
                        Log.d("Nutrition", "Json erfolgreich in Objekt umgewandelt");

                        //nur zum debuggen, muss später raus
                        for (OpenFoodFactsResponseModel.Product product : products.products) {
                            Log.d("Nutrition", "Name: " + product.product_name + "Code: " + product.code + "Bild: " + product.image_url);
                        }
                    }
                } catch(Exception e) {
                    Log.e("Nutrition", "Fehler beim API-Call", e);
                }
            }
        });
        executor.shutdown();
    }

    private String apiCall(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if( responseCode == HttpURLConnection.HTTP_OK ) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null ) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            Log.e("Nutrition", "Http-Antwort war fehlerhaft");
            return null;
        }
    }

    private OpenFoodFactsResponseModel parseJsonResponseToObject( String jsonResponse ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OpenFoodFactsResponseModel products = objectMapper.readValue(jsonResponse, OpenFoodFactsResponseModel.class);
        return products;
    }
}
