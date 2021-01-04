package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class singleRecipe extends AppCompatActivity implements View.OnClickListener {

    private EditText commandInput;
    private String title;
    private String readyInMinutes;
    private String sourceUrl;
    private String summary;
    private String imageURL;
    private String servings;
    private JSONObject result;
    private JSONArray extendedIngredients;
    private List<String> prepareFood = new ArrayList<String>();
    private List<String> Ingredients = new ArrayList<String>();
    private List<String> steps = new ArrayList<String>();
    private TextView titleView;
    private ImageView ImageView;
    private List<TextView> prepareFoodView = new ArrayList<TextView>();
    private List<TextView> stepsView = new ArrayList<TextView>();
    private List<TextView> commandsView = new ArrayList<TextView>();
    boolean Login = searchRecipes.Login;
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
        setContentView(R.layout.activity_single_recipe);
        titleView = ((TextView) findViewById(R.id.singleTitleView));
        ImageView = ((ImageView) findViewById(R.id.singleImageView));
        ((Button) findViewById(R.id.sendCommandButton)).setOnClickListener(this);
        commandInput = ((EditText) findViewById(R.id.commandText));


        Thread read = new Thread(ReadJSONThread);
        read.start();
        while (!c.readDone){
            try {
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        c.readDone = false;

        getTitle(result);
        getReadyInMinutes(result);
        getSourceUrl(result);
        getSummary(result);
        getImageURL(result);
        getServings(result);
        getAllExtendedIngredients(result);
        getAnalyzedInstructions(result);

        titleView.setText(title);
        titleView.setTextSize(25);

        new singleRecipe.DownloadImageTask(ImageView)
                .execute(imageURL);

        LinearLayout searchResult = (LinearLayout) findViewById(R.id.singleLayout);
        prepareFoodView.add(new TextView(this));
        prepareFoodView.get(0).setText("Ingredients");
        prepareFoodView.get(0).setTextSize(20);
        searchResult.addView(prepareFoodView.get(0));

        for (int i = 0; i < prepareFood.size(); i++) {
            prepareFoodView.add(new TextView(this));
            prepareFoodView.get(i).setText(prepareFood.get(i));
            prepareFoodView.get(i).setTextSize(15);
            searchResult.addView(prepareFoodView.get(i));
        }


    }

    @Override
    public void onClick(View v) {
        int clickNumber = v.getId();
        String sendMessage = "";
        if (clickNumber == R.id.sendCommandButton && Login) {
            Toast.makeText(singleRecipe.this, "你尚未登入",Toast.LENGTH_SHORT).show();
        } else if (clickNumber == R.id.sendCommandButton && Login) {
            sendMessage = commandInput.getText().toString().trim();
        }
    }

    private void getTitle(JSONObject result) {
        try {
            title = result.getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReadyInMinutes(JSONObject result) {
        try {
            readyInMinutes = result.getString("readyInMinutes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSourceUrl(JSONObject result) {
        try {
            sourceUrl = result.getString("sourceUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSummary(JSONObject result) {
        try {
            summary = result.getString("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImageURL(JSONObject result) {
        try {
            imageURL = result.getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServings(JSONObject result) {
        try {
            servings = result.getString("servings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllExtendedIngredients(JSONObject result) {
        try {
            extendedIngredients = result.getJSONArray("extendedIngredients");
            for (int i = 0; i < extendedIngredients.length(); i++) {
                JSONObject singleIngredient = extendedIngredients.getJSONObject(i);
                Ingredients.add(singleIngredient.getString("originalString"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAnalyzedInstructions(JSONObject result) {
        try {
            JSONArray allSteps = result.getJSONArray("analyzedInstructions");
            for (int i = 0; i < allSteps.length(); i++) {
                JSONObject step = allSteps.getJSONObject(i);
                steps.add(step.getString("step"));
            }
        } catch (Exception e) {
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