package application.controller;

import application.model.CharacterModel;
import application.model.CharacterSelection;
import application.model.GameManager;
import application.model.SoundManager;
import application.view.MenuView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.InputStream;

public class MenuController {

    public ImageView easyImageView,mediumImageView,hardImageView;

    @FXML private ImageView characterView,mapView; //Personnalisation
    private CharacterModel model;

    @FXML private Button buttonPersonnalisation, buttonPlay, buttonRules;
    @FXML private RadioButton easyRadio;
    @FXML private RadioButton mediumRadio;
    @FXML private RadioButton hardRadio;
    @FXML private ToggleGroup difficultyGroup;


    @FXML
    public void initialize() {
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
            System.out.println(GameManager.getInstance().getSelectedDifficulty());

            SoundManager.playClickSound("/clic.mp3", 0.8);
            MenuView.showPlayWindow();
            Stage stage = (Stage) buttonPlay.getScene().getWindow();
            stage.close();
        });
        buttonRules.setOnAction(e -> {
            MenuView.showRulesWindow();
            SoundManager.playClickSound("/clic.mp3", 0.8);
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
    public void updateMap(String map){
        Image imageMap = new Image("/" + map);
        mapView.setImage(imageMap);
    }
    public int getSelectedDifficulty() {
        int difficulty;
        RadioButton selected = (RadioButton) difficultyGroup.getSelectedToggle();
        if (selected.getText()=="Facile"){
            difficulty=1;
        } else if (selected.getText()=="Moyen") {
            difficulty=2;
        }else{
            difficulty=3;
        }
        return difficulty;
    }



}
