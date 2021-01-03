package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class searchRecipesByIngredients extends AppCompatActivity implements View.OnClickListener {

    private int clickNumber = 0;
    private JSONObject result;
    private List<String> imageURLs = new ArrayList<String>();
    private List<String> titles = new ArrayList<String>();
    public List<String> ids = new ArrayList<String>();
    private List<TextView> allTitles = new ArrayList<TextView>();
    private List<ImageView> images = new ArrayList<ImageView>();
    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadMessage();
            c.readDone = true;
        }
    };
    private Runnable sendSingleThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchById", ids.get(clickNumber - 500));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_by_ingredients);

        Thread connect = new Thread(ReadJSONThread);
        connect.start();

        while (!c.readDone) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        c.readDone = false;


        getTitles(result);
        getIds(result);
        getImageURLs(result);

        LinearLayout ingredientsResult = (LinearLayout) findViewById(R.id.ingredientsResult);

        for (int i = 0; i < ids.size(); i++) {
            allTitles.add(new TextView(this));
            images.add(new ImageView(this));

            allTitles.get(i).setText(titles.get(i));
            new searchRecipesByIngredients.DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));

            images.get(i).setId(500 + i);
            images.get(i).setOnClickListener(this);

            ingredientsResult.addView(allTitles.get(i));
            ingredientsResult.addView(images.get(i));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ids.clear();
        titles.clear();
        images.clear();
        imageURLs.clear();
        allTitles.clear();
        clickNumber = 0;
        c.readDone = false;
    }

    private void getImageURLs (JSONObject result) {
        JSONArray results = null;
        try {
            results = result.getJSONArray("Result");

            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = results.getJSONObject(i);
                    imageURLs.add(jsonObject.getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTitles (JSONObject result) {
        JSONArray results = null;
        try {
            results = result.getJSONArray("Result");
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = results.getJSONObject(i);
                    titles.add(jsonObject.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getIds (JSONObject result) {
        JSONArray results = null;
        try {
            results = result.getJSONArray("Result");
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = results.getJSONObject(i);
                    ids.add(jsonObject.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        clickNumber = v.getId();
        if (clickNumber >= 500 && clickNumber < 550) {
            Thread sendIds = new Thread(sendSingleThread);
            sendIds.start();
            intent = new Intent(searchRecipesByIngredients.this, singleRecipe.class);
            startActivity(intent);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}