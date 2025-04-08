package com.example.project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.*;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }




    @FXML
    private Button btnAffiche;

    @FXML
    private TextField champRecherche;





    @FXML
    private HBox optionBar;





    //table des données
    private ObservableList<Entity> movieData = FXCollections.observableArrayList();
    @FXML
    private TableView<Entity> movieList;
    @FXML
    private TableColumn<Entity, String> title;
    @FXML
    private TableColumn<Entity, String> format;

    @FXML
    private TableColumn<Entity, String> genre;
    @FXML
    private TableColumn<Entity, Integer> annee;
    @FXML
    private TableColumn<Entity, String> director;
    @FXML
    private TableColumn<Entity, String> ajout;




    // Créer la connexion
    String url = "jdbc:mysql://localhost:3306/ruffinary"; // Remplace par tes infos
    String username = "root"; // Remplace par ton nom d'utilisateur
    String password = "J9ueve-14540"; // Remplace par ton mot de passe






    @FXML
    public void initialize() {
        optionBar.setVisible(false);
        optionBar.setManaged(false);

        try {
            title.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTitle()));
            annee.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYear()).asObject());
            genre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
            format.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormat()));
            director.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector()));
            ajout.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateAjout()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Initialiser la TableView avec les colonnes

        loadMovieData();


        btnAffiche.setOnAction(event -> {

            System.out.println("Button clicked");


            boolean currentlyVisible = optionBar.isVisible();
            optionBar.setVisible(!currentlyVisible);
            optionBar.setManaged(!currentlyVisible);
        });
    }

    // Méthode pour charger les données de la base de données
    private void loadMovieData() {
        try (Connection connection = DriverManager.getConnection(url, username, password);) {
            String query = "SELECT * FROM entity join format using(format_id)";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String director = resultSet.getString("realisateur");
                    int year = resultSet.getInt("annee_sortie");
                    String genre = resultSet.getString("genre");
                    String format = resultSet.getString("format_nom");
                    String date_ajout = resultSet.getString("date_ajout");

                    movieData.add(new Entity(title, director, year, genre, format, date_ajout));
                }

                // Afficher les données dans la TableView
                movieList.setItems(movieData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}