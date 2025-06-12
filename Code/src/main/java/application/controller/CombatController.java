package application.controller;

import application.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

import java.io.IOException;

/**
 * Contrôleur gérant l'interface et la logique du combat entre le joueur et un boss.
 *
 * Ce contrôleur :
 * - Affiche les images du joueur et du boss,
 * - Gère la barre de vie du boss,
 * - Gère les interactions utilisateur (attaque, fuite),
 * - Contrôle la durée du combat avec un timer,
 * - Déclenche les événements de victoire ou défaite,
 * - Communique avec le GameController et GameState pour la progression du jeu.
 *
 * Il adapte la difficulté et la force d'attaque selon le niveau et la difficulté de la carte.
 */
public class CombatController {

    @FXML private Button btnFuite;
    @FXML private Button btnAttaque;
    @FXML private ImageView imJoueur;
    @FXML private ImageView imBoss;
    @FXML private ProgressBar barrePvBoss;
    @FXML private Label labelTimer;
    @FXML private Label labelPvInfo;
    @FXML private Label labelForceInfo;


    private Boss boss;
    private int dureeCombat; // secondes
    private int tempsRestant;
    private double forceParClic;
    private GameMap map;

    private javafx.animation.Timeline timeline;
    private float bossPvMax;
    private GameController gameController;
    private GameState gameState;
    private ImageView fuiteView;


    /**
     * Définit l'état du jeu pour accéder aux informations de progression et multiplicateurs.
     *
     * @param gameState L'état actuel du jeu.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Initialise la référence vers le contrôleur de jeu et la carte pour accéder à la difficulté.
     *
     * @param gameController Le contrôleur principal du jeu.
     * @param map La carte de jeu actuelle.
     */
    public void setGameController(GameController gameController,GameMap map) {
        this.map = map;
        this.gameController = gameController;
    }

    /**
     * Action déclenchée lors du clic sur le bouton fuite.
     * Arrête le combat, réinitialise les PV du boss et notifie le GameController.
     */
    @FXML
    private void handleFuite() {
        if (timeline != null) {
            timeline.stop();
        }
        boss.setPV(bossPvMax);
        updateBossPV(boss, bossPvMax);
        if (gameController != null) {
            gameController.quitterCombat();
        }
    }

    /**
     * Initialise l'interface du combat avec le boss donné.
     * Configure les images, la force d'attaque, les effets visuels et les timers.
     *
     * @param boss Le boss contre lequel le joueur combat.
     */
    public void setBoss(Boss boss) {
        SoundManager.playBackgroundMusic("/music_combat.mp3");
        String imagePath = boss.getView();
        forceParClic = 3*gameState.getMultiplicator()/(map.getDifficulte()-0.5);
        labelPvInfo.setText("PV Boss: " + (int)boss.getPV());
        labelForceInfo.setText("Force: " + String.format("%.2f", forceParClic));
        imBoss.setImage(new Image(imagePath+".png"));
        imJoueur.setImage(new Image (GameManager.getSelectedCharacter().getCharacterImageName()));
        // Appliquer une ombre blanche
        DropShadow whiteShadow = new DropShadow();
        whiteShadow.setColor(Color.WHITE);
        whiteShadow.setRadius(15);
        whiteShadow.setOffsetX(0);
        whiteShadow.setOffsetY(0);
        whiteShadow.setSpread(0.5);
        imBoss.setEffect(whiteShadow);
        imJoueur.setEffect(whiteShadow);

        // Initialiser la barre de vie (ProgressBar)
        barrePvBoss.setProgress(1.0); // 100%

        btnAttaque.setOnAction(e -> {
            // Si le timer n’a pas encore commencé, on le lance
            if (timeline == null) {
                startCombat();
            }

            double pvRestants = boss.getPV() - forceParClic;
            boss.setPV(Math.max(0, (float)pvRestants));
            updateBossPV(boss, bossPvMax);
            labelPvInfo.setText("PV Boss: " + (int)boss.getPV());
            labelForceInfo.setText("Force: " + String.format("%.2f", forceParClic));

            if (boss.getPV() <= 0) {
                victoire();
            }
        });

        this.boss = boss;
        this.bossPvMax = boss.getPV();

        Image imAttaque = new Image("attaque.png");
        ImageView attaqueView = new ImageView(imAttaque);
        attaqueView.setFitWidth(250);
        attaqueView.setFitHeight(250);
        btnAttaque.setGraphic(attaqueView);
        btnAttaque.setText("");
        btnAttaque.setStyle("-fx-background-color: transparent;");
        btnAttaque.setOnMousePressed(e-> {
            attaqueView.setScaleX(0.95);
            attaqueView.setScaleY(0.95);
        });
        btnAttaque.setOnMouseReleased(e-> {
            attaqueView.setScaleX(1.0);
            attaqueView.setScaleY(1.0);
        });

        Image imFuite = new Image("btn_fuite.png");
        fuiteView = new ImageView(imFuite);
        fuiteView.setFitWidth(180);
        fuiteView.setFitHeight(60);
        btnFuite.setGraphic(fuiteView);
        btnFuite.setText("");
        btnFuite.setStyle("-fx-background-color: transparent;");
        btnFuite.setOnMousePressed(e-> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            if (!btnFuite.isDisabled()) {
                fuiteView.setScaleX(0.95);
                fuiteView.setScaleY(0.95);
            }
        });
        btnFuite.setOnMouseReleased(e-> {
            fuiteView.setScaleX(1.0);
            fuiteView.setScaleY(1.0);
        });

