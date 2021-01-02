package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((Button) findViewById(R.id.registerButton)).setOnClickListener(this);

    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.registerButton:
                intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
        }
    }
}