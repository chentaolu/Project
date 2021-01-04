package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class singleRecipe extends AppCompatActivity implements View.OnClickListener {

    private EditText commandInput;
    private String title;
    private String readyInMinutes;
    private String sourceUrl;
    private String summary;
    private String imageURL;
    private String servings;
    private String recipeId;
    private String sendCommand;
    private JSONObject result;
    private JSONArray extendedIngredients;
    private List<String> commandsIds = new ArrayList<String>();
    private List<String> Ingredients = new ArrayList<String>();
    private List<String> steps = new ArrayList<String>();
    private List<String> commands = new ArrayList<String>();

    private TextView titleView;
    private TextView minuteView;

    private TextView idView;
    private TextView setView1;
    private TextView setView2;
    private ImageView ImageView;

    private List<TextView> stepCount = new ArrayList<TextView>();

    private List<TextView> ingredientsView = new ArrayList<TextView>();
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
    private Runnable sendCommandThread = new Runnable() {
        @Override
        public void run() {
            c.SendCommandMessage(c, "sendComment", recipeId, com.example.test.Login.id, sendCommand);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);
        idView =  ((TextView) findViewById(R.id.idView));
        setView1 =  ((TextView) findViewById(R.id.extendedIngredient));
        setView2 =  ((TextView) findViewById(R.id.steps));
        titleView = ((TextView) findViewById(R.id.singleTitleView));
        minuteView = ((TextView) findViewById(R.id.prepareMin));
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

        getRecipeID(result);
        getTitle(result);
        getReadyInMinutes(result);
        getSourceUrl(result);
        getSummary(result);
        getImageURL(result);
        getServings(result);
        getAllExtendedIngredients(result);
        getAnalyzedInstructions(result);
        getCommandsId(result);
        getCommands(result);

        LinearLayout allPage = (LinearLayout) findViewById(R.id.allPage);
        LinearLayout basePart = (LinearLayout) findViewById(R.id.basePart);
        LinearLayout dynamicPart1 = (LinearLayout) findViewById(R.id.dynamicPart1);
        LinearLayout dynamicPart2 = (LinearLayout) findViewById(R.id.dynamicPart2);
        LinearLayout respondPart = (LinearLayout) findViewById(R.id.respondPart);

        titleView.setText(title);
        titleView.setTextSize(25);
        titleView.setGravity(Gravity.CENTER);
        minuteView.setText("prepare time: " + readyInMinutes + " minutes, serving: " + servings);
        minuteView.setTextSize(20);
        new singleRecipe.DownloadImageTask(ImageView)
                .execute(imageURL);

        if(Login){
            idView.setText(com.example.test.Login.id);
            idView.setGravity(Gravity.CENTER_VERTICAL);
            idView.setTextSize(20);
        } else{
            idView.setText("訪客");
            idView.setGravity(Gravity.CENTER_VERTICAL);
            idView.setTextSize(20);
        }
        setView1.setText("Ingredients:");
        for(int i = 0; i < Ingredients.size(); i++){
            ingredientsView.add(new TextView(this));
            ingredientsView.get(i).setText(Ingredients.get(i));
            dynamicPart1.addView(ingredientsView.get(i));
        }
        setView2.setText("Steps:");
        for(int i = 0; i < steps.size(); i++){
            stepCount.add(new TextView(this));
            stepsView.add(new TextView(this));
            int count = i+1;
            stepCount.get(i).setText("step " + count + ":");
            stepsView.get(i).setText(steps.get(i));
            dynamicPart2.addView(stepCount.get(i));
            dynamicPart2.addView(stepsView.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        int clickNumber = v.getId();
        if (clickNumber == R.id.sendCommandButton && Login == false) {
            Toast.makeText(singleRecipe.this, "你尚未登入",Toast.LENGTH_SHORT).show();
        } else if (clickNumber == R.id.sendCommandButton && Login == true) {
            sendCommand = commandInput.getText().toString().trim();
            if (sendCommand.equals("")) {
                Toast.makeText(singleRecipe.this, "請填入評論",Toast.LENGTH_SHORT).show();
            } else {
                commandInput.setText(null);
                Thread sendJson = new Thread(sendCommandThread);
                sendJson.start();
                finish();
                startActivity(getIntent());
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        title = "";
        readyInMinutes = "";
        sourceUrl = "";
        summary = "";
        imageURL = "";
        servings = "";
        recipeId = "";
        sendCommand = "";
        result = null;
        extendedIngredients = null;
        commandsIds.clear();
        Ingredients.clear();
        steps.clear();
        commands.clear();
        ingredientsView.clear();
        steps.clear();
        commandsView.clear();
        c.readDone = false;
    }

    private void getRecipeID(JSONObject result) {
        try {
            recipeId = result.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCommandsId(JSONObject result) {
        try {
            JSONArray comments = result.getJSONArray("comments");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject object = comments.getJSONObject(i);
                commandsIds.add(object.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCommands(JSONObject result) {
        try {
            JSONArray comments = result.getJSONArray("comments");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject object = comments.getJSONObject(i);
                commands.add(object.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
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