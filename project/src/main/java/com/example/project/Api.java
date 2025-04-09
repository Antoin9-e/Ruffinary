package com.example.project;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        String upcCode = "3384442257954";
        String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + upcCode;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                System.out.println("Réponse UPCitemdb :\n" + body);
            } else {
                System.out.println("Erreur : " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
