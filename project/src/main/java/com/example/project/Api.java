package com.example.project;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Api {


    public Document searchLaserDisc(String search) throws IOException {
        try {
            // URL de la page de redirection
            String url = "https://www.lddb.com/search.php?search=" + search;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .followRedirects(true) // Permet de suivre les redirections
                    .get();

            // Afficher tout le contenu HTML de la page redirigée
            String pageHtml = doc.html();  // Récupère tout le HTML de la page après redirection

            Elements tables = doc.select("tr.contents_0");

            String newUrl = "";
            for (Element table : tables) {
                newUrl = table.select("td a").attr("href");
            }

            // Afficher le contenu de la page redirigée
            Document newDoc = Jsoup.connect(newUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();
            // Récupérer une table et extraire des données spécifiques (ex : format vidéo)
           return newDoc;
        }catch (Exception e){
            e.printStackTrace();

        }

        return null;
    }

    public String getLaserDiscTitle(Document doc) throws IOException {
        // Récupérer le titre du LaserDisc
        String title = doc.select("h2.lddb").text();
        System.out.println("Titre : " + title);
        int startIndex = title.indexOf("(");
        title = title.substring(0, startIndex).trim();
        return title;
    }

    public String getLaserDiscCountry(Document doc) throws IOException {
        // Extraire le pays
        String country = doc.select("td.field:contains(Country) + td.data").text();
        System.out.println("Pays : " + country);
        return country;
    }

    public String getLaserDiscPrice(Document doc) throws IOException {
        // Extraire le prix
        String price = doc.select("td.field:contains(Price) + td.data").text();
        System.out.println("Prix : " + price);
        return price;
    }

    public String getLaserDiscCode(Document doc) throws IOException {
        // Extraire le code-barres UPC
        String code = doc.select("h2.lddb").text();
        int startIndex = code.indexOf("[");
        int endIndex = code.indexOf("]");
        code = code.substring(startIndex+1, endIndex);
        System.out.println("UPC : " + code);
        return code;
    }

    public String getLaserDiscPublisher(Document doc) throws IOException {
        // Extraire l'éditeur
        String publisher = doc.select("td.field:contains(Publisher) + td.data a").text();
        System.out.println("Éditeur : " + publisher);
        return publisher;
    }

    public String getLaserDiscReleased(Document doc) throws IOException {
        // Extraire la date de sortie
        String title = doc.select("h2.lddb").text();
        int startIndex = title.indexOf("(");
        int endIndex = title.indexOf(")");
        String released = title.substring(startIndex + 1, endIndex);
        System.out.println("Date de sortie : " + released);
        return released;
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
                if (title.contains("&#039;")){
                    title = title.replace("&#039;", "'");
                }
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
            if (editor.contains("&#039;")){
                editor = editor.replace("&#039;", "'");
            }


        }else{
            editor = "Unknown";
        }

        System.out.println("Title part: " + editor);


        return editor;
    }
    }
