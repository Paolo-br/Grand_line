package application.controller;

import application.model.Boss;
import application.model.Island;
import application.model.SoundManager;
import application.view.BossView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Contrôleur pour gérer les interactions avec un boss sur une île dans la carte du jeu.
 * Cette classe gère la vue du boss (BossView), la détection des clics sur l'image,
 * et affiche une fenêtre popup pour lancer un combat lorsque les conditions sont remplies.
 * Elle communique avec le GameController pour vérifier si le bateau est accosté à l'île
 * et pour lancer le combat avec le boss.
 *
 * Références :
 * - /CombatPopup.fxml : interface utilisateur pour la fenêtre popup de combat.
 * - SoundManager : gestion des sons dans l'application.
 */
public class BossController {
    private final Island island;
    private final BossView bossView;
    private GameController gameController;

    /**
     * Constructeur du contrôleur BossController.
     * Initialise le gestionnaire d'événements sur la vue du boss.
     *
     * @param island L'île sur laquelle le boss est positionné.
     * @param bossView La vue graphique associée au boss.
     */
    public BossController(Island island, BossView bossView) {
        this.island = island;
        this.bossView = bossView;

        initClickListener();
    }

    /**
     * Initialise le listener de clic sur la vue du boss.
     *
     * Lors d'un clic, joue un son puis :
     * - Si l'île est visible, le boss non vaincu, et le bateau accosté à l'île,
     *   affiche la popup de combat.
     * - Sinon, affiche un message indiquant que le joueur doit accoster l'île.
     */
    private void initClickListener() {
        bossView.setOnMouseClicked(event -> {
            SoundManager.playClickSound("/music_boss.mp3", 0.8);
            if (island.isVisible() && !island.getBoss().isVaincu() && gameController != null && gameController.isBoatDockedAtIsland(island)) {
                showCombatPopup();
            } else if (gameController != null) {
                gameController.showToast("Vous devez accoster l'île pour affronter le boss.", bossView);
            }
        });
    }

    /**
     * Affiche une fenêtre popup permettant de lancer un combat contre le boss.
     * Charge l'interface depuis le fichier FXML, configure l'image du boss,
     * et gère la validation du combat via un callback.
     */
    private void showCombatPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CombatPopup.fxml"));
            Parent root = loader.load();

            PopupCombatController popupController = loader.getController();
            popupController.setBossImage(island.getBoss().getView() + ".png");

            popupController.setOnAccept(() -> {
                if (gameController != null) {
                    gameController.lancerCombat(island.getBoss());
                } else {
                    System.err.println("GameController non défini dans BossController !");
                }
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Affronter le Boss");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Définit le GameController associé à ce BossController.
     *
     * @param gameController Instance de GameController pour interagir avec le jeu.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
