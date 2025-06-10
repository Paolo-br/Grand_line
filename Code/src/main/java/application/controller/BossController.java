package application.controller;

import application.model.Boss;
import application.model.Island;
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

public class BossController {
    private final Island island;
    private final BossView bossView; // image visible sur la map
    private GameController gameController;

    public BossController(Island island, BossView bossView) {
        this.island = island;
        this.bossView = bossView;

        initClickListener();
    }

    private void initClickListener() {
        bossView.setOnMouseClicked(event -> {
            if (island.isVisible() && !island.getBoss().isVaincu() && gameController != null && gameController.isBoatDockedAtIsland(island)) {
                showCombatPopup();
            }else if (gameController != null) {
                gameController.showToast( "Vous devez accoster l'île pour affronter le boss.",bossView);
            }
        });
    }

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



    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}