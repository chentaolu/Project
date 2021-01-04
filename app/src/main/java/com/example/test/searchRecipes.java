package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class searchRecipes extends AppCompatActivity implements View.OnClickListener{

    public static Client c;
    private EditText SearchFood;
    private EditText SearchByIngredients;
    private EditText SearchFoodVideo;
    private Button LoginButton;
    private String id;
    private Spinner spinner;
    private static boolean firstCreate = true;
    final String[] chooseNumber = {"10", "20", "30", "40", "50"};
    public static Boolean Login = false;
    private Runnable connectToServerThread = new Runnable() {
        @Override
        public void run() {
            c = new Client("4.tcp.ngrok.io", 19636);
        }
    };
    private Runnable sendRecipeMessageThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c,"searchRecipe", spinner.getSelectedItem().toString().trim(),
                    SearchFood.getText().toString().trim());
        }
    };
    private Runnable searchIngredientsThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchByIngredients", spinner.getSelectedItem().toString().trim(),
                    SearchByIngredients.getText().toString().trim());
        }
    };
    private Runnable searchVideoThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchVideo", spinner.getSelectedItem().toString().trim(),
                    SearchFoodVideo.getText().toString().trim());
        }
    };
    private Runnable searchRandomThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchRandomRecipes", spinner.getSelectedItem().toString().trim());
        }
    };

    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        spinner = (Spinner)findViewById(R.id.spinner);
        ((Button) findViewById(R.id.searchNutrientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchVieoButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchIngredientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchRecipeButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchRandomButton)).setOnClickListener(this);
        SearchFood = (EditText) findViewById(R.id.searchFood);
        SearchByIngredients = (EditText) findViewById(R.id.searchByIngredients);
        SearchFoodVideo = (EditText) findViewById(R.id.searchVideo);

        ArrayAdapter<String> lunchList = new ArrayAdapter<>(searchRecipes.this,
                android.R.layout.simple_spinner_dropdown_item,chooseNumber);
        spinner.setAdapter(lunchList);


        if (firstCreate == true) {
            Thread connect = new Thread(connectToServerThread);
            connect.start();
            firstCreate = false;
        } else {
            firstCreate = false;
        }
        LinearLayout relative = (LinearLayout) findViewById(R.id.loginLayout);
        LoginButton = new Button(this);
        LoginButton.setId(25);                      //25 = login id
        LoginButton.setOnClickListener(this);
        LoginButton.setText(R.string.action_sign_in);
        LoginButton.setTextColor(getResources().getColorStateList(R.color.white));
        LoginButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
        relative.addView(LoginButton);
        TextView loginId = new TextView(this);
        loginId.setId(26);
        loginId.setTextSize(20);
        loginId.setText(com.example.test.Login.id);
        relative.addView(loginId);
        if (!Login) {
            relative.removeView(loginId);
        } else {
            relative.removeView(LoginButton);
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
                try {
                    Thread sendRandomRecipe = new Thread(searchRandomThread);
                    sendRandomRecipe.start();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchRandomRecipe.class);
                startActivity(intent);
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}