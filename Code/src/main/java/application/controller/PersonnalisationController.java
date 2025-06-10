package application.controller;

import application.model.CharacterModel;
import application.model.CharacterSelection;
import application.model.GameManager;
import application.view.MenuView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class PersonnalisationController {

    @FXML private ImageView characterView;
    @FXML private ImageView headView, bodyView, legsView;
    @FXML private Button leftHeadButton, rightHeadButton,leftBodyButton, rightBodyButton,leftLegsButton, rightLegsButton;
    @FXML private Button selectButton,randomButton;
    private CharacterModel model;
    private MenuView MenuView;
    private MenuController menuController;

    public void setControleurB(MenuController controleurB) {
        this.menuController = controleurB;
    }

    @FXML
    public void initialize() {
        model = new CharacterModel();
        updateImages();

        leftHeadButton.setOnAction(e -> {
            model.prevHead();
            updateImages();
        });

        rightHeadButton.setOnAction(e -> {
            model.nextHead();
            updateImages();
        });

        leftBodyButton.setOnAction(e -> {
            model.prevBody();
            updateImages();
        });

        rightBodyButton.setOnAction(e -> {
            model.nextBody();
            updateImages();
        });

        leftLegsButton.setOnAction(e -> {
            model.prevLegs();
            updateImages();
        });
        rightLegsButton.setOnAction(e -> {
            model.nextLegs();
            updateImages();
        });
        selectButton.setOnAction(e -> {
            // 1. Sauvegarder le skin sélectionné
            GameManager.getInstance().setSelectedCharacter(model.getCurrentSelection());

            if (menuController != null) {
                menuController.updateImages();
            }

            // 2. Fermer la fenêtre actuelle
            Stage currentStage = (Stage) selectButton.getScene().getWindow();
            currentStage.close();

            // 3. Ouvrir la fenêtre de sélection de map
            application.view.MenuView.showMapSelectionWindow(menuController); // à adapter à ton système de vue
        });
        randomButton.setOnAction(e -> {
            model.randomize();
            updateImages();
        });


    }

    private void updateImages() {
        headView.setImage(model.getHead());
        bodyView.setImage(model.getBody());
        legsView.setImage(model.getLegs());
    }
}

