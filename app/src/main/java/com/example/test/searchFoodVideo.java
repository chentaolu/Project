package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class searchFoodVideo extends AppCompatActivity implements View.OnClickListener {

    private String YouTubeURL = "https://www.youtube.com/watch?v=";
    private WebView yt;
    private JSONObject result;
    private List<ImageView> images = new ArrayList<ImageView>();
    private List<String> imageURLs = new ArrayList<String>();
    private List<TextView> titles = new ArrayList<TextView>();
    private List<String> shortTitles = new ArrayList<String>();
    private List<String> youtubeIds = new ArrayList<String>();

    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadMessage();
            c.readDone = true;
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_video);

        Thread getMessage = new Thread(ReadJSONThread);
        getMessage.start();

        while (!c.readDone) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        c.readDone = false;

        LinearLayout videos = (LinearLayout) findViewById(R.id.videos);

        getImageURLs(result);
        getTitles(result);
        getYouTubeURL(result);


        for (int i = 0; i < imageURLs.size(); i++) {

            titles.add(new TextView(this));
            images.add(new ImageView(this));

            new DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));
            titles.get(i).setText(shortTitles.get(i));
            titles.get(i).setTextColor(R.color.black);
            titles.get(i).setTextSize(20);
            titles.get(i).setGravity(Gravity.CENTER);
            images.get(i).setId(100 + i);
            images.get(i).setForegroundGravity(Gravity.CENTER);
            images.get(i).setOnClickListener(this);

            videos.addView(titles.get(i));
            videos.addView(images.get(i));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        images.clear();
        titles.clear();
        imageURLs.clear();
        shortTitles.clear();
        result = null;
        c.readDone = false;
    }

    private void getImageURLs (JSONObject result) {
        JSONArray results = null;
        try {
            results = result.getJSONArray("Result");

            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = results.getJSONObject(i);
                    imageURLs.add(jsonObject.getString("thumbnail"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getYouTubeURL (JSONObject input) {
        JSONArray results = null;
        try {
            results = result.getJSONArray("Result");
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = results.getJSONObject(i);
                    youtubeIds.add(jsonObject.getString("youTubeId"));
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
                    shortTitles.add(jsonObject.getString("title"));
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
        int clickNum = v.getId();
        if (clickNum >= 100 && clickNum < 150) {
            try {
                yt = (WebView) findViewById(R.id.yt);
                yt.getSettings().setJavaScriptEnabled(true);
                yt.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                yt.loadUrl(YouTubeURL + youtubeIds.get(clickNum - 100));
            } catch (Exception e) {
                e.printStackTrace();
            }
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