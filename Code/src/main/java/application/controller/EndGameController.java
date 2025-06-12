package application.controller;

import application.model.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EndGameController {

    @FXML private ImageView imgResultat;
    @FXML private Button btnMenu;
    @FXML private Button btnRejouer;

    private GameController gameController;

    /**
     * Associe le contrôleur principal du jeu à ce contrôleur.
     * @param controller Instance du GameController
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }


    /**
     * Affiche l'image correspondant au résultat du jeu (victoire ou défaite).
     * Applique également un effet d'ombre blanche autour de l'image.
     * @param victoire true si victoire, false sinon
     */
    public void afficherResultat(boolean victoire) {
        String imagePath = victoire ? "/victoire.png" : "/defaite.png";
        System.out.println(imagePath);
        imgResultat.setImage(new Image(imagePath));
        DropShadow whiteShadow = new DropShadow();
        whiteShadow.setColor(Color.WHITE);
        whiteShadow.setRadius(15);
        whiteShadow.setOffsetX(0);
        whiteShadow.setOffsetY(0);
        whiteShadow.setSpread(0.5);
        imgResultat.setEffect(whiteShadow);
    }

    /**
     * Initialisation automatique après le chargement du FXML.
     * Configure l'apparence et les comportements des boutons Menu et Rejouer.
     */
    @FXML
    private void initialize() {
        Image imgMenu = new Image("btn_menu.png");
        ImageView menuView = new ImageView(imgMenu);
        menuView.setFitWidth(180); // adapte à ton design
        menuView.setFitHeight(60);
        btnMenu.setGraphic(menuView);
        btnMenu.setText(""); // supprime le texte
        btnMenu.setStyle("-fx-background-color: transparent;");


        Image imgRejouer = new Image("btn_rejouer.png");
        ImageView rejouerView = new ImageView(imgRejouer);
        rejouerView.setFitWidth(180);
        rejouerView.setFitHeight(60);
        btnRejouer.setGraphic(rejouerView);
        btnRejouer.setText("");
        btnRejouer.setStyle("-fx-background-color: transparent;");
        btnMenu.setOnMousePressed(e-> {
            menuView.setScaleX(0.95);
            menuView.setScaleY(0.95);
        });
        btnRejouer.setOnMousePressed(e-> {
            rejouerView.setScaleX(0.95);
            rejouerView.setScaleY(0.95);
        });
        btnMenu.setOnMouseReleased(e-> {
            menuView.setScaleX(1.0);
            menuView.setScaleY(1.0);
        });
        btnRejouer.setOnMouseReleased(e-> {
            rejouerView.setScaleX(1.0);
            rejouerView.setScaleY(1.0);
        });
        btnMenu.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);gameController.retourMenu();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.close();});
        btnRejouer.setOnAction(e -> {SoundManager.playClickSound("/clic.mp3", 0.8);gameController.rejouerPartie();});
    }
}
