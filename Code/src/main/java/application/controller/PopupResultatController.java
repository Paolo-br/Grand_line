package application.controller;

import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Contrôleur pour la fenêtre popup affichant le résultat d'un combat (victoire ou défaite).
 * Affiche une image et un message adaptés au résultat, ainsi qu'un bouton de retour.
 */
public class PopupResultatController {

    @FXML private ImageView imageResultat;
    @FXML private Label labelMessage;
    @FXML private Button btnRetour;
    @FXML private ImageView btnImage;

    private Runnable onClose;

    /**
     * Initialisation du contrôleur.
     * Configure l'image du bouton retour, ainsi que les effets visuels et
     * l'action déclenchée lors du clic sur ce bouton.
     */
    public void initialize() {
        // Set default button image
        Image retourImage = new Image("back_map.png");
        btnImage.setImage(retourImage);

        // Button scale effect
        btnRetour.setOnMousePressed(e -> {
            btnImage.setScaleX(0.95);
            btnImage.setScaleY(0.95);
        });
        btnRetour.setOnMouseReleased(e -> {
            btnImage.setScaleX(1.0);
            btnImage.setScaleY(1.0);
        });

        btnRetour.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            if (onClose != null) onClose.run();
            closeWindow();
        });
    }

    /**
     * Définit l'action à exécuter lors de la fermeture de la popup.
     *
     * @param onClose Runnable représentant l'action à exécuter
     */
    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    /**
     * Met à jour l'affichage du résultat en fonction du succès ou de l'échec.
     *
     * @param victory true si victoire, false si défaite
     */
    public void setResult(boolean victory) {
        if (victory) {
            imageResultat.setImage(new Image("victory.png"));
            labelMessage.setText("Vous avez vaincu le boss !");
        } else {
            imageResultat.setImage(new Image("defeat.png"));
            labelMessage.setText("Vous avez été vaincu par le boss.");
        }
    }

    /**
     * Ferme la fenêtre popup.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }
}
