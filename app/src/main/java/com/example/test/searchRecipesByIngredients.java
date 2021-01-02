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
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class searchRecipesByIngredients extends AppCompatActivity {

    private JSONArray result;
    private List<String> imagesURLs = new ArrayList<String>();
    private List<String> titles = new ArrayList<String>();
    public List<String> ids = new ArrayList<String>();
    private List<TextView> allTitles;
    private List<ImageView> images;
    Client c = searchRecipes.c;
    private Runnable ReadJSONThread = new Runnable() {
        @Override
        public void run() {
            result = c.ReadArray();
            c.readDone = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes_by_ingredients);

        Thread connect = new Thread(ReadJSONThread);
        connect.start();

        getTitles(result);
        getIds(result);
        getPictureURLs(result);

        LinearLayout ingredientsResult = (LinearLayout) findViewById(R.id.ingredientsResult);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < ids.size(); i++) {
            allTitles.add(new TextView(this));
            images.add(new ImageView(this));
            allTitles.get(i).setText(titles.get(i));
            new searchRecipesByIngredients.DownloadImageTask(images.get(i))
                    .execute(imagesURLs.get(i));

            allTitles.get(i).setId(140 + i);
            images.get(i).setId(160 + i);

            allTitles.get(i).setLayoutParams(params);
            images.get(i).setLayoutParams(params);

            ingredientsResult.addView(allTitles.get(i));
            ingredientsResult.addView(images.get(i));

        }

    }

    private void getPictureURLs(JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject object = result.getJSONObject(i);
                imagesURLs.add(object.getString("image"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getTitles(JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject object = result.getJSONObject(i);
                titles.add(object.getString("title"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private  void getIds(JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject object = result.getJSONObject(i);
                titles.add(object.getString("id"));
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