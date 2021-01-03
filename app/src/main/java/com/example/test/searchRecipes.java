package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class searchRecipes extends AppCompatActivity implements View.OnClickListener{

    public static Client c;
    private EditText SearchFood;
    private EditText SearchByIngredients;
    private EditText SearchFoodVideo;
    private Button LoginButton;
    private String id;
    private static boolean firstCreate = true;
    public static Boolean Login = false;
    private Runnable connectToServerThread = new Runnable() {
        @Override
        public void run() {
            c = new Client("192.168.11.117", 4567);
        }
    };
    private Runnable sendRecipeMessageThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c,"searchRecipe", SearchFood.getText().toString().trim());
        }
    };
    private Runnable searchIngredientsThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchByIngredients", SearchByIngredients.getText().toString().trim());
        }
    };
    private Runnable searchVideoThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchVideo", SearchFoodVideo.getText().toString().trim());
        }
    };

    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        ((Button) findViewById(R.id.searchNutrientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchVieoButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchIngredientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchRecipeButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchRandomButton)).setOnClickListener(this);
        SearchFood = (EditText) findViewById(R.id.searchFood);
        SearchByIngredients = (EditText) findViewById(R.id.searchByIngredients);
        SearchFoodVideo = (EditText) findViewById(R.id.searchVideo);
        if (firstCreate == true) {
            Thread connect = new Thread(connectToServerThread);
            connect.start();
            firstCreate = false;
        } else {
            firstCreate = false;
        }
        LinearLayout relative = (LinearLayout) findViewById(R.id.loginLayout);
        if (!Login) {
            LoginButton = new Button(this);
            LoginButton.setId(25);                      //25 = login id
            LoginButton.setOnClickListener(this);
            LoginButton.setText(R.string.action_sign_in);
            LoginButton.setTextColor(getResources().getColorStateList(R.color.white));
            LoginButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
            relative.addView(LoginButton);
        } else {
            TextView loginId = new TextView(this);
            loginId.setId(26);
            loginId.setText("");
        }

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
                try {
                    Thread sendSearchVideo = new Thread (searchVideoThread);
                    sendSearchVideo.start();
                    SearchFoodVideo.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchFoodVideo.class);
                startActivity(intent);
                break;
            case R.id.searchIngredientsButton:
                try {
                    Thread sendSearchIngredients = new Thread (searchIngredientsThread);
                    sendSearchIngredients.start();
                    SearchByIngredients.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchRecipesByIngredients.class);
                startActivity(intent);
                break;
            case R.id.searchRecipeButton:
                try {
                    Thread sendSearchRecipe = new Thread(sendRecipeMessageThread);
                    sendSearchRecipe.start();
                    SearchFood.setText(null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchRecipesResult.class);
                startActivity(intent);
                break;
            case 25:
                intent = new Intent(searchRecipes.this, Login.class);
                startActivity(intent);
                break;
            case R.id.searchRandomButton:
                intent = new Intent(searchRecipes.this, singleRecipe.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}