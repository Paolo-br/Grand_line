package application.controller;

import application.model.CharacterModel;
import application.model.GameManager;
import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//Controller qui gere la fenetre de personnalisation du personnage
// les fleches, le changement de personnnage, le bouton aleatoire...
// il est lié a un menuController pour pouvoir afficher le personnage choisi
// quand le bouton selectionner est appuyé

public class PersonnalisationController {

    @FXML private ImageView headView, bodyView, legsView;
    @FXML private Button leftHeadButton, rightHeadButton,leftBodyButton, rightBodyButton,leftLegsButton, rightLegsButton;
    @FXML private Button selectButton,randomButton;
    private CharacterModel model;
    private MenuController menuController;

    // permet de recuperer le bon menu controller
    public void setControleurB(MenuController controleurB) {
        this.menuController = controleurB;
    }

    // pour initialiser tous les boutons et leurs actions
    @FXML
    public void initialize() {
        setupBouton(leftHeadButton, "/fleche_gauche.png");
        setupBouton(rightHeadButton, "/fleche_droite.png");
        setupBouton(leftBodyButton, "/fleche_gauche.png");
        setupBouton(rightBodyButton, "/fleche_droite.png");
        setupBouton(leftLegsButton, "/fleche_gauche.png");
        setupBouton(rightLegsButton, "/fleche_droite.png");


        model = new CharacterModel();
        updateImages();

        leftHeadButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.prevHead();
            updateImages();
        });

        rightHeadButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.nextHead();
            updateImages();
        });

        leftBodyButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.prevBody();
            updateImages();
        });

        rightBodyButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.nextBody();
            updateImages();
        });

        leftLegsButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.prevLegs();
            updateImages();
        });
        rightLegsButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.nextLegs();
            updateImages();
        });
        Image imgSelect = new Image("btn_selec.png");
        ImageView selecView = new ImageView(imgSelect);
        selecView.setFitWidth(180);
        selecView.setFitHeight(60);
        selectButton.setGraphic(selecView);
        selectButton.setText("");
        selectButton.setStyle("-fx-background-color: transparent;");
        selectButton.setOnMousePressed(e-> {
            selecView.setScaleX(0.95);
            selecView.setScaleY(0.95);
        });
        selectButton.setOnMouseReleased(e-> {
            selecView.setScaleX(1.0);
            selecView.setScaleY(1.0);
        });
        selectButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            // sauvegarder le skin sélectionné
            GameManager.getInstance().setSelectedCharacter(model.getCurrentSelection());

            if (menuController != null) {
                menuController.updateImages();
            }

            // fermer la fenêtre actuelle
            Stage currentStage = (Stage) selectButton.getScene().getWindow();
            currentStage.close();

            // ouvrir la fenêtre de sélection de map
            application.view.MenuView.showMapSelectionWindow(menuController);
        });
        Image imgRandom = new Image("btn_alea.png");
        ImageView randomView = new ImageView(imgRandom);
        randomView.setFitWidth(180);
        randomView.setFitHeight(60);
        randomButton.setGraphic(randomView);
        randomButton.setText("");
        randomButton.setStyle("-fx-background-color: transparent;");
        randomButton.setOnMousePressed(e-> {
            randomView.setScaleX(0.95);
            randomView.setScaleY(0.95);
        });
        randomButton.setOnMouseReleased(e-> {
            randomView.setScaleX(1.0);
            randomView.setScaleY(1.0);
        });
        randomButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            model.randomize();
            updateImages();
        });


    }

    // pour reafficher le  bon personnage apres modification
    private void updateImages() {
        headView.setImage(model.getHead());
        bodyView.setImage(model.getBody());
        legsView.setImage(model.getLegs());
    }

    /**
     * Configure un bouton avec une image et des effets visuels au clic.
     *
     * @param button Le bouton à configurer
     * @param imageNormal Le chemin de l'image à charger pour le bouton
     */
    private void setupBouton(Button button, String imageNormal) {
        if (imageNormal != null) {
            var url = getClass().getResource(imageNormal); // <-- chercher l'image dans le classpath
            if (url == null) {
                System.err.println("Image non trouvée : " + imageNormal);
                return;
            }
            Image img = new Image(url.toExternalForm()); // <-- charger avec URL externe
            ImageView view = new ImageView(img);
            view.setFitWidth(45);
            view.setFitHeight(90);
            button.setGraphic(view);
            button.setText("");
            button.setStyle("-fx-background-color: transparent;");

            button.setOnMousePressed(e -> {
                view.setScaleX(0.95);
                view.setScaleY(0.95);
            });

            button.setOnMouseReleased(e -> {
                view.setScaleX(1.0);
                view.setScaleY(1.0);
            });
        }
    }
}

