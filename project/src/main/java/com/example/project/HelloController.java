package com.example.project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private BorderPane conteneur;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Label cpt;

    @FXML
    private ChoiceBox<String> genreChoice;

    @FXML
    private ChoiceBox<String> filtreFormat;


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
    @FXML
    private ToggleGroup type;

    @FXML
    private TextField upcField;
    @FXML
    private Button deleteBtn;

    @FXML
    private RadioMenuItem light;

    @FXML
    private RadioMenuItem dark;


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
    private TableColumn<Entity, String> editor;
    @FXML
    private Button submitAdd;
    @FXML
    private Button submitAdd2;
    @FXML
    private VBox barcodeAdd;
    @FXML
    private CheckBox ldCheck;


    // Créer la connexion
    String url = "jdbc:mysql://localhost:3306/ruffinary"; // Remplace par tes infos
    String username = "root"; // Remplace par ton nom d'utilisateur
    String password = "J9ueve-14540"; // Remplace par ton mot de passe


    @FXML
    public void initialize() {


        genreChoice.getItems().addAll("Action", "Aventure", "Science-Fiction", "Comédie", "Romance", "Drame", "Thriller");
        genreChoice.setValue("Action");
        filtreFormat.getItems().addAll("DVD", "Blu-Ray", "UMD", "Laserdisc", "Blu-Ray 4K", "HD-DVD", "Blu-Ray 3d", "All");
        filtreFormat.setValue("All");
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
            editor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEditor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialiser la TableView avec les colonnes

        loadMovieData("");

        // Ajouter un écouteur d'événements pour le champ de recherche
        champRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBar();
        });


        light.setSelected(true);
        light.setOnAction(event -> {
            System.out.println("Light mode selected");
            Theme a = new Theme();
            Scene scene = conteneur.getScene();
            a.setLightMode(scene);
        });

        dark.setOnAction(event -> {
            System.out.println("Dark mode selected");
            Theme a = new Theme();
            Scene scene = conteneur.getScene();
            a.setDarkMode(scene);
        });

        submitAdd.setOnAction(event -> {
            ;
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

        // Ajouter un écouteur d'événements pour le bouton de suppression
        movieList.setOnMouseClicked(event -> {
            if (movieList.getSelectionModel().getSelectedItem() != null) {
                deleteBtn.setDisable(false);
                deleteBtn.setOnAction(evt -> {
                    Entity selectedMovie = movieList.getSelectionModel().getSelectedItem();
                    deleteEntity(selectedMovie);

                });
            } else {
                deleteBtn.setDisable(true);
            }
        });

        filtreFormat.setOnAction(event -> {
            String selectedFormat = filtreFormat.getValue();
            System.out.println("Selected format: " + selectedFormat);
            String filter = "where format_nom = '" + selectedFormat + "'";
            if (selectedFormat.equals("All")) {
                filter = "";
            }
            movieList.getItems().clear();
            loadMovieData(filter);

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


    }

    private  void deleteEntity(Entity entity) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce film ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Bdd b = new Bdd();
            if (b.deleteEntity(entity)) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Suppression reussi");
                a.setHeaderText("Le film a bien été supprimé");
                a.show();
                movieData.remove(entity);
                movieList.getItems().clear();

                String filter = "where format_nom = '" + filtreFormat.getValue() + "'";
                if (filtreFormat.getValue().equals("All")) {
                    filter = "";
                }
                loadMovieData(filter);


            } else {
                Alert f = new Alert(Alert.AlertType.ERROR);
                f.setTitle("Erreur de suppression");
                f.setHeaderText("Le film n'a pas pu être supprimé");
                f.show();
            }
        }

    }

    // Méthode pour charger les données de la base de données
    private void loadMovieData(String filter) {
        try (Connection connection = DriverManager.getConnection(url, username, password);) {

            String query = "SELECT * FROM entity join format using(format_id)" + filter;
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                int cpt = 0;

                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String director = resultSet.getString("realisateur");
                    int year = resultSet.getInt("annee_sortie");
                    String genre = resultSet.getString("genre");
                    String format = resultSet.getString("format_nom");
                    String date_ajout = resultSet.getString("date_ajout");
                    String editor = resultSet.getString("editeur");

                    movieData.add(new Entity(title, director, year, editor, genre, format, date_ajout));
                    cpt++;
                }
                this.cpt.setText(String.valueOf(cpt));

                // Afficher les données dans la TableView
                movieList.setItems(movieData);

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEntity() {
        try {
            Bdd b = new Bdd();
            RadioMenuItem selectedFormat = (RadioMenuItem) type.getSelectedToggle();
            String format = selectedFormat.getText();
            Entity en = new Entity(format);
            System.out.println("Format: " + format + " id: " + en.getFormatId());

            Entity entity = new Entity(titleField.getText(), realisateurField.getText(), Integer.parseInt(anneeField.getText()), editeurTextField.getText(), genreChoice.getValue(), en.getFormat(), LocalDate.now().toString());
            b.addEntity(entity, null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout reussi");
            alert.show();
            movieData.clear();
            loadMovieData("");
            filtreFormat.setValue("All");

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de format");
            alert.setContentText("Veuillez choisir un format valide.");
            alert.showAndWait();
        }

    }


    private void addEntityByBarcode() {
        try {
            String code = upcField.getText();

            if (code.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Code-barres vide");
                alert.setContentText("Veuillez entrer un code-barres valide.");
                alert.showAndWait();
                return;
            }
            if (code.length() != 13) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Code-barres invalide");
                alert.setContentText("Veuillez entrer un code-barres valide.");
                alert.showAndWait();
                return;
            }
            System.out.println("Code-barres: " + code);
            Api a = new Api();


            if (ldCheck.isSelected()) {
                if (a.searchLaserDisc(code) == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Code-barres invalide");
                    alert.setContentText("L'Api ne connait pas le barcode.");
                    alert.showAndWait();
                    return;
                }

                Document doc = a.searchLaserDisc(code);
                String codeLd = a.getLaserDiscCode(doc);
                String titre = a.getLaserDiscTitle(doc);
                String Pays = a.getLaserDiscCountry(doc);
                String prix = a.getLaserDiscPrice(doc);
                String editeur = a.getLaserDiscPublisher(doc);
                String anneesr = a.getLaserDiscReleased(doc);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informations sur le LaserDisc");
                alert.setHeaderText("Informations sur le LaserDisc");

                TextField real = new TextField();
                TextField nom = new TextField();
                TextField annees = new TextField();
                TextField editeurs = new TextField();
                TextField format = new TextField();
                ChoiceBox<String> genre = new ChoiceBox<>();
                genre.getItems().addAll("Action", "Aventure", "Science-Fiction", "Comédie", "Romance", "Drame", "Thriller");
                genre.setValue("Unknow");
                nom.setText(titre);
                real.setText("Unknown");
                annees.setText(anneesr);
                editeurs.setText(editeur);
                format.setText("Laserdisc");


                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.add(new Label("Titre:"), 0, 0);
                grid.add(nom, 1, 0);
                grid.add(new Label("Realisateur:"), 0, 1);
                grid.add(real, 1, 1);
                grid.add(new Label("Annee:"), 0, 2);
                grid.add(annees, 1, 2);
                grid.add(new Label("Editeur:"), 0, 3);
                grid.add(editeurs, 1, 3);
                grid.add(new Label("Format:"), 0, 4);
                grid.add(format, 1, 4);
                grid.add(new Label("Genre:"), 0, 5);
                grid.add(genre, 1, 5);


                alert.getDialogPane().setContent(grid);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Entity entity = new Ld(nom.getText(), real.getText(), Integer.parseInt(annees.getText()), editeurs.getText(), genre.getValue(), format.getText(), LocalDate.now().toString(), a.getLaserDiscCountry(doc), a.getLaserDiscPrice(doc), a.getLaserDiscCode(doc));
                    Bdd b = new Bdd();
                    if (b.addEntity(entity, code)) {
                        System.out.println("Entity added to database");
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Ajout reussi");
                        alert2.setHeaderText("Le LaserDisc a bien été ajouté");
                        alert2.show();
                    }
                    movieData.clear();
                    loadMovieData("");
                    upcField.clear();
                    filtreFormat.setValue("All");

                } else {
                    System.out.println("Cancel clicked");
                }

            } else {
                if (a.movieSearch(code) == null) {
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
                TextField real = new TextField();
                TextField nom = new TextField();
                TextField annee = new TextField();
                TextField editeur = new TextField();
                TextField format = new TextField();
                ChoiceBox<String> genre = new ChoiceBox<>();
                genre.getItems().addAll("Action", "Aventure", "Science-Fiction", "Comédie", "Romance", "Drame", "Thriller");
                genre.setValue("Unknow");
                nom.setText(a.getTitle(json));
                real.setText(a.getDirector(json));
                annee.setText(String.valueOf(a.getAnnee(json)));
                editeur.setText(a.getEditor(json));
                format.setText(a.getFormat(json));


                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.add(new Label("Titre:"), 0, 0);
                grid.add(nom, 1, 0);
                grid.add(new Label("Realisateur:"), 0, 1);
                grid.add(real, 1, 1);
                grid.add(new Label("Annee:"), 0, 2);
                grid.add(annee, 1, 2);
                grid.add(new Label("Editeur:"), 0, 3);
                grid.add(editeur, 1, 3);
                grid.add(new Label("Format:"), 0, 4);
                grid.add(format, 1, 4);
                grid.add(new Label("Genre:"), 0, 5);
                grid.add(genre, 1, 5);


                conf.getDialogPane().setContent(grid);

                Optional<ButtonType> result = conf.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Entity entity = new Entity(nom.getText(), real.getText(), Integer.parseInt(annee.getText()), editeur.getText(), genre.getValue(), format.getText(), LocalDate.now().toString());

                    Bdd b = new Bdd();
                    if (b.addEntity(entity, code)) {
                        System.out.println("Entity added to database");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Ajout reussi");
                        alert.setHeaderText("Le film a bien été ajouté");
                        alert.show();
                    }
                    movieData.clear();
                    loadMovieData("");
                    upcField.clear();
                    filtreFormat.setValue("All");

                } else {
                    System.out.println("Cancel clicked");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchBar() {
        String query = champRecherche.getText();
        String format = filtreFormat.getValue();

        String filter = "where format_nom = '" + format + "'  and ( titre like '%" + query + "%' or realisateur like '%" + query + "%' or editeur like '%" + query + "%')";
        if (format.equals("All")) {
            filter = "where titre like '%" + query + "%' or realisateur like '%" + query + "%' or editeur like '%" + query + "%'";
        }
        movieList.getItems().clear();
        loadMovieData(filter);
        System.out.println("Query: " + query);
        System.out.println("Filter: " + filter);

        }

    }







