package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.test.ui.login.LoginActivity;

public class searchRecipes extends AppCompatActivity implements View.OnClickListener {

    static public Client c;
    private EditText SearchFood;

    /*private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            c = new Client("10.0.2.2", 5678);
        }
    };*/

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        ((Button) findViewById(R.id.searchNutrientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchVieoButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchIngredientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.loginButton)).setOnClickListener(this);
        SearchFood = (EditText) findViewById(R.id.searchFood);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.searchNutrientsButton:
                intent = new Intent(searchRecipes.this, searchRecipesByNutrients.class);
                c.SendMessage("searchNutrient", "");
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
                //c.SendMessage("searchFood", SearchFood.getText().toString().trim());
                intent = new Intent(searchRecipes.this, recipesResult.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}