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
import java.time.LocalDate;
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


    @FXML
    private TextField editeurTextField;
    @FXML private ToggleGroup type;

    @FXML
    private TextField upcField ;






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
    private Button submitAdd2;
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

            titleField.clear();
            realisateurField.clear();
            anneeField.clear();
            genreField.clear();
            editeurTextField.clear();
        });

        submitAdd2.setOnAction(event -> {
            System.out.println("Button clicked");
            addEntityByBarcode();
            upcField.clear();
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
                    String editor = resultSet.getString("editeur");

                    movieData.add(new Entity(title, director, year, editor, genre, format, date_ajout));
                }

                // Afficher les données dans la TableView
                movieList.setItems(movieData);

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  void addEntity() {
        Bdd b = new Bdd();
        RadioMenuItem selectedFormat = (RadioMenuItem) type.getSelectedToggle();
        String format = selectedFormat.getText();
        Entity en = new Entity(format);
        System.out.println( "Format: " + format + " id: " + en.getFormatId());

        Entity entity = new Entity(titleField.getText(), realisateurField.getText(), Integer.parseInt(anneeField.getText()), editeurTextField.getText(), genreField.getText(), en.getFormat(), LocalDate.now().toString());
        b.addEntity(entity, null);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout reussi");
        alert.show();
        movieData.clear();
        loadMovieData();
    }





    private void addEntityByBarcode(){
        try{
            String code = upcField.getText();

            if (code.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Code-barres vide");
                alert.setContentText("Veuillez entrer un code-barres valide.");
                alert.showAndWait();
                return;
            }
            if (code.length() != 13){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Code-barres invalide");
                alert.setContentText("Veuillez entrer un code-barres valide.");
                alert.showAndWait();
                return;
            }
            System.out.println("Code-barres: " + code);
            Api a = new Api();
            if (a.movieSearch(code) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Code-barres invalide");
                alert.setContentText("L'Api ne connait pas le barcode.");
                alert.showAndWait();
                return;
            }
            String json = a.movieSearch(code);


            a.getFormat(json);
            a.getDirector(json);
            a.getAnnee(json);
            a.getTitle(json);
            a.getEditor(json);

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
            conf.setTitle("Confirmation des informations recues");
            conf.setHeaderText("Liste des informations recues");
            conf.setContentText(
                    "Titre:   " + a.getTitle(json) + "\n" +
                            "Realisateur:   " + a.getDirector(json) + "\n" +
                            "Annee:   " + a.getAnnee(json) + "\n" +
                            "Format:   " + a.getFormat(json) + "\n" +
                            "Editeur:   " + a.getEditor(json)
            );

            Optional<ButtonType> result = conf.showAndWait();
            if (result.get() == ButtonType.OK){
                Entity entity = new Entity(a.getTitle(json), a.getDirector(json), a.getAnnee(json), a.getEditor(json), "Unknow",a.getFormat(json), LocalDate.now().toString());

                Bdd b = new Bdd();
                b.addEntity(entity, code);
                System.out.println("Entity added to database");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajout reussi");
                alert.show();
                movieData.clear();
                loadMovieData();
                upcField.clear();
            }
            else {
                System.out.println("Cancel clicked");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }







}