package application.controller;

import application.model.SoundManager;
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

/**
 * Contrôleur de la fenêtre affichant les règles du jeu.
 * Permet de naviguer entre plusieurs images explicatives avec des boutons "gauche" et "droite",
 * et de revenir au menu principal via un bouton "retour".
 */
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

    /**
     * Initialisation du contrôleur.
     * Configure les boutons avec leurs images, lie les actions aux clics et affiche la première image.
     */
    @FXML
    private void initialize() {
        setupBouton(btnRetour, "/retour_menu.png");
        setupBouton(btnGauche, "/fleche_gauche.png");
        setupBouton(btnDroite, "/fleche_droite.png");

        afficherImageCourante();

        btnGauche.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);afficherImagePrecedente();});
        btnDroite.setOnAction(e -> {SoundManager.playClickSound("/clic.mp3", 0.8);afficherImageSuivante();});
        btnRetour.setOnAction(e -> {SoundManager.playClickSound("/clic.mp3", 0.8);retourMenu();});
    }

    /**
     * Affiche l'image correspondant à l'index courant dans l'ImageView principale.
     * Affiche un message d'erreur si l'image n'est pas trouvée.
     */
    private void afficherImageCourante() {
        var url = getClass().getResource(images.get(indexImage));
        if (url == null) {
            System.err.println("Image non trouvée : " + images.get(indexImage));
            return;
        }
        Image image = new Image(url.toExternalForm());
        imagePrincipale.setImage(image);
    }


    /**
     * Affiche l'image suivante si possible (index inférieur à la taille de la liste).
     */
    private void afficherImageSuivante() {
        if (indexImage < images.size() - 1) {
            indexImage++;
            afficherImageCourante();
        }
    }

    /**
     * Affiche l'image précédente si possible (index supérieur à 0).
     */
    private void afficherImagePrecedente() {
        if (indexImage > 0) {
            indexImage--;
            afficherImageCourante();
        }
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

    /**
     * Ferme la fenêtre actuelle et ouvre le menu principal.
     */
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
