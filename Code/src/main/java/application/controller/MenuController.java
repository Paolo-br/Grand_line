package application.controller;

import application.model.CharacterModel;
import application.model.CharacterSelection;
import application.model.GameManager;
import application.model.SoundManager;
import application.view.MenuView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;

public class MenuController {

    public ImageView easyImageView, mediumImageView, hardImageView;

    @FXML
    private ImageView characterView, mapView; //Personnalisation
    private CharacterModel model;

    @FXML
    private Button buttonPersonnalisation, buttonPlay, buttonRules;
    @FXML
    private RadioButton easyRadio;
    @FXML
    private RadioButton mediumRadio;
    @FXML
    private RadioButton hardRadio;
    @FXML
    private ToggleGroup difficultyGroup;
    private StackPane loadingOverlay;


    @FXML
    public void initialize() {
        SoundManager.playBackgroundMusic("/musiqueOnePiece.mp3");
        difficultyGroup = new ToggleGroup();
        easyRadio.setToggleGroup(difficultyGroup);
        mediumRadio.setToggleGroup(difficultyGroup);
        hardRadio.setToggleGroup(difficultyGroup);

        easyImageView.setImage(new Image(getClass().getResource("/persoEasyOmbre.png").toExternalForm()));
        mediumImageView.setImage(new Image(getClass().getResource("/persoMedium.png").toExternalForm()));
        hardImageView.setImage(new Image(getClass().getResource("/persoHard.png").toExternalForm()));
        // Changement visuel à la sélection
        easyRadio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            easyImageView.setImage(new Image(getClass().getResource(isSelected ?
                    "/persoEasyOmbre.png" : "/persoEasy.png").toExternalForm()));
        });

        mediumRadio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            mediumImageView.setImage(new Image(getClass().getResource(isSelected ?
                    "/persoMediumOmbre.png" : "/persoMedium.png").toExternalForm()));
        });

        hardRadio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            hardImageView.setImage(new Image(getClass().getResource(isSelected ?
                    "/persoHardOmbre.png" : "/persoHard.png").toExternalForm()));
        });


        buttonPersonnalisation.setOnAction(e -> {
            MenuView.showPersonnalisationWindow(this);
            SoundManager.playClickSound("/clic.mp3", 0.8);
        });

        buttonPlay.setOnAction(e -> {
            int selectedDifficulty = getSelectedDifficulty();
            GameManager.getInstance().setSelectedDifficulty(selectedDifficulty);
            SoundManager.playClickSound("/clic.mp3", 0.8);

            showLoadingOverlay(); // Affiche le loading

            // Lancer le chargement en arrière-plan
            Task<Void> loadGameTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Simule le chargement (à remplacer par ta logique réelle)
                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void succeeded() {
                    hideLoadingOverlay();
                    SoundManager.stopBackgroundMusic();
                    MenuView.showPlayWindow();
                    Stage stage = (Stage) buttonPlay.getScene().getWindow();
                    stage.close();
                }
            };

            new Thread(loadGameTask).start();
        });

        buttonRules.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReglesJeu.fxml"));
                Scene scene = new Scene(loader.load());

                Stage currentStage = (Stage) buttonRules.getScene().getWindow();
                currentStage.setScene(scene); // remplace la scène actuelle
                currentStage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        updateImages();
        updateMap(GameManager.getInstance().getSelectedMapImageName());
    }

    public void updateImages() {
        CharacterSelection character = GameManager.getInstance().getSelectedCharacter();
        if (character != null) {
            String fileName = character.getCharacterImageName();
            System.out.println(fileName);
            Image characterImage = new Image("/" + fileName);
            characterView.setImage(characterImage);
        }
    }

    public void updateMap(String map) {
        Image imageMap = new Image("/" + map);
        mapView.setImage(imageMap);
    }

    public int getSelectedDifficulty() {
        RadioButton selected = (RadioButton) difficultyGroup.getSelectedToggle();
        String text = selected.getText().toLowerCase();
        return switch (text) {
            case "facile" -> 1;
            case "moyen" -> 2;
            case "difficile" -> 3;
            default -> 2; // Par défaut "Moyen"
        };
    }

    private void showLoadingOverlay() {
        if (loadingOverlay == null) {
            loadingOverlay = new StackPane();
            loadingOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

            ProgressIndicator spinner = new ProgressIndicator();
            spinner.setMaxSize(100, 100);
            spinner.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

            loadingOverlay.getChildren().add(spinner);
            // Ajoute au root de ta scène
            Pane root =(Pane) buttonPlay.getScene().getRoot();
            loadingOverlay.prefWidthProperty().bind(root.prefWidthProperty());
            loadingOverlay.prefHeightProperty().bind(root.prefHeightProperty());
            root.getChildren().add(loadingOverlay);
        }
        loadingOverlay.toFront();
        loadingOverlay.setVisible(true);
    }

    private void hideLoadingOverlay() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(false);
        }
    }
}
