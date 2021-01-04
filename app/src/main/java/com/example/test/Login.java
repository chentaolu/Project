package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import static java.lang.Thread.sleep;

public class Login extends AppCompatActivity implements View.OnClickListener {

    public static String id = "";
    Client c = searchRecipes.c;
    private EditText account;
    private EditText password;
    private String accountText = "";
    private String passwordText = "";
    private JSONObject confirm = null;
    private Runnable loginThread = new Runnable() {
        @Override
        public void run() {
            c.sendLoginInformation(c, "login", accountText, passwordText);
        }
    };
    private Runnable confirmThread = new Runnable() {
        @Override
        public void run() {
            confirm = c.ReadMessage();
            c.readDone = true;
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
                    Thread confirmFromServer = new Thread(confirmThread);
                    confirmFromServer.start();
                    while (!c.readDone) {
                        sleep(1000);
                    };
                    c.readDone = false;
                    if (confirm.getString("status").equals("success")) {
                        searchRecipes.Login = true;
                        id = confirm.getString("id");
                    } else {
                        searchRecipes.Login = false;
                        Toast.makeText(Login.this, "錯誤的帳號或密碼",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (searchRecipes.Login == true) {
                    intent = new Intent(Login.this, searchRecipes.class);
                    startActivity(intent);
                }
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