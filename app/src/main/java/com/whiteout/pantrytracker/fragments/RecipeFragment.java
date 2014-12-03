package com.whiteout.pantrytracker.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.data.model.Recipe;
import com.whiteout.pantrytracker.data.web.interfaces.YummlyRecipeRetriever;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/14
 * Email:   kdecline@gmail.com
 */
public class RecipeFragment extends Fragment {

    ImageView mImageView;
    TextView  titleName;
    TextView  cookTime;
    TextView  yield;
    TextView  description;
    ImageView logoView;

    String title;
    String cookTimeString;
    String yieldString;
    String descrptionString;
    Float  rating;
    String yummlyId;

    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        mImageView = (ImageView) view.findViewById(R.id.largeImage);
        titleName  = (TextView)  view.findViewById(R.id.name);
        cookTime   = (TextView)  view.findViewById(R.id.cookTime);
        yield      = (TextView)  view.findViewById(R.id.yield);
        description= (TextView)  view.findViewById(R.id.description);
        logoView   = (ImageView) view.findViewById(R.id.logoView);

        Intent mIntent = getActivity().getIntent();
        title = mIntent.getStringExtra(RecipeSearchFragment.EXTRA_RECIPE_NAME);
        yummlyId = mIntent.getStringExtra(RecipeSearchFragment.EXTRA_ID);
        rating = mIntent.getFloatExtra(RecipeSearchFragment.EXTRA_RATING, -1);

        if (title != null) {
            titleName.setText(title);
        }

        new RecipeFetcher().execute(yummlyId);

        return view;
    }

    void loadRecipe(Recipe recipe) {
        if (recipe.getCookTime() != null)
            cookTime.setText(recipe.getCookTime());
        if (recipe.getYield() != null)
            yield.setText(recipe.getYield());
        description.setText(recipe.getDirections());
        ImageLoader.getInstance().displayImage(recipe.getFoodDownloadURL(), mImageView, options, null);
        ImageLoader.getInstance().displayImage(recipe.getYummlyLogo(), logoView, options, null);
    }

    private class RecipeFetcher extends AsyncTask<String, Void, Recipe> {
        @Override
        protected Recipe doInBackground(String... params) {
            Recipe recipe = null;
            YummlyRecipeRetriever yrr = new YummlyRecipeRetriever();
            Log.d("Kenny", "Fetching " + params[0]);
            return yrr.fetchRecipes(params[0]);
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            super.onPostExecute(recipe);
            loadRecipe(recipe);
        }
    }
}
