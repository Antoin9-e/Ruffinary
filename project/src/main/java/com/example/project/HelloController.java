package com.example.project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.Optional;

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
    private VBox addCase;

    @FXML
    private Button addBTN;

    //Champs de texte pour ajouter une entité
    @FXML
    private TextField titleField;
    @FXML
    private TextField realisateurField;
    @FXML
    private TextField anneeField;
    @FXML
    private TextField genreField;
    @FXML
    private SplitMenuButton formatField;
    private RadioMenuItem dvdMenuAdd;
    private RadioMenuItem UmdMenuAdd;
    private RadioMenuItem BluRayMenuAdd;
    private RadioMenuItem LaDiMenuAdd;
    @FXML private ToggleGroup type;






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
    @FXML
    private Button submitAdd;
    @FXML
            private VBox barcodeAdd;




    // Créer la connexion
    String url = "jdbc:mysql://localhost:3306/ruffinary"; // Remplace par tes infos
    String username = "root"; // Remplace par ton nom d'utilisateur
    String password = "J9ueve-14540"; // Remplace par ton mot de passe






    @FXML
    public void initialize() {
        barcodeAdd.setManaged(false);
        barcodeAdd.setVisible(false);
        addCase.setManaged(false);
        addCase.setVisible(false);

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

        submitAdd.setOnAction(event -> {;
            System.out.println("Button clicked");
            addEntity();
            loadMovieData();
            titleField.clear();
            realisateurField.clear();
            anneeField.clear();
            genreField.clear();
        });


        addBTN.setOnAction(event -> {
            barcodeAdd.setManaged(false);
            barcodeAdd.setVisible(false);
            addCase.setManaged(false);
            addCase.setVisible(false);
            System.out.println("Button clicked");

            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Ajouter un film");
            a.setHeaderText("Veuillez choisir la méthode d'ajout");
            ButtonType b = new ButtonType("Manuellement");
            ButtonType c = new ButtonType("Par Code-barres");
            a.getButtonTypes().clear();
            a.getButtonTypes().setAll(b, c);
            Optional<ButtonType> result = a.showAndWait();

            if (result.get() == b) {
                boolean currentlyVisible = addCase.isVisible();
                addCase.setVisible(!currentlyVisible);
                addCase.setManaged(!currentlyVisible);
            } else {
                boolean currentlyVisible = barcodeAdd.isVisible();
                barcodeAdd.setVisible(!currentlyVisible);
                barcodeAdd.setManaged(!currentlyVisible);
            }




        });


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

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addEntity(){
        try (Connection connection = DriverManager.getConnection(url, username, password);) {

            String titre = titleField.getText();
            String realisateur = realisateurField.getText();
            int annee = Integer.parseInt(anneeField.getText());
            RadioMenuItem selectedFormat = (RadioMenuItem) type.getSelectedToggle();
            String genre = genreField.getText();
            String format = selectedFormat.getText();
            System.out.println("Format: " + format);
            int formatId;
            // Convertir le format en entier
            if (format.equals("Blu-Ray")) {
                formatId = 1;
            } else if (format.equals("Laser-Disc")) {
                formatId = 2;
            } else if (format.equals("DVD")) {
                formatId = 3;
            } else if (format.equals("UMD")) {
                formatId = 4;
            } else {
                System.out.println("Format non valide");
                return;
            }


            String query = "INSERT INTO entity (titre, realisateur, annee_sortie, genre,format_id) VALUES (?, ?, ?, ?,?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, titre);
                    statement.setString(2, realisateur);
                    statement.setInt(3, annee);
                    statement.setString(4, genre);
                    statement.setInt(5, formatId);

                    // Exécuter la requête
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A new entity was inserted successfully!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }catch (NumberFormatException e) {
                System.out.println("Invalid input for year. Please enter a valid number.");
            }





    }





}