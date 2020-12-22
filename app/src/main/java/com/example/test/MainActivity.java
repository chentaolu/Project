package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Client client;
    private Button button;
    private Button buttonLogin;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            client = new Client("10.0.2.2", 5678);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        buttonLogin = (Button)findViewById(R.id.loginButton);
        Thread connect = new Thread(runnable);
        connect.start();

        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity2.class)));
        buttonLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        //
    }

    public Client getClient() {
        return this.client;
    }
}