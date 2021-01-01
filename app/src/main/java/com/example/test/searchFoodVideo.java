package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class searchFoodVideo extends AppCompatActivity {

    private String YouTubeURL = "https://www.youtube.com/watch?v=";
    private WebView yt;
    private JSONObject result;
    private List<ImageView> images;

    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadMessage();
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_video);
        //GraphTemperature GT = new GraphTemperature(getApplicationContext());
        LinearLayout test = (LinearLayout) findViewById(R.id.test);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button buyButton = new Button(this);
        buyButton.setText("123");
        buyButton.setId(70);
        buyButton.setLayoutParams(params);
        images = new ArrayList<ImageView>();
        images.add(new ImageView(this));
        images.get(0).setId(90);
        images.get(0).setLayoutParams(params);
        new DownloadImageTask(images.get(0))
                .execute("https://i.ytimg.com/vi/iteIg6OXiac/mqdefault.jpg");

        test.addView(images.get(0));
        test.addView(buyButton);

        /*
        ImageView imageview= new ImageView(getApplicationContext());
        imageview.setImageResource();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        params.leftMargin = 100;
        params.topMargin = 100;
        */

        /*
        yt = (WebView) findViewById(R.id.yt);
        yt.getSettings().setJavaScriptEnabled(true);

        yt.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        yt.loadUrl("https://www.youtube.com/watch?v=xYcU5w8GpDA&list=RD6hIjKViufzI&index=15&ab_channel=TheBellas-Topic");
        */
    }

    private List<String> getAllPicURL (JSONObject input) {
        List<String> pictures = new ArrayList<String>();
        try {
            JSONArray videos = input.getJSONArray("videos");
            for (int i = 0; i < videos.length(); i++) {
                JSONObject jsonObject = videos.getJSONObject(i);
                pictures.add(jsonObject.getString("thumbnail"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pictures;
    }

    private List<String> getYouTubeURL (JSONObject input) {
        List<String> YouTubeURLs = new ArrayList<String>();
        try {
            JSONArray videos = input.getJSONArray("videos");
            for (int i = 0; i < videos.length(); i++) {
                JSONObject jsonObject = videos.getJSONObject(i);
                YouTubeURLs.add(jsonObject.getString("youTubeId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return YouTubeURLs;
    }

    private List<String> getTitle (JSONObject input) {
        List<String> Titles = new ArrayList<String>();
        try {
            JSONArray videos = input.getJSONArray("videos");
            for (int i = 0; i < videos.length(); i++) {
                JSONObject jsonObject = videos.getJSONObject(i);
                Titles.add(jsonObject.getString("shortTitle"));
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