package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.*;
import java.util.*;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Button btnSubmit;
    private EditText ingInput, numMissingIngredient;
    private ArrayList<String> processedInput = new ArrayList<String>();
    private int missIng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = findViewById(R.id.btnSubmit);
        ingInput = findViewById(R.id.ingInput);
        numMissingIngredient = findViewById(R.id.missingIngredient);

        //ingInput.setText("tomato");
        //numMissingIngredient.setText("2");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = ingInput.getText().toString();
                while (input.length() > 0) {
                    if (input.indexOf(",")>0) {
                        String ingredient = input.substring(0, input.indexOf(","));
                        processedInput.add(ingredient);
                        input = input.substring(input.indexOf(",") + 1, input.length());
                    } else {
                        processedInput.add(input);
                        break;
                    }
                }
                missIng = Integer.valueOf(numMissingIngredient.getText().toString());

                launchActivity();
            }
        });

    }

    private void launchActivity(){
        Intent intent = new Intent(this, PossibleRecipeList.class);
        Bundle bundle = new Bundle();
        bundle.putInt("missingIngredient", missIng);
        bundle.putStringArrayList("ingredients", processedInput);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}