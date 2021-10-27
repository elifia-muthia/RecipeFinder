package com.example.recipefinder;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PossibleRecipeList extends AppCompatActivity {
    private Button button;
    ArrayList<Recipe> recipes;
    ArrayList<String> ingredients;
    int missingIngredient;
    ListView listView;

    public PossibleRecipeList() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recipe_possible);
        listView = (ListView) findViewById(R.id.listView);

        Bundle getData = getIntent().getExtras();
        ingredients = getData.getStringArrayList("ingredients");
        missingIngredient = getData.getInt("missingIngredient");

        button = findViewById(R.id.btnBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButton();
            }
        });

        recipes = new ArrayList<Recipe>();
        String urlString = "https://api.spoonacular.com/recipes/findByIngredients?ranking=2&ingredients=";

        for (int i = 0; i < ingredients.size(); i++) {
            if (i == 0) {
                urlString += ingredients.get(i);
            } else {
                urlString += ",+" + ingredients.get(i);
            }
        }

        urlString += "&apiKey=fbbc114865dd45059d934ff40ab4d5ba";
        new fetchData().execute(urlString);



    }

    private void backButton(){
        MainActivity mainCall = new MainActivity();
        Intent intent = new Intent(this, mainCall.getClass());
        startActivity(intent);


    }

    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            recipes.clear();
            String finalResult = null;
            try {
                URL url = new URL(params[0]);
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
                finalResult = result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                finalResult = "error";
            }
            return finalResult;
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONArray rawAPIResponse = new JSONArray(s);

                for (int i = 0; i < rawAPIResponse.length(); i++) {
                    JSONObject response = rawAPIResponse.getJSONObject(i);
                    int count = response.getInt("missedIngredientCount");
                    if (count <= missingIngredient) {
                        Recipe recipe = new Recipe(response.getString("title"), response.getString("image"), response.getInt("id"), count);
                        recipes.add(recipe);
                    }
                }

                RecipeConnector adapter = new RecipeConnector(PossibleRecipeList.this, recipes);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), RecipeInfo.class);
                    intent.putExtra("id", recipes.get(i).getId());
                    startActivity(intent);
                }
            });
        }
    }
}
