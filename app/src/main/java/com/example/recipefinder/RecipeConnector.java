package com.example.recipefinder;


import android.content.Context;
import android.net.Uri;
import android.view.*;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TextView;
import java.util.ArrayList;


public class RecipeConnector extends BaseAdapter{

    private ArrayList<Recipe> recipes;
    private Context context;
    private TextView name;
    private TextView missingIngredients;
    private ImageView recipePic;

    public RecipeConnector(Context context, ArrayList<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public  View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.connector_recipe, parent, false);
        }

        name = (TextView) convertView.findViewById(R.id.name);
        missingIngredients = (TextView) convertView.findViewById(R.id.missingIngredient);
        recipePic = (ImageView) convertView.findViewById(R.id.recipePic);
        name.setText(recipes.get(position).getName());
        String text = "Number of Missing Ingredients: " + String.valueOf(recipes.get(position).getMissingIngredients());
        missingIngredients.setText(text);
        Uri imageLink = Uri.parse(recipes.get(position).getUrl());
        recipePic.setImageURI(imageLink);

        return convertView;
    }
}
