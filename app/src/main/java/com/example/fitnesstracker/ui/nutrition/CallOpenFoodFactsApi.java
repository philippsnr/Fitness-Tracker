package com.example.fitnesstracker.ui.nutrition;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CallOpenFoodFactsApi {

    private String basicUrl = "https://world.openfoodfacts.org/cgi/search.pl?";
    public void callOpenFoodFactsApiForNutritionNames(EditText nutritionName) {
        String apiUrlToAdd = "search_terms=" + nutritionName + "&search_simple=1&action=process&json=1&fields=product_name,image_url,code";
        String apiUrl = basicUrl + apiUrlToAdd;

        new AsyncApiCall().execute(apiUrl);
    }

    private class AsyncApiCall extends AsyncTask<String, Void, OpenFoodFactsResponseModel> {

        protected OpenFoodFactsResponseModel doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            OpenFoodFactsResponseModel responseObject = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String jsonResponse = stringBuilder.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                responseObject = objectMapper.readValue(jsonResponse, OpenFoodFactsResponseModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return responseObject;
        }

        protected void onPostExecute(OpenFoodFactsResponseModel responseObject) {
            for (OpenFoodFactsResponseModel.Product product : responseObject.products) {
                Log.d("API", "Name: " + product.product_name + ", " + "Code: " + product.code + ", " + "Bild: " + product.image_url);
            }
        }
    }
}
