package com.example.test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private String ServerName;
    private int port;
    Socket client;
    String send;

    public Client(String ServerName, int port) {
        this.ServerName = ServerName;
        this.port = port;
        try {
            client = new Socket(ServerName, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("HELLO FROM " + client.getLocalSocketAddress());
        } catch (IOException e) {
           try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return this.client;
    }

    /**
     * send method and param
     * for example searchRecipes pizza
     * @param message
     */
    public void SendMessage(Client c, String function, String message) {

        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(function, message);

            JSONObject output = new JSONObject(map);
            String jsonString = "";

            jsonString = output.toString();
            OutputStream outToServer = c.getSocket().getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JSONObject ReadMessage() {
        String read = "";
        JSONObject input = null;
        try {
            DataInputStream in = new DataInputStream(this.client.getInputStream());
            read = in.readUTF();
            input = new JSONObject(read);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return input;
    }

    public void SendSearchRecipesByNutrients (Client c, String function, String carbsMin, String carbsMax,
                                              String caloriesMin, String caloriesMax, String proteinMin, String proteinMax,
                                              String fatMin, String fatMax, String vitaminCMin, String vitaminCMax,
                                              String fiberMin, String fiberMax, String sugarMin, String sugarMax,
                                              String ironMin, String ironMax) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> index = new HashMap<String, String>();

        if(carbsMin.equals("")) {
            index.put("carbsMin", "0");
        } else {
            index.put("carbsMin", carbsMin);
        }
        if(carbsMax.equals("")) {
            index.put("carbsMax", "0");
        } else {
            index.put("carbsMax", carbsMax);
        }

        if(caloriesMin.equals("")) {
            index.put("caloriesMin", "0");
        } else {
            index.put("caloriesMin", caloriesMin);
        }
        if(caloriesMax.equals("")) {
            index.put("caloriesMax", "0");
        } else {
            index.put("caloriesMax", caloriesMax);
        }

        if(proteinMin.equals("")) {
            index.put("proteinMin", "0");
        } else {
            index.put("proteinMin", proteinMin);
        }
        if(proteinMax.equals("")) {
            index.put("proteinMax", "0");
        } else {
            index.put("proteinMax", proteinMax);
        }

        if(fatMin.equals("")) {
            index.put("fatMin", "0");
        } else {
            index.put("fatMin", fatMin);
        }
        if(fatMax.equals("")) {
            index.put("fatMax", "0");
        } else {
            index.put("fatMax", fatMax);
        }

        if(vitaminCMin.equals("")) {
            index.put("vitaminCMin", "0");
        } else {
            index.put("vitaminCMin", vitaminCMin);
        }
        if(vitaminCMax.equals("")) {
            index.put("vitaminCMax", "0");
        } else {
            index.put("vitaminCMax", vitaminCMax);
        }

        if(fiberMin.equals("")) {
            index.put("fiberMin", "0");
        } else {
            index.put("fiberMin", fiberMin);
        }
        if(fiberMax.equals("")) {
            index.put("fiberMax", "0");
        } else {
            index.put("fiberMax", fiberMax);
        }

        if(sugarMin.equals("")) {
            index.put("sugarMin", "0");
        } else {
            index.put("sugarMin", sugarMin);
        }
        if(sugarMax.equals("")) {
            index.put("sugarMax", "0");
        } else {
            index.put("sugarMax", sugarMax);
        }

        if(ironMin.equals("")) {
            index.put("ironMin", "0");
        } else {
            index.put("ironMin", ironMin);
        }
        if(ironMax.equals("")) {
            index.put("ironMax", "0");
        } else {
            index.put("ironMax", ironMax);
        }

        map.put(function, index);

        try {
            JSONObject output = new JSONObject(map);
            String jsonString = "";

            jsonString = output.toString();
            OutputStream outToServer = c.getSocket().getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLoginInformation (Client c, String function, String Account, String password) {
        Map<String, Object> login = new HashMap<String, Object>();
        Map<String, String> account = new HashMap<String, String>();

        account.put("Account", Account);
        account.put("password", password);

        login.put(function, account);
        try {
            JSONObject output = new JSONObject(map);
            String jsonString = "";
            jsonString = output.toString();
            OutputStream outToServer = c.getSocket().getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

