package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class recipesResult extends AppCompatActivity {

    private LinearLayout mLayout;
    private JSONObject result;
    private List<ImageView> images;
    private List<String> imageURLs;
    private List<String> recipeTitle;

    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadMessage();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_video);

        Thread getMessage = new Thread(ReadJSONThread);
        getMessage.start();

        //GraphTemperature GT = new GraphTemperature(getApplicationContext());
        LinearLayout test = (LinearLayout) findViewById(R.id.imgLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        imageURLs = getAllPicURL(result);
        recipeTitle = getTitle(result);
        images = new ArrayList<ImageView>();

        for (int i = 0; i < imageURLs.size(); i++) {
            images.add(new ImageView(this));
            images.get(i).setId(0);
            images.get(i).setLayoutParams(params);
            new DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));
            test.addView(images.get(i));
        }

    }

    private List<String> getAllPicURL (JSONObject input) {
        List<String> pictures = new ArrayList<String>();
        try {
            JSONArray result = input.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                pictures.add(jsonObject.getString("image"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pictures;
    }

    private List<String> getTitle (JSONObject input) {
        List<String> Titles = new ArrayList<String>();
        try {
            JSONArray result = input.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                Titles.add(jsonObject.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Titles;
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