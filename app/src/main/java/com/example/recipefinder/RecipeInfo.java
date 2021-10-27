package com.example.recipefinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.*;

import android.widget.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;

public class RecipeInfo extends AppCompatActivity {

   // private Button backButton;
    //private ImageView imageView;

   // private NestedScrollView ingredients;
    //private NestedScrollView instructions;

    private int id;
    private String apiKey = "fbbc114865dd45059d934ff40ab4d5ba";
    private String imageUrl;

    private TextView mRecipeTitle;
    private TextView mIngList;
    private TextView mInstructList;

    public RecipeInfo() {}

    protected void DefineViews()
    {
        if (mRecipeTitle == null)
        {
            mRecipeTitle = findViewById(R.id.titleRecipe);
        }
        if (mIngList == null)
        {
            mIngList = findViewById(R.id.listOfIng);
        }
        if (mInstructList == null)
        {
            mInstructList = findViewById(R.id.listOfInstructions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_recipe);

       // backButton = findViewById(R.id.backButton);
     //   imageView = findViewById(R.id.imageView);
        DefineViews();
        id = getIntent().getExtras().getInt("id");

      /* backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });*/
        String urlString = "https://api.spoonacular.com/recipes/" + id + "/information?apiKey=" + apiKey;
        new RecipeInfo.fetchData().execute(urlString);
//        imageView.setImageURI(Uri.parse(imageUrl));
    }

    protected void updateUI(String title, String rawIng, String rawInst)
    {
        DefineViews();
        mRecipeTitle.setText(title);
        mIngList.setText(rawIng);
        mInstructList.setText(rawInst);
    }

    /*private void back(){
        MainActivity main = new MainActivity();
        Intent intent = new Intent(this, main.getClass());
        startActivity(intent);
    }*/

    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
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

        protected String ExpandJSON(JSONArray jsonArray, String[] jsonParameters)
        {
           String res = "";
           try {
               for (int i = 0; i < jsonArray.length(); i++)
               {
                   JSONObject currObject = jsonArray.getJSONObject(i);
                   for (String param : jsonParameters)
                   {
                       res += currObject.getString(param) + " ";
                   }
                   res += "\n";
               }
           } catch(Exception e) {
                res = "N/A";
           }

           return res;
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            String mRawIngridients = "";
            String mRawInstructions = "";
            String mTitle = "";
            try {

                JSONObject rawAPIResponse = new JSONObject(s);

                mTitle = rawAPIResponse.getString("title");
                try {
                    JSONArray fullIngredients = rawAPIResponse.getJSONArray("extendedIngredients");
                    JSONArray fullInstructions = rawAPIResponse.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");

                    mRawIngridients = ExpandJSON(fullIngredients, new String[]{"originalString"});
                    mRawInstructions = ExpandJSON(fullInstructions, new String[]{"number", "step"});
                }
                catch(Exception e) {
                    mRawInstructions = "N/A";
                    mRawIngridients = "N/A";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateUI(mTitle, mRawIngridients, mRawInstructions);
        }
    }

}
