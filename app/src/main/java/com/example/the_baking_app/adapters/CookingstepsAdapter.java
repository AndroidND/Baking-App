package com.example.the_baking_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.the_baking_app.CookingStepDetailFragment;
import com.example.the_baking_app.CookingstepActivity;
import com.example.the_baking_app.DetailActivity;
import com.example.the_baking_app.MainActivity;
import com.example.the_baking_app.R;
import com.example.the_baking_app.models.CookingStep;
import com.example.the_baking_app.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CookingstepsAdapter extends RecyclerView.Adapter<CookingstepsAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCookingstepShortDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mCookingstepShortDescription = itemView.findViewById(R.id.tv_cookingstep_short_description);
        }

    }

    private Context context;
    private List<CookingStep> cookingstepsList;
    private String recipe;
    private boolean isTwoPane;
    private View previousItemView = null;

    private int selectedPos = RecyclerView.NO_POSITION;


    public CookingstepsAdapter(Context context, List<CookingStep> cookingstepsList, boolean isTwoPane,
        String recipe) {
        this.context = context;
        this.cookingstepsList = cookingstepsList;
        this.recipe = recipe;
        this.isTwoPane = isTwoPane;
        if (cookingstepsList != null) {
            Log.d(TAG, "CookingstepsAdapter: " + cookingstepsList.size());
        }
    }

    @NonNull
    @Override
    public CookingstepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View recipeView = inflater.inflate(R.layout.cookingstep_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        Log.d(TAG, "onCreateViewHolder: " + recipeView.toString()   );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CookingstepsAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + cookingstepsList.get(position).getShortDescription());
        if (cookingstepsList != null) {
            final CookingStep cookingStep = cookingstepsList.get(position);
            holder.mCookingstepShortDescription.setText(cookingStep.getShortDescription());


            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // update ingredients and steps fragment
                    if (isTwoPane) {
                        // add fragment
                        CookingStepDetailFragment fragment = CookingStepDetailFragment.newInstance(cookingStep);
                        ((FragmentActivity) view.getContext())
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.cookingsteps_container, fragment)
                                .addToBackStack(null)
                                .commit();

                        if(previousItemView == null){
                            view.setBackgroundColor(Color.parseColor("#d3d3d3"));
                            // remove highlight from previous clicked item
                            previousItemView = view;
                        } else if (view != previousItemView) {
                            // current clicked item highlighted
                            view.setBackgroundColor(Color.parseColor("#d3d3d3"));
                            // remove highlight from previous clicked item
                            previousItemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            previousItemView = view;
                        }

                    } else {
                        Intent intent = new Intent(context, CookingstepActivity.class);
                        Log.d(TAG, "onClick: " + cookingstepsList.get(position).getDescription());
                        intent.putParcelableArrayListExtra(MainActivity.EXTRA_STEPS, new ArrayList<>(cookingstepsList));
                        intent.putExtra(DetailActivity.EXTRA_STEP_INDEX, position);
                        intent.putExtra(DetailActivity.EXTRA_RECIPE, recipe);
                        Log.d(TAG, "onClick: " + position);
                        context.startActivity(intent);
                    }


                }

            });
        }


    }

    @Override
    public int getItemCount() {
        return cookingstepsList == null ? 0 : cookingstepsList.size();
    }

}
