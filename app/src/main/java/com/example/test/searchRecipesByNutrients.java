package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner spinner;
    Client c = searchRecipes.c;
    final String[] chooseNumber = {"10", "20", "30", "40", "50"};

    private Runnable sendSearchRecipesByNutrientsThread = new Runnable() {
        @Override
        public void run() {
            c.SendSearchRecipesByNutrients(c, "SendSearchRecipesByNutrients",
                    spinner.getSelectedItem().toString().trim(), carbsMin.getText().toString().trim(),
                    carbsMax.getText().toString().trim(), caloriesMin.getText().toString().trim(),
                    caloriesMax.getText().toString().trim(), proteinMin.getText().toString().trim(),
                    proteinMax.getText().toString().trim(), fatMin.getText().toString().trim(),
                    fatMax.getText().toString().trim(), vitaminCMin.getText().toString().trim(),
                    vitaminCMax.getText().toString().trim(), fiberMin.getText().toString().trim(),
                    fiberMax.getText().toString().trim(), sugarMin.getText().toString().trim(),
                    sugarMax.getText().toString().trim(), ironMin.getText().toString().trim(),
                    ironMax.getText().toString().trim());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_by_nutrients);
        spinner = (Spinner)findViewById(R.id.spinner);
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


        ArrayAdapter<String> lunchList = new ArrayAdapter<>(searchRecipesByNutrients.this,
                android.R.layout.simple_spinner_dropdown_item,chooseNumber);
        spinner.setAdapter(lunchList);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.searchButton:
                Thread sendSearchRecipesByNutrients = new Thread(sendSearchRecipesByNutrientsThread);
                sendSearchRecipesByNutrients.start();
                carbsMin.setText(null);
                carbsMax.setText(null);
                caloriesMin.setText(null);
                caloriesMax.setText(null);
                proteinMin.setText(null);
                proteinMax.setText(null);
                fatMin.setText(null);
                fatMax.setText(null);
                vitaminCMin.setText(null);
                vitaminCMax.setText(null);
                fiberMin.setText(null);
                fiberMax.setText(null);
                sugarMin.setText(null);
                sugarMax.setText(null);
                ironMin.setText(null);
                ironMax.setText(null);
                intent = new Intent(searchRecipesByNutrients.this, recipesResult.class);
                startActivity(intent);
                break;
        }
    }
}