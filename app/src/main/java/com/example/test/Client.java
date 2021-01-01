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

}

