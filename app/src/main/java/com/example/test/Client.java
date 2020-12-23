package com.example.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

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

    /**
     * send method and param
     * for example searchRecipes pizza
     * @param message
     */
    public void SendMessage(String message) {
        try {
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String ReadMessage() {
        String read = "";
        try {
            DataInputStream in = new DataInputStream(this.client.getInputStream());
            read = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
    }

}

