package com.example.fitnesstracker.ui.nutrition;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CallOpenFoodFactsApi {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess (List<OpenFoodFactsResponseModel.Product> result);
        void onFailure (Exception e);
    }

    private static final String basicUrl = "https://world.openfoodfacts.org/cgi/search.pl?";

    /**
     * Initiiert den API-Aufruf um verfügbare Produkte anhand des Namens zu finden
     * @param nutritionName Name nach dem gesucht werden soll
     */
    protected static void callOpenFoodFactsApiForNutritionNames(String nutritionName, Callback callback) {
        String apiUrlToAdd = "search_terms=" + nutritionName + "&search_simple=1&action=process&json=1&fields=product_name,image_url,code";
        String apiUrl = basicUrl + apiUrlToAdd;
        Log.d("Nutrition", apiUrl);

        executeApiCall(apiUrl, callback);
    }

    /**
     * Controller des API-Aufrufs
     * @param callback
     * @param apiUrl URL des API-Endpunktes
     */
    private static void executeApiCall(String apiUrl, Callback callback) {
        executor.execute(() -> {
            List<OpenFoodFactsResponseModel.Product> productList;
            try {
                    String jsonResponse = apiCall(apiUrl);
                    if ( jsonResponse != null ) {
                        OpenFoodFactsResponseModel products = parseJsonResponseToObject(jsonResponse);
                        Log.d("Nutrition", "Json erfolgreich in Objekt umgewandelt");
                        productList = extractProductDetailsFromApiResponse(products);

                        //nur zum debuggen, muss später raus
                        for (OpenFoodFactsResponseModel.Product product : productList) {
                            Log.d("Nutrition", "Name: " + product.product_name + "Code: " + product.code + "Bild: " + product.image_url);
                        }

                        mainHandler.post(() -> callback.onSuccess(productList));
                    }
                } catch(Exception e) {
                    Log.e("Nutrition", "Fehler beim API-Call", e);
                    mainHandler.post(() -> callback.onFailure(e));
                }
        }); executor.shutdown();
    }

    /**
     * extrahiert die Informationen aus der API-Antwort, welche benötigt werden
     * @param apiResponse API-Antwort als Objekt mit allen Informationen
     * @return Liste der Informationen die wirklich benötigt werden
     */
    private static List<OpenFoodFactsResponseModel.Product> extractProductDetailsFromApiResponse(OpenFoodFactsResponseModel apiResponse) {
        List<OpenFoodFactsResponseModel.Product> productList = new ArrayList<>();
        for (OpenFoodFactsResponseModel.Product product : apiResponse.products) {
            if (!product.product_name.isEmpty() && !product.code.isEmpty()) {
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * Ausführung des API-Aufrufs
     * @param apiUrl URL zum API-Endpunkt
     * @return gibt Antwort der API als String zurück
     * @throws Exception
     */
    private static String apiCall(String apiUrl) throws Exception {
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

    /**
     * Übersetzt den Antwort-String in ein Objekt
     * @param jsonResponse API-Antwort als String
     * @return Objekt mit API-Antwort
     * @throws Exception
     */
    private static OpenFoodFactsResponseModel parseJsonResponseToObject(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, OpenFoodFactsResponseModel.class);
    }
}
