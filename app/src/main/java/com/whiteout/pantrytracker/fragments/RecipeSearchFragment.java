package com.whiteout.pantrytracker.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.internal.ca;
import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.activities.MainActivity;
import com.whiteout.pantrytracker.activities.RecipeActivity;
import com.whiteout.pantrytracker.data.PantryDataSource;
import com.whiteout.pantrytracker.data.model.Item;
import com.whiteout.pantrytracker.data.model.Recipe;
import com.whiteout.pantrytracker.data.model.RecipeSearch;
import com.whiteout.pantrytracker.data.web.interfaces.YummlyRecipeSearchRetriever;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/14
 * Email:   kdecline@gmail.com
 */
public class RecipeSearchFragment extends ListFragment {
    public static final String EXTRA_RATING      = "extra-rating";
    public static final String EXTRA_ID          = "extra-id";
    public static final String EXTRA_RECIPE_NAME = "extra-recipe-name";

    PantryDataSource dataSource;
    List<RecipeSearch> searches;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataSource = ((MainActivity)getActivity()).getDataSource();
        new FetchRecipes().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RecipeSearch searchItem = searches.get(position);

        Intent intent  = new Intent(getActivity(), RecipeActivity.class);
        intent.putExtra(EXTRA_RATING, searchItem.getRating());
        intent.putExtra(EXTRA_ID, searchItem.getId());
        intent.putExtra(EXTRA_RECIPE_NAME, searchItem.getRecipeName());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                setListAdapter(null);
                new FetchRecipes().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class FetchRecipes extends AsyncTask<Void, Void, List<RecipeSearch>> {
        @Override
        protected List<RecipeSearch> doInBackground(Void... params) {
            List<Item> items = null;
            List<RecipeSearch> recipes = null;
            try {
                dataSource.open();
                Log.wtf("Kenny", "Opened datasource");
                items = dataSource.getAllItems();
                String[] itemNames = new String[items.size()];
                for (int i=0; i<items.size(); i++) {
                    itemNames[i] = items.get(i).getName();
                }
                Log.wtf("Kenny", "Querying Yummly API");
                YummlyRecipeSearchRetriever ret = new YummlyRecipeSearchRetriever();
                if (itemNames.length == 0) {
                    recipes = ret.fetchRecipes(new String[] {"apple"});
                } else {
                    recipes = ret.fetchRecipes(itemNames);
                }

            } catch (SQLException e) {
                Log.d("Kenny", "error loading items");
            } finally {
                dataSource.close();
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(List<RecipeSearch> recipeSearches) {
            searches = recipeSearches;
            try {
                List<String> vals = new ArrayList<String>();
                for (RecipeSearch r : recipeSearches) {
                    vals.add(r.getRecipeName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, vals);
                setListAdapter(adapter);
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            Log.d("Kenny", "Setting view");


        }
    }
}
