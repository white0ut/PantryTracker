package com.whiteout.pantrytracker.data.web.interfaces;

import android.net.Uri;
import android.util.Log;

import com.whiteout.pantrytracker.data.model.RecipeSearch;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/14
 * Email:   kdecline@gmail.com
 */
public class YummlyRecipeSearchRetriever {
    private static final String ENDPOINT = "http://api.yummly.com/v1/api/recipes?_app_id=9cc50088&_app_key=347d218bb9d46fe84a9c904a4874d364&q=";
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

    public List<RecipeSearch> fetchRecipes(String[] searchItems) {
        List<RecipeSearch> searches = new ArrayList<RecipeSearch>();

        try {
            String url = ENDPOINT;
            for (String item : searchItems) {
                url += item + "+";
            }
            String jsonString = getUrl(url);


        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return searches;
    }
}
