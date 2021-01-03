package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Client c = searchRecipes.c;
    private EditText idText;
    private EditText accountText;
    private EditText passwordText;
    private Runnable sendRegister = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "register",
                    idText.getText().toString().trim(),
                    accountText.getText().toString().trim(),
                    passwordText.getText().toString().trim());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        idText = ((EditText) findViewById(R.id.nickname));
        accountText = ((EditText) findViewById(R.id.username));
        passwordText = ((EditText) findViewById(R.id.password));
        ((Button) findViewById(R.id.registerButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.registerButton:
                if (checkIdLength(idText.getText().toString().trim())) {
                    if (checkAccount(accountText.getText().toString().trim())) {
                        if (checkAccount(passwordText.getText().toString().trim())) {
                            Thread sendRegisterThread = new Thread(sendRegister);
                            sendRegisterThread.start();
                            intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "Password勿留空白",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Email勿留空白",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "id請輸入11個字元以內",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkIdLength(String id) {
        boolean check = false;
        if (id.length() <= 11 && id.length() > 0) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    private boolean checkPassWord(String password) {
        boolean check = false;
        if (password.length() > 0) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    private  boolean checkAccount(String account) {
        boolean check = false;
        if (account.length() > 0) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }
}