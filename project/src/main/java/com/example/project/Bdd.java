package com.example.project;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bdd {

    public Connection connect() {
        String url = "jdbc:mysql://localhost:3306/ruffinary";
        String username = "root";
        String password = "J9ueve-14540";

        try {
            // Connexion à la base de données sans fermer immédiatement
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connexion réussie !");
            return connection;  // Ne pas fermer la connexion ici
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
            return null;
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connexion fermée !");
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    public void addEntity(Entity entity, String code_barre) {
        Connection connection = null;
        try {
            connection = connect();  // Connexion ouverte pour exécuter les requêtes
            if (connection != null) {
                String sql = "INSERT INTO entity (titre, realisateur, annee_sortie, code_barre, genre, format_id, date_ajout) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, entity.getTitle());
                    preparedStatement.setString(2, entity.getDirector());
                    preparedStatement.setInt(3, entity.getYear());
                    preparedStatement.setString(4, code_barre);
                    preparedStatement.setString(5, entity.getGenre());
                    preparedStatement.setInt(6, entity.getFormatId());
                    preparedStatement.setString(7, entity.getDateAjout());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("✅ Entité ajoutée avec succès !");
                    } else {
                        System.out.println("❌ Aucune entité ajoutée.");
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Erreur d'exécution de la requête : " + e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur d'ajout de l'entité");
                    alert.setContentText("Une erreur s'est produite lors de l'ajout de l'entité : " + e.getMessage());
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion ou autre : " + e.getMessage());
        } finally {
            closeConnection(connection);  // Connexion fermée à la fin de la méthode
        }
    }
}
