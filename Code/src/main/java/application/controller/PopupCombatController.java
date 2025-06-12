package application.controller;

import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Contrôleur pour la fenêtre popup de confirmation de combat.
 * Ce popup demande à l'utilisateur s'il souhaite engager le combat contre un boss,
 * avec les boutons "Oui" et "Non" et l'affichage de l'image du boss concerné.
 */
public class PopupCombatController {

    @FXML private Button btnOui;
    @FXML private Button btnNon;
    @FXML private ImageView bossImage;

    private Runnable onAccept;

    /**
     * Initialisation du contrôleur : configure les boutons "Oui" et "Non"
     * avec leurs images, styles et animations de clic.
     * Configure aussi les actions associées aux boutons.
     */
    public void initialize() {
        Image imOui = new Image("oui.png");
        ImageView ouiView = new ImageView(imOui);
        ouiView.setFitWidth(180);
        ouiView.setFitHeight(60);
        btnOui.setGraphic(ouiView);
        btnOui.setText("");
        btnOui.setStyle("-fx-background-color: transparent;");
        btnOui.setOnMousePressed(e-> {
            ouiView.setScaleX(0.95);
            ouiView.setScaleY(0.95);
        });
        btnOui.setOnMouseReleased(e-> {
            ouiView.setScaleX(1.0);
            ouiView.setScaleY(1.0);
        });

        Image imNon = new Image("non.png");
        ImageView nonView = new ImageView(imNon);
        nonView.setFitWidth(180);
        nonView.setFitHeight(60);
        btnNon.setGraphic(nonView);
        btnNon.setText("");
        btnNon.setStyle("-fx-background-color: transparent;");
        btnNon.setOnMousePressed(e-> {
            nonView.setScaleX(0.95);
            nonView.setScaleY(0.95);
        });
        btnNon.setOnMouseReleased(e-> {
            nonView.setScaleX(1.0);
            nonView.setScaleY(1.0);
        });

        btnOui.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            if (onAccept != null) onAccept.run();
            closeWindow();
        });
        btnNon.setOnAction(e -> {SoundManager.playClickSound("/clic.mp3", 0.8);closeWindow();});
    }


    /**
     * Ferme la fenêtre popup en récupérant la fenêtre actuelle depuis un des boutons.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnOui.getScene().getWindow();
        stage.close();
    }

    /**
     * Définit l'action à exécuter lorsque l'utilisateur accepte le combat (bouton Oui).
     *
     * @param onAccept Runnable représentant l'action à exécuter
     */
    public void setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
    }

    /**
     * Définit l'image du boss à afficher dans le popup.
     *
     * @param imagePath Chemin relatif ou absolu vers l'image du boss
     */
    public void setBossImage(String imagePath) {
        bossImage.setImage(new javafx.scene.image.Image(imagePath));
    }
}
