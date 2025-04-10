package com.example.project;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Api {

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjOTkxM2ExMjBhNGU0MWY4ODFhODIzZmNlYTMyZWMxYyIsIm5iZiI6MTc0NDE5NTQ1NC42Miwic3ViIjoiNjdmNjRmN2UxYmM2Mzk1NjZhZDkxMmI3Iiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.XAvVi3zyPhd0avbBJ__gGc5l6CHo9AiqK5aaXKAlyDg";

    public static String infoBarCode(String code) {
        OkHttpClient client = new OkHttpClient();


        String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + code;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();

                System.out.println("Réponse UPCitemdb :\n" + body);
                return body;
            } else {
                System.out.println("Erreur : " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }



    public boolean Valide(String json){
        if (json == null || json.isEmpty()) {
            System.out.println("La réponse est vide ou nulle.");
            return false;
        }
        String total = "";
        String [] parts = json.split("\"total\":");

        total = parts[1].split(",")[0].replace("\"", "").trim();
        System.out.println("Total part: " + total);
        if (total.equals("0")){
            System.out.println("Le code-barres n'est pas valide");
            return false;
        }
        else {
            System.out.println("Le code-barres est valide");
            return true;
        }
    }

    public String movieSearch(String search) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.dvdfr.com/api/search.php?gencode="+ search)
                .get()
                .build();



            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    System.out.println("Réponse de l'API :\n" + body);
                    return body;
                    // Traitez la réponse JSON ici
                } else {
                    System.out.println("Erreur : " + response.code());

                    return null ;



                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
        }

        public String getFormat(String json){
            String format = "";
            if (json.contains("<media>")) {
                String [] parts = json.split("<media>");

                format = parts[1].split("</media>")[0];
            }else{
                format = "Unknown";
            }

            System.out.println("Format part: " + format);


            return format;
        }

        public String getDirector(String json){
            String director = "";
            if (json.contains("star type=")) {
                String [] parts = json.split("<star type=\"Réalisateur\".*?>");

                director = parts[1].split("</star>")[0];

            }else {
                director = "Unknown";
            }

            System.out.println("Director part: " + director);

            return director;
        }

        public Integer getAnnee(String json){
            String annee = "";
            int a = 0;
            if (json.contains("<annee>")) {
                String [] parts = json.split("<annee>");

                annee = parts[1].split("</annee>")[0];
                a = Integer.parseInt(annee);

            }

            System.out.println("Annee part: " + a);

            return a;

        }

        public String getTitle(String json){
            String title = "";
            if (json.contains("<titres> <fr>")) {
                String [] parts = json.split("<titres> <fr>");

                title = parts[1].split("</fr>")[0];
            }else{
                title = "Unknown";
            }

            System.out.println("Title part: " + title);


            return title;
        }

    public String getEditor(String json){
        String editor = "";
        if (json.contains("editeur")) {
            String [] parts = json.split("<editeur>");

            editor = parts[1].split("</editeur>")[0];
        }else{
            editor = "Unknown";
        }

        System.out.println("Title part: " + editor);


        return editor;
    }
    }
