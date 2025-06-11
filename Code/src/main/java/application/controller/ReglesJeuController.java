package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

public class ReglesJeuController {

    @FXML private Button btnRetour;
    @FXML private Button btnGauche;
    @FXML private Button btnDroite;
    @FXML private ImageView imagePrincipale;

    private final List<String> images = List.of(
            "/regle1.png",
            "/regle2.png",
            "/regle3.png",
            "/regle4.png"

    );

    private int indexImage = 0;

    @FXML
    private void initialize() {
        setupBouton(btnRetour, "/retour_menu.png");
        setupBouton(btnGauche, "/fleche_gauche.png");
        setupBouton(btnDroite, "/fleche_droite.png");

        afficherImageCourante();

        btnGauche.setOnAction(e -> afficherImagePrecedente());
        btnDroite.setOnAction(e -> afficherImageSuivante());
        btnRetour.setOnAction(e -> retourMenu());
    }

    private void afficherImageCourante() {
        var url = getClass().getResource(images.get(indexImage));
        if (url == null) {
            System.err.println("Image non trouvée : " + images.get(indexImage));
            return;
        }
        Image image = new Image(url.toExternalForm());
        imagePrincipale.setImage(image);
    }


    private void afficherImageSuivante() {
        if (indexImage < images.size() - 1) {
            indexImage++;
            afficherImageCourante();
        }
    }

    private void afficherImagePrecedente() {
        if (indexImage > 0) {
            indexImage--;
            afficherImageCourante();
        }
    }

    private void setupBouton(Button button, String imageNormal) {
        if (imageNormal != null) {
            var url = getClass().getResource(imageNormal); // <-- chercher l'image dans le classpath
            if (url == null) {
                System.err.println("Image non trouvée : " + imageNormal);
                return;
            }
            Image img = new Image(url.toExternalForm()); // <-- charger avec URL externe
            ImageView view = new ImageView(img);
            view.setFitWidth(90);
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

    private void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnRetour.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
