package com.example.the_baking_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.the_baking_app.adapters.RecipesAdapter;
import com.example.the_baking_app.models.Recipe;
import com.example.the_baking_app.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_INGREDIENTS = "ingredients";
    public final static String EXTRA_STEPS = "steps";

    private List<Recipe> recipeList = new ArrayList<Recipe>();

    private RecyclerView recipeRecyclerView;
    private RecipesAdapter recipesAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_main) TextView tv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.AppTitle);

        recipeRecyclerView = findViewById(R.id.rv_recipe_list);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        if (width > 2048) {
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        recipesAdapter = new RecipesAdapter(this,recipeList);
        recipeRecyclerView.setAdapter(recipesAdapter);

        new FetchRecipesTask().execute();


    }


    public class FetchRecipesTask extends AsyncTask<Void, Integer, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... Void) {
            final List<Recipe> recipes;
            try {
                recipes = NetworkUtils.fetchRecipes();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {

            try {
                if (recipes != null) {
                    recipeList = recipes;
                    recipesAdapter = new RecipesAdapter(MainActivity.this,recipes);
                    recipeRecyclerView.setAdapter(recipesAdapter);

                    tv_main.setVisibility(View.GONE);

                } else {
                    tv_main.setText(R.string.recipe_fetch_error_message);

                }
            } catch (Exception e) {
                e.printStackTrace();
                tv_main.setText(R.string.recipe_fetch_error_message);
            }

        }


    }
}
