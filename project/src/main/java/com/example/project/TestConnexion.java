package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnexion {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ruffinary";
        String username = "root";
        String password = "J9ueve-14540";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Connexion réussie !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }

}
