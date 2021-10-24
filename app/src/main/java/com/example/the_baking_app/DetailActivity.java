package com.example.the_baking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.the_baking_app.adapters.CookingstepsAdapter;
import com.example.the_baking_app.models.CookingStep;
import com.example.the_baking_app.models.Ingredient;
import com.example.the_baking_app.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity  {
    public static final String EXTRA_STEP_INDEX = "step-index";
    public static final String EXTRA_RECIPE = "recipe";
    private static boolean isTwoPane = false;

    @BindView(R.id.toolbar) Toolbar toolbar;
    // get a reference to the TextView in the fragment layout
    @BindView((R.id.tv_ingredients)) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        // retrieve intent to get recipe parcelable
        Intent intent = getIntent();

        // get recipe parcelable
        List<CookingStep> steps = intent.getParcelableArrayListExtra(MainActivity.EXTRA_STEPS);

        List<Ingredient> ingredients = intent.getParcelableArrayListExtra(MainActivity.EXTRA_INGREDIENTS);

        String recipe = intent.getStringExtra(DetailActivity.EXTRA_RECIPE);

        toolbar.setTitle(recipe);

        // Display ingredients

        if (findViewById(R.id.cookingsteps_container) != null) {
            isTwoPane = true;
        }

        // display all ingredients
        textView.setText("Ingredients:");
        for (Ingredient ingredient: ingredients) {
            textView.append(
                    "\n ~ "
                            + ingredient.getQuantity()
                            + " "
                            + ingredient.getMeasure().toLowerCase()
                            + " "
                            + ingredient.getIngredient()
                            + " "
            );
        }
        // get a reference to the RecyclerView in the fragment layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cookingsteps_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CookingstepsAdapter adapter = new CookingstepsAdapter(this,
                steps, isTwoPane, recipe);

        recyclerView.setAdapter(adapter);

    }



    public void onCookingStepSelected(int position) {
        // toast to check onclick functionality of cookingstep list fragment
//        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
    }



}
