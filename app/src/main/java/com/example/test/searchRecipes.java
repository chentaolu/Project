package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.ui.login.LoginActivity;

public class searchRecipes extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        ((Button) findViewById(R.id.searchNutrientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchVieoButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchIngredientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.loginButton)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.searchNutrientsButton:
                intent = new Intent(searchRecipes.this, searchRecipesByNutrients.class);
                startActivity(intent);
                break;
            case R.id.searchVieoButton:
                intent = new Intent(searchRecipes.this, searchFoodVideo.class);
                startActivity(intent);
                break;
            case R.id.searchIngredientsButton:
                intent = new Intent(searchRecipes.this, searchRecipesByIngredients.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                intent = new Intent(searchRecipes.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.searchFood:

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}