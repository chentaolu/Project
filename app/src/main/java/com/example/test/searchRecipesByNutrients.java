package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class searchRecipesByNutrients extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private EditText carbsMin;
    private EditText carbsMax;
    private EditText caloriesMin;
    private EditText caloriesMax;
    private EditText proteinMin;
    private EditText proteinMax;
    private EditText fatMin;
    private EditText fatMax;
    private EditText vitaminCMin;
    private EditText vitaminCMax;
    private EditText fiberMin;
    private EditText fiberMax;
    private EditText sugarMin;
    private EditText sugarMax;
    private EditText ironMin;
    private EditText ironMax;
    Client c = searchRecipes.c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_by_nutrients);

        mTextView = (TextView) findViewById(R.id.text);
        carbsMin = (EditText) findViewById(R.id.carbsMin);
        carbsMax = (EditText) findViewById(R.id.carbsMax);
        caloriesMin = (EditText) findViewById(R.id.caloriesMin);
        caloriesMax = (EditText) findViewById(R.id.caloriesMax);
        proteinMin = (EditText) findViewById(R.id.proteinMin);
        proteinMax = (EditText) findViewById(R.id.proteinMax);
        fatMin = (EditText) findViewById(R.id.fatMin);
        fatMax = (EditText) findViewById(R.id.fatMax);
        vitaminCMin = (EditText) findViewById(R.id.vitaminCMin);
        vitaminCMax = (EditText) findViewById(R.id.vitaminCMax);
        fiberMin = (EditText) findViewById(R.id.fiberMin);
        fiberMax = (EditText) findViewById(R.id.fiberMax);
        sugarMin = (EditText) findViewById(R.id.sugarMin);
        sugarMax = (EditText) findViewById(R.id.sugarMax);
        ironMin = (EditText) findViewById(R.id.ironMin);
        ironMax = (EditText) findViewById(R.id.ironMax);
        ((Button) findViewById(R.id.searchButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.searchButton:
                
                intent = new Intent(searchRecipesByNutrients.this, recipesResult.class);
                startActivity(intent);
                break;
        }
    }
}