package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class searchFoodVideo extends AppCompatActivity {

    private String YouTubeURL = "https://www.youtube.com/watch?v=";
    private WebView yt;
    private JSONObject result;
    private List<ImageView> images;
    private List<String> imageURLs;
    private List<TextView> titles;
    private List<String> shortTitles;
    private ProgressBar spinner;

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
        /*
        Thread getMessage = new Thread(ReadJSONThread);
        getMessage.start();
        */
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        while (!c.readDone);
        c.readDone = false;


        spinner.setVisibility(View.GONE);

        LinearLayout videos = (LinearLayout) findViewById(R.id.videos);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        imageURLs = getAllPicURL(result);
        shortTitles = getTitle(result);
        images = new ArrayList<ImageView>();
        titles = new ArrayList<TextView>();

        for (int i = 0; i < imageURLs.size(); i++) {

            titles.add(new TextView(this));
            images.add(new ImageView(this));

            new DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));
            titles.get(i).setText(shortTitles.get(i));

            titles.get(i).setId(100 + i);
            images.get(i).setId(120 + i);

            titles.get(i).setLayoutParams(params);
            images.get(i).setLayoutParams(params);



            videos.addView(titles.get(i));
            videos.addView(images.get(i));
        }

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