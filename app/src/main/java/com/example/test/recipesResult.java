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

    private JSONArray result;
    private LinearLayout mLayout;
    private List<String> ids = new ArrayList<String>();
    private List<String> titles = new ArrayList<String>();
    private List<String> imageURLs = new ArrayList<String>();
    private List<TextView> allTitle = new ArrayList<TextView>();
    private List<ImageView> images = new ArrayList<ImageView>();
    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadArray();
            c.readDone = true;
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

        setContentView(R.layout.activity_recipes_result); ///不使用 main.xml 資源

        Thread getResult = new Thread(ReadJSONThread);
        getResult.start();

        while(!c.readDone);
        c.readDone = true;

        getIds(result);
        getImageURLs(result);
        getTitles(result);

        LinearLayout layout = new LinearLayout(this);

        this.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout relative = (LinearLayout) findViewById(R.id.imgLayout);

        for(int i = 0; i < ids.size(); i++){
            allTitle.add(new TextView(this));
            images.add(new ImageView(this));

            allTitle.get(i).setText(titles.get(i));
            allTitle.get(i).setTextSize(50);
            relative.addView(allTitle.get(i), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            new recipesResult.DownloadImageTask(images.get(i))
                    .execute(imageURLs.get(i));

            relative.addView(images.get(i), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

    private void getIds (JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                ids.add(jsonObject.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getImageURLs (JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                imageURLs.add(jsonObject.getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTitles (JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                titles.add(jsonObject.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}