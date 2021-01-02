package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Client c = searchRecipes.c;
    private EditText account;
    private EditText password;
    private String accountText = "";
    private String passwordText = "";
    private Runnable loginThread = new Runnable() {
        @Override
        public void run() {
            c.sendLoginInformation(c, "login", accountText, passwordText);
        }
    };


    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((Button) findViewById(R.id.login)).setOnClickListener(this);
        ((Button) findViewById(R.id.registerButton)).setOnClickListener(this);
        account = ((EditText) findViewById(R.id.username));
        password = ((EditText) findViewById(R.id.password));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.login:
                accountText = account.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                try {
                    Thread loginInServer = new Thread(loginThread);
                    loginInServer.start();
                    account.setText(null);
                    password.setText(null);
                    //-----------------------------------------------------------------------
                    searchRecipes.Login = true;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                intent = new Intent(Login.this, searchRecipes.class);
                startActivity(intent);
                break;
            case R.id.registerButton:
                intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}