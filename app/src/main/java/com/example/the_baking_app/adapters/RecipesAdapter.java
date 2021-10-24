package com.example.the_baking_app.adapters;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.the_baking_app.BakingIngredientsWidget;
import com.example.the_baking_app.BuildConfig;
import com.example.the_baking_app.DetailActivity;
import com.example.the_baking_app.MainActivity;
import com.example.the_baking_app.R;
import com.example.the_baking_app.models.Ingredient;
import com.example.the_baking_app.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
        public static String PREFERENCES_ID = "recipe ID";
        public static String PREFERENCES_WIDGET_TITLE = "recipe title";
        public static String PREFERENCES_WIDGET_CONTENT = "recipe ingredients";

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mRecipeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mRecipeName = itemView.findViewById(R.id.tv_recipe_name);
        }


    }

    private Context context;
    private List<Recipe> recipeList;
    private SharedPreferences sharedPreferences;

    public RecipesAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        if (recipeList != null) {
            Log.d(TAG, "RecipesAdapter: " + recipeList.size());
        }
    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        sharedPreferences = context.getSharedPreferences (BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        Log.d(TAG, "onCreateViewHolder: " + recipeView.toString()   );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (recipeList != null) {
            final Recipe recipe = recipeList.get(position);
            holder.mRecipeName.setText(recipe.getName() + " (" + recipe.getServings() + " servings)"); //recipe.getName()


            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // update ingredients and steps fragment
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putParcelableArrayListExtra(MainActivity.EXTRA_STEPS, new ArrayList(recipe.getSteps()));
                    intent.putParcelableArrayListExtra(MainActivity.EXTRA_INGREDIENTS, new ArrayList(recipe.getIngredients()));
                    intent.putExtra(DetailActivity.EXTRA_RECIPE, recipe.getName());
                    context.startActivity(intent);


                        int id = recipeList.get(position).getId();

                            boolean isRecipeInWidget = (sharedPreferences.getInt(PREFERENCES_ID, -1) == recipe.getId());

                            // If recipe already in widget, remove it
                            if (isRecipeInWidget){
                                sharedPreferences.edit()
                                        .remove(PREFERENCES_ID)
                                        .remove(PREFERENCES_WIDGET_TITLE)
                                        .remove(PREFERENCES_WIDGET_CONTENT)
                                        .apply();

                            }
                            // if recipe not in widget, then add it
                            else{
                                sharedPreferences
                                        .edit()
                                        .putInt(PREFERENCES_ID, recipe.getId())
                                        .putString(PREFERENCES_WIDGET_TITLE, recipe.getName())
                                        .putString(PREFERENCES_WIDGET_CONTENT, ingredientsString(position))
                                        .apply();

                            }

                            // Put changes on the Widget
                            ComponentName provider = new ComponentName(context, BakingIngredientsWidget.class);
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                            int[] ids = appWidgetManager.getAppWidgetIds(provider);
                            BakingIngredientsWidget bakingWidget = new BakingIngredientsWidget();
                            bakingWidget.onUpdate(context, appWidgetManager, ids);
                        }




            });
        }


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * Combine dosage and ingredient name to produce a complete line of text to use at widget
     * @return
     */
    private String ingredientsString(int pos){
        StringBuilder result = new StringBuilder();
        for (Ingredient ingredient :  recipeList.get(pos).getIngredients()){
            result.append(ingredient.quantity).append(" ").append(ingredient.measure).append(" ").append(ingredient.getIngredient()).append("\n");
        }
        return result.toString();
    }

}
