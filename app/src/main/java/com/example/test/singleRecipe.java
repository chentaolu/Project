package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class singleRecipe extends AppCompatActivity implements View.OnClickListener {

    EditText commandInput;
    Client c = searchRecipes.c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        ((Button) findViewById(R.id.sendCommandButton)).setOnClickListener(this);
        commandInput = ((EditText) findViewById(R.id.commandText));
    }

    @Override
    public void onClick(View v) {
        int clickNumber = v.getId();
        String sendMessage = "";
        if (clickNumber == R.id.sendCommandButton && c.LoginOrNot == false) {
            Toast.makeText(singleRecipe.this, "你尚未登入",Toast.LENGTH_SHORT).show();
        } else if (clickNumber == R.id.sendCommandButton && c.LoginOrNot == true) {
            sendMessage = commandInput.getText().toString().trim();
        }
    }
}