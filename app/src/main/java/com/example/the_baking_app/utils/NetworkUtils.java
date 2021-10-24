package com.example.the_baking_app.utils;

import android.net.Uri;
import android.util.Log;

import com.example.the_baking_app.models.CookingStep;
import com.example.the_baking_app.models.Ingredient;
import com.example.the_baking_app.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    public static final String recipeURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    // build url to fetch movies filtered by category
    public static URL buildUrl() {

        Uri.Builder builder = new Uri.Builder();
        Uri builtUri = builder.scheme("https")
                .authority("d17h27t6h515a5.cloudfront.net")
                .appendPath("topher")
                .appendPath("2017")
                .appendPath("May")
                .appendPath("59121517_baking")
                .appendPath("baking.json")
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, "buildUrl: " + builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    // Return http response as string  - no parsing here
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String response;
        BufferedReader reader = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return  null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            response = buffer.toString();
//            Log.d(TAG, "getResponseFromHttpUrl: " + response);
            return response;

        } catch (Exception e) {
//            continue;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return null;
    }


    public static List<Recipe> fetchRecipes() throws JSONException, IOException {
        // initialize recipes list
        List<Recipe> recipes= new ArrayList<Recipe>();

        // build url and send http request for recipe - returns response body
        URL url = buildUrl();
        String response = getResponseFromHttpUrl(url);

        // response body is array of objects
        JSONArray array = new JSONArray(response);

        // build List<Recipe> parcelable object
        // each object is key: value of id(int), name(string), ingredients(array), steps(array), servings(int), image(string)
        for (int i=0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            int recipeId = obj.getInt("id");
            String name = obj.getString("name");
            Log.d(TAG, "fetchRecipes: " + name);

            JSONArray ingredientsArray = obj.getJSONArray("ingredients");
            // each object in ingredients array is key: value of quantity(int), measure(String), ingredient(String)
            List<Ingredient> ingredients = new ArrayList<Ingredient>();
            for (int j=0; j < ingredientsArray.length(); j++) {
                JSONObject ingredientObj = ingredientsArray.getJSONObject(j);

                int quantity = ingredientObj.getInt("quantity");
                String measure = ingredientObj.getString("measure");
                String ingredient = ingredientObj.getString("ingredient");
                ingredients.add(new Ingredient(
                        recipeId,
                        name,
                        j,
                        quantity,
                        measure,
                        ingredient
                ));
            }

            JSONArray stepsArray = obj.getJSONArray("steps");
            // each object in steps array is key: value of id(int), shortDescription(String), description(String), videoURL (String), thumbnailURL(String)
            List<CookingStep> steps = new ArrayList<CookingStep>();
            for (int k=0; k < stepsArray.length(); k++) {
                JSONObject stepObject = stepsArray.getJSONObject(k);
                int stepId = stepObject.getInt("id");
                String shortDescription = stepObject.getString("shortDescription");
                String description = stepObject.getString("description");
                String videoURL = stepObject.getString("videoURL");
                String thumbnailURL = stepObject.getString("thumbnailURL");
                Log.d(TAG, "fetchRecipes: " + shortDescription + description + videoURL + thumbnailURL);
                steps.add(new CookingStep(
                        stepId,
                        shortDescription,
                        description,
                        videoURL,
                        thumbnailURL
                ));
            }

            int servings = obj.getInt("servings");
            String image = obj.getString("image");

            // add parcelable Recipe to List<Recipe>
            recipes.add(new Recipe(
                    recipeId,
                    name,
                    ingredients,
                    steps,
                    servings,
                    image
            ));
        }


        return recipes;
    }
}

