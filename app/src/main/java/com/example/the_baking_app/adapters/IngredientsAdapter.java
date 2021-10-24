package com.example.the_baking_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.the_baking_app.R;
import com.example.the_baking_app.models.Ingredient;

import java.util.List;

public class IngredientsAdapter extends BaseAdapter {
    Context mContext;
    List<Ingredient> mIngredients;

    public IngredientsAdapter(Context mContext, List<Ingredient> ingredients) {
        this.mContext = mContext;
        this.mIngredients = ingredients;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate(R.layout.baking_ingredients_widget, parent, false);
        }
        return null;
    }
}
