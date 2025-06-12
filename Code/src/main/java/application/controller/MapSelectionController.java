package application.controller;

import application.model.GameManager;
import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Random;

public class MapSelectionController {

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

        selectMapButton.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            //on sauvegarde nos map choisi dans le gameManager
            GameManager.getInstance().setSelectedMapIndex(currentIndex);

            String selectedMap = mapImages[currentIndex];
            System.out.println("Carte sélectionnée : " + selectedMap);
            menuController.updateMap(selectedMap);
            ((Stage) selectMapButton.getScene().getWindow()).close();
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
}
