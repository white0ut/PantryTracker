package com.whiteout.pantrytracker.data.web.interfaces;

import android.util.Log;

import com.whiteout.pantrytracker.data.model.Recipe;
import com.whiteout.pantrytracker.data.model.RecipeSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/2014
 * Email:   kdecline@gmail.com
 */
public class YummlyRecipeRetriever {
    private static final String ENDPOINT = "http://api.yummly.com/v1/api/recipe/";
    private static final String ENDPOINT_TAG = "?_app_id=9cc50088&_app_key=347d218bb9d46fe84a9c904a4874d364";
    private static final String TAG = "Kenny";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out =new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public Recipe fetchRecipes(String recipeId) {
        Recipe recipe = null;

        try {
            String url = ENDPOINT + recipeId + ENDPOINT_TAG;
            String jsonString = getUrl(url);

            recipe = parseJSON(jsonString);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
        return recipe;
    }

    Recipe parseJSON(String jsonString) throws JSONException {
        Recipe recipe = new Recipe();
        JSONObject jsonObject = new JSONObject(jsonString);

        String lines = "";
        JSONArray ingredLines = jsonObject.getJSONArray("ingredientLines");
        for (int i=0; i < ingredLines.length(); i++) {
            lines += ingredLines.getString(i) + "\n";
        }
        recipe.setdirections(lines);
        recipe.setName(jsonObject.getString("name"));
        recipe.setYield(jsonObject.getString("yield"));
        recipe.setYummlyId(jsonObject.getString("id"));

        return recipe;
    }
}
