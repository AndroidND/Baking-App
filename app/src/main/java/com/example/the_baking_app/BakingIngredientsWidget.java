package com.example.the_baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

import static com.example.the_baking_app.adapters.RecipesAdapter.PREFERENCES_WIDGET_CONTENT;
import static com.example.the_baking_app.adapters.RecipesAdapter.PREFERENCES_WIDGET_TITLE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingIngredientsWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile =
            "com.example.android.the_baking_app";
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_ingredients_widget);
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        views.setTextViewText(R.id.widget_recipe_title, sharedPreferences.getString(PREFERENCES_WIDGET_TITLE, ""));
        views.setTextViewText(R.id.widget_recipe_ingredients, sharedPreferences.getString(PREFERENCES_WIDGET_CONTENT, ""));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

