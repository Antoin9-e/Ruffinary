package com.example.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnexion {
    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/ruffinary";
//        String username = "root";
//        String password = "J9ueve-14540";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println("✅ Connexion réussie !");
//        } catch (SQLException e) {
//            System.out.println("❌ Erreur de connexion : " + e.getMessage());
//        }

        try {
            // URL de la page de redirection
            String search = "3333299370026";
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
            String json = newDoc.html();

            String title = newDoc.select("h2.lddb").text();  // Ex : Maison aux esprits, La (1993)

            // Extraire le pays
            String country = newDoc.select("td.field:contains(Country) + td.data").text();  // Ex : France

            int startIndex = title.indexOf("(");
            int endIndex = title.indexOf(")");
            // Extraire la date de sortie
            String released = title.substring(startIndex+1,endIndex);  // Ex : ???
            // Extraire le titre sans la date
            title = title.substring(0, startIndex).trim();  // Ex : Maison aux esprits, La

            // Extraire l'éditeur
            String publisher = newDoc.select("td.field:contains(Publisher) + td.data a").text();  // Ex : L'Ecran Laser

            // Extraire le prix
            String price = newDoc.select("td.field:contains(Price) + td.data").text();  // Ex : ???

            // Extraire le code-barres UPC
            String upc = newDoc.select("td.field:contains(UPC) + td.data").text();  // Ex : 3357802488077



            // Afficher les informations extraites
            System.out.println("Titre du produit : " + title);
            System.out.println("Pays : " + country);
            System.out.println("Date de sortie : " + released);
            System.out.println("Éditeur : " + publisher);
            System.out.println("Prix : " + price);
            System.out.println("UPC : " + upc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
