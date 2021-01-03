package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class searchRecipesResult extends AppCompatActivity {

    private JSONObject result;
    private String baseImageURL;
    private List<String> ids = new ArrayList<String>();
    private List<String> titles = new ArrayList<String>();
    private List<String> imageURLs = new ArrayList<String>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_result);

        Thread getMessage = new Thread(ReadJSONThread);
        getMessage.start();

        while (!c.readDone);
        c.readDone = false;

        baseImageURL = getBaseImageURL(result);
        getIds(result);
        getTitles(result);
        getImageURLs(result);

        LinearLayout searchResult = (LinearLayout) findViewById(R.id.searchResult);
        for (int i = 0; i < ids.size(); i++) {
            allTitles.add(new TextView(this));
            images.add(new ImageView(this));

            images.get(i).setId(200 + i);

            new searchRecipesResult.DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));
            allTitles.get(i).setText(titles.get(i));

            searchResult.addView(allTitles.get(i));
            searchResult.addView(images.get(i));
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
        c.readDone = false;
    }

    private String getBaseImageURL(JSONObject object) {
        String baseURL = "";
        try {
            baseURL = object.getString("baseUri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseURL;
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