        if (map.getDifficulte()==1){labelTimer.setText("00:30");}
        if (map.getDifficulte()==2){labelTimer.setText("00:25");}
        if (map.getDifficulte()==3){labelTimer.setText("00:20");}


    }

    /**
     * Met à jour la barre de vie du boss selon ses PV actuels.
     *
     * @param boss Le boss dont on met à jour la barre de vie.
     * @param maxPV Les points de vie maximum du boss.
     */
    public void updateBossPV(Boss boss, float maxPV) {
        float progress = boss.getPV() / maxPV;
        barrePvBoss.setProgress(progress);
    }

    /**
     * Démarre le timer principal du combat et le timer de désactivation du bouton fuite.
     * Le combat dure moins longtemps selon la difficulté de la carte.
     */
    private void startCombat() {
        dureeCombat = 35 - map.getDifficulte() * 5;
        this.tempsRestant = dureeCombat;
        labelTimer.setText("00:" + String.format("%02d", tempsRestant));

        // Timer principal du combat
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    tempsRestant--;
                    labelTimer.setText("00:" + String.format("%02d", tempsRestant));
                    if (tempsRestant <= 0) {
                        timeline.stop();
                        defaite();
                    }
                })
        );
        timeline.setCycleCount(dureeCombat);
        timeline.play();

        // Timer pour désactiver le bouton de fuite après 5 secondes
        Timeline desactivationFuite = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    btnFuite.setDisable(true);
                    fuiteView.setImage(new Image("btn_fuite_unable.png"));
                })
        );
        desactivationFuite.setCycleCount(1);
        desactivationFuite.play();
    }


    /**
     * Gestion de la victoire du joueur.
     * Met à jour le statut du boss, le niveau et multiplicateur du joueur,
     * et affiche une fenêtre popup de résultat.
     */
    private void victoire() {
        if (timeline != null) timeline.stop();
        boss.setVaincu(true);

        String nomBoss = boss.getNom();
        double bonus = 0.0;

        gameState.setLevel(gameState.getLevel() + 1);
        gameState.setMultiplicator(1 + gameState.getLevel() / 10.0 + bonus);

        boolean victoireFinale = checkVictoireFinale();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResultatCombatPopup.fxml"));
                Parent root = loader.load();

                PopupResultatController controller = loader.getController();
                controller.setResult(true); // Victoire

                controller.setOnClose(() -> {
                    gameController.quitterCombat();
                    if (victoireFinale) {
                        gameController.afficherEcranFin(true); // FIN VICTORIEUSE
                    }
                });

                Stage popupStage = new Stage();
                popupStage.setScene(new javafx.scene.Scene(root));
                popupStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    /**
     * Gestion de la défaite du joueur.
     * Diminue le nombre de vies, vérifie la défaite finale,
     * et affiche une fenêtre popup de résultat.
     */
    private void defaite() {
        if (timeline != null) timeline.stop();
        gameState.setLives(gameState.getLives() - 1);

        boolean defaiteFinale = (gameState.getLives() <= 0);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResultatCombatPopup.fxml"));
                Parent root = loader.load();

                PopupResultatController controller = loader.getController();
                controller.setResult(false); // Défaite

                controller.setOnClose(() -> {
                    gameController.quitterCombat();
                    if (defaiteFinale) {
                        gameController.afficherEcranFin(false); // FIN DÉFAITE
                    }
                });

                Stage popupStage = new Stage();
                popupStage.setScene(new javafx.scene.Scene(root));
                popupStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Vérifie si le joueur a vaincu le boss final (One Piece).
     *
     * @return true si le boss final est vaincu, false sinon.
     */
    private boolean checkVictoireFinale() {
        for (Island ile : map.getIles()) {
            if (ile.isOnePiece() && ile.getBoss() != null) {
                return ile.getBoss().isVaincu();
            }
        }
        return false;
    }

    /**
     * Accesseur du timer principal du combat.
     *
     * @return la Timeline représentant le timer principal.
     */
    public Timeline getTimeline() {
        return timeline;
    }







}
