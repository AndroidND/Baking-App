package com.example.the_baking_app.utils;

import com.example.the_baking_app.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonRecipeApi {
//http://go.udacity.com/android-baking-app-json
    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
