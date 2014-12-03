package com.whiteout.pantrytracker.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.whiteout.pantrytracker.activities.MainActivity;
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
public class RecipeFragment extends ListFragment {

    PantryDataSource dataSource;
    List<RecipeSearch> searches;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataSource = ((MainActivity)getActivity()).getDataSource();
        new FetchRecipes().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
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
            List<String> vals = new ArrayList<String>();
            for (RecipeSearch r : recipeSearches) {
                vals.add(r.getRecipeName());
            }
            Log.d("Kenny", "Setting view");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, vals);
            setListAdapter(adapter);
        }
    }
}
