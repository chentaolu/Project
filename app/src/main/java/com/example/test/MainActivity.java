package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Client client;
    private Button button;
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
        Thread connect = new Thread(runnable);
        connect.start();

        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity2.class)));

        //
    }

    public Client getClient() {
        return this.client;
    }
}