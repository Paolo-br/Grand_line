package application.controller;

import application.model.GameManager;
import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Random;

//controller pour la fenetre de selection de map
// fonctionne aussi avec des indexs pour choisir la map

public class  MapSelectionController {

    @FXML private ImageView mapImageView;
    @FXML private Button leftMapButton, rightMapButton, selectMapButton,randomMapButton;

    private final String[] mapImages = { "map1.png", "map2.png", "map3.png","map4.png","map5.png","map6.png" };
    private int currentIndex = 0;
    private MenuController menuController;

    public void setControleurB(MenuController controleurB) {
        this.menuController = controleurB;
    }

    @FXML
    public void initialize() {
        updateMap();
        setupBouton(leftMapButton, "/fleche_gauche.png");
        setupBouton(rightMapButton, "/fleche_droite.png");
        leftMapButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            currentIndex = (currentIndex - 1 + mapImages.length) % mapImages.length;
            updateMap();
        });

        rightMapButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            currentIndex = (currentIndex + 1) % mapImages.length;
            updateMap();
        });

        Image imgSelect = new Image("btn_selec.png");
        ImageView selecView = new ImageView(imgSelect);
        selecView.setFitWidth(180);
        selecView.setFitHeight(60);
        selectMapButton.setGraphic(selecView);
        selectMapButton.setText("");
        selectMapButton.setStyle("-fx-background-color: transparent;");
        selectMapButton.setOnMousePressed(e-> {
            selecView.setScaleX(0.95);
            selecView.setScaleY(0.95);
        });
        selectMapButton.setOnMouseReleased(e-> {
            selecView.setScaleX(1.0);
            selecView.setScaleY(1.0);
        });
        selectMapButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            //on sauvegarde nos map choisi dans le gameManager
            GameManager.getInstance().setSelectedMapIndex(currentIndex);

            String selectedMap = mapImages[currentIndex];
            //System.out.println("Carte sélectionnée : " + selectedMap);
            menuController.updateMap(selectedMap);
            ((Stage) selectMapButton.getScene().getWindow()).close();
        });

        Image imgRandom = new Image("btn_alea.png");
        ImageView randomView = new ImageView(imgRandom);
        randomView.setFitWidth(180);
        randomView.setFitHeight(60);
        randomMapButton.setGraphic(randomView);
        randomMapButton.setText("");
        randomMapButton.setStyle("-fx-background-color: transparent;");
        randomMapButton.setOnMousePressed(e-> {
            randomView.setScaleX(0.95);
            randomView.setScaleY(0.95);
        });
        randomMapButton.setOnMouseReleased(e-> {
            randomView.setScaleX(1.0);
            randomView.setScaleY(1.0);
        });
        randomMapButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            currentIndex = new Random().nextInt(mapImages.length);
            updateMap();
        });
    }

    private void updateMap() {
        mapImageView.setImage(new Image(mapImages[currentIndex]));
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
