package com.example.test;

import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private TextView mTextView;
    //private Client client = MainActivity.getClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mTextView = (TextView) findViewById(R.id.text);

    }
}