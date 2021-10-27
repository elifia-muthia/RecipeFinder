package com.example.recipefinder;

import android.os.AsyncTask;
import android.content.*;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class connAsyncTask extends AsyncTask<String, Integer, ArrayList<Recipe>> {

    private int missingIngredients;
    private RecyclerView.Adapter rvAdapter;
    private Context context;

    public connAsyncTask(int ingCount, Context context) {
        missingIngredients = ingCount;
        this.context = context;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        ArrayList<Recipe> filteredResponse = new ArrayList<Recipe>();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder result = new StringBuilder();
            String line;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                while ((line = in.readLine()) != null) {
                    result.append(line);
                    result.append(System.lineSeparator());
                }
            }
            conn.disconnect();
            JSONArray rawAPIResponse = new JSONArray(result);
            for (int i = 0; i < rawAPIResponse.length(); i++) {
                JSONObject response = rawAPIResponse.getJSONObject(i);
                int count = response.getInt("missedIngredientCount");
                if (count <= missingIngredients) {
                    Recipe recipe = new Recipe(response.getString("title"), response.getString("image"), response.getInt("id"), count);
                    filteredResponse.add(recipe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredResponse;
    }

    /*@Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
        rvAdapter.notifyDataSetChanged();
    }*/
}
