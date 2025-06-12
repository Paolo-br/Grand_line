package application.controller;

import application.model.*;
import application.view.*;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

/**
 * Contrôleur principal du jeu.
 * Gère l'affichage de la carte, le bateau, les combats, les interactions utilisateur,
 * ainsi que la gestion de l'état du jeu (vies, niveau, multiplicateur).
 */

public class GameController {

    @FXML private Pane cartePane;
    @FXML private HBox heartsBox;
    @FXML private Label levelLabel;
    @FXML private Label multiplicatorValueLabel;
    @FXML private Label toastLabel;
    @FXML private Button btnQuitter;


    private Boat boat;
    private BoatView boatView;
    private BoatController boatController;
    private GameMap map;
    private final GameState gameState = new GameState();
    private Boss currentBossCombat;
    private CombatController currentFight;


    /**
     * Affiche toutes les îles sur la carte.
     */
    public void afficherIles() {
        cartePane.getChildren().clear();
        for (IslandView vue : map.getIslandViews()) {
            cartePane.getChildren().add(vue);
        }
    }

    /**
     * Met à jour l'affichage des vies sous forme de coeurs dans la barre de vies.
     *
     * @param lives Nombre de vies à afficher.
     */
    private void updateHearts(int lives) {
        heartsBox.getChildren().clear();
        for (int i = 0; i < lives; i++) {
            ImageView heart = new ImageView(new Image("coeur.png"));
            heart.setFitWidth(19);
            heart.setFitHeight(19);
            heart.setPreserveRatio(true);
            heart.setPickOnBounds(true);
            heart.setEffect(new DropShadow(10, Color.BLACK));
            heartsBox.getChildren().add(heart);
        }
    }

    /**
     * Active la possibilité de déposer le bateau sur la carte via drag & drop.
     */
    private void enableCarteDrop() {
        // Gestion du drag over : on accepte uniquement le drag provenant du bateau
        cartePane.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof BoatView &&
                    event.getDragboard().hasString() &&
                    event.getDragboard().getString().equals("BOAT")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Gestion du drop : positionne le bateau sur la position déposée
        cartePane.setOnDragDropped(event -> {
            double x = event.getX() - boatView.getWidth() / 2;
            double y = event.getY() - boatView.getHeight() / 2;


            boat.setPosition(x, y);


            boatView.updatePositionFromModel();

            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * Initialisation du contrôleur (appelée automatiquement après chargement FXML).
     * Configure la carte, le bateau, les boutons et les liaisons avec l'état du jeu.
     */
    @FXML
    public void initialize() {

        int mapImageName = GameManager.getInstance().getSelectedMapIndex()+1;
        //System.out.println(mapImageName);

        // Création et application du fond de carte avec l'image correspondant à la sélection
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("/map"+mapImageName+".png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Appliquer en fond
        cartePane.setBackground(new Background(backgroundImage));

        // Initialisation de la carte du jeu selon la difficulté sélectionnée
        map = new GameMap(GameManager.getInstance().getSelectedDifficulty(), this); // exemple difficulté 1
        afficherIles();
        cartePane.getChildren().add(toastLabel);

        // Initialiser le bateau
        boat=new Boat(new Position(53,346));
        boatView = new BoatView(boat);
        cartePane.getChildren().add(boatView);

        // Contrôleur
        boatController = new BoatController(boatView, map);
        enableCarteDrop();
        // Ajout des vues des boss sur la carte
        for (BossView bossView : map.getBossViews()) {
            cartePane.getChildren().add(bossView);
        }

        // Initialisation de l'affichage des vies et mise à jour automatique au changement
        updateHearts(gameState.getLives());
        gameState.livesProperty().addListener((obs, oldVal, newVal) -> {
            updateHearts(newVal.intValue());
        });

        // Lier le texte des labels aux propriétés de GameState
        levelLabel.textProperty().bind(
                gameState.levelProperty().asString("Niveau : %d")
        );

        multiplicatorValueLabel.textProperty().bind(
                Bindings.format("x%.1f", gameState.multiplicatorProperty())
        );

        btnQuitter.setOnAction(e -> {
            SoundManager.playClickSound("/clic.mp3", 0.8);
            quitterPartie();
            Stage stage = (Stage) btnQuitter.getScene().getWindow();
            stage.close();
        });
        Image imgQuitter = new Image("btn_leave.png");
        ImageView quitterView = new ImageView(imgQuitter);
        quitterView.setFitWidth(180);
        quitterView.setFitHeight(60);
        btnQuitter.setGraphic(quitterView);
        btnQuitter.setText("");
        btnQuitter.setStyle("-fx-background-color: transparent;");
        btnQuitter.setOnMousePressed(e-> {
            quitterView.setScaleX(0.95);
            quitterView.setScaleY(0.95);
        });
        btnQuitter.setOnMouseReleased(e-> {
            quitterView.setScaleX(1.0);
            quitterView.setScaleY(1.0);
        });
       SoundManager.playBackgroundMusic("/music_map.mp3");
    }

    /**
     * Lance un combat contre un boss donné.
     * Charge la vue du combat et initialise son contrôleur.
     *
     * @param boss Boss contre lequel lancer le combat.
     */
    public void lancerCombat(Boss boss) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CombatView.fxml"));
            Parent combatRoot = loader.load();

            // Lier le controller CombatController avec le GameController
            CombatController combatController = loader.getController();
            combatController.setGameController(this,map);
            combatController.setGameState(gameState);
            combatController.setBoss(boss);
            this.currentBossCombat = boss;
            this.currentFight = combatController;

            // Ajouter la vue combat par-dessus la carte
            cartePane.getChildren().add(combatRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Quitte la partie en cours.
     * Stoppe le combat si en cours et revient au menu principal.
     */
    public void quitterPartie() {
        if (currentFight != null && currentFight.getTimeline() != null) {
            currentFight.getTimeline().stop();
        }

        SoundManager.stopBackgroundMusic();
        try {
            FXMLLoader loader = new FXMLLoader(MenuView.class.getResource("/main_menu.fxml")); // adapte le chemin
            Parent root = loader.load();
            Scene scene = new Scene(root,1000,700);
            Stage stage = new Stage();
            stage.setTitle("Menu Principal");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Quitte l'écran de combat et retourne à la carte.
     * Met à jour la visibilité des îles et affiche un drapeau de victoire si besoin.
     */
    public void quitterCombat() {
        SoundManager.stopBackgroundMusic();
        SoundManager.playBackgroundMusic("/music_map.mp3");
        // Enlève le dernier enfant ajouté (la vue de combat)
        if (!cartePane.getChildren().isEmpty()) {
            Node last = cartePane.getChildren().get(cartePane.getChildren().size() - 1);
            cartePane.getChildren().remove(last);
        }
        for (IslandView iv : map.getIslandViews()) {
            if (iv.getIsland().getBoss() == currentBossCombat && currentBossCombat.isVaincu()) {
                Island island = iv.getIsland();
                island.setexplored(true);
                iv.getBossView().updateVisibility(); // cache dynamiquement

                if (iv.getBossView().getParent() != null) {
                    double x = iv.getBossView().getLayoutX();
                    double y = iv.getBossView().getLayoutY();
                    VictoryFlagView flag = new VictoryFlagView(x, y);
                    cartePane.getChildren().add(flag);
                }

                List<Island> voisines = trouverIlesLesPlusProches(island, 2);
                for (Island proche : voisines) {
                    if (!proche.isVisible()) {
                        proche.setVisible(true);
                        for (IslandView v : map.getIslandViews()) {
                            if (v.getIsland() == proche) {
                                v.updateVisibility();
                                break;
                            }
                        }
                    }
                }
                Island end = map.getOnepiece();

                //Vérifier que TOUS les boss sont vaincus, sauf celui de end
                boolean tousBossVaincus = true;
                for (Island ile : map.getIles()) {
                    if (ile != end && ile.getBoss() != null && !ile.getBoss().isVaincu()) {
                        tousBossVaincus = false;
                        break;
                    }
                }

                // Si tous sont vaincus, on rend visible l'île end
                if (tousBossVaincus && !end.isVisible()) {
                    end.setVisible(true);
                    for (IslandView v : map.getIslandViews()) {
                        if (v.getIsland() == end) {
                            v.updateVisibility();
                            break;
                        }
                    }
                }


                break;
            }
        }
    }

    /**
     * Affiche l'écran de fin de jeu avec le résultat (victoire ou défaite).
     *
     * @param victoire true si victoire, false sinon.
     */
    public void afficherEcranFin(boolean victoire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EndGameView.fxml"));
            Parent overlay = loader.load();

            EndGameController controller = loader.getController();
            controller.setGameController(this);
            controller.afficherResultat(victoire);

            // Superposer à la carte
            cartePane.getChildren().add(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuView.class.getResource("/main_menu.fxml")); // adapte le chemin
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Menu Principal");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Relance une nouvelle partie en réinitialisant complètement l'état du jeu et l'affichage.
     * Cette méthode remet les vies, le niveau et le multiplicateur à leurs valeurs initiales,
     * reconstruit la carte et le bateau, puis remet à jour les éléments graphiques.
     */
    public void rejouerPartie() {
            System.out.println("Nouvelle Partie créée");

            // 1. Réinitialiser GameState
            gameState.setLives(5);
            gameState.setLevel(1);
            gameState.setMultiplicator(1);

            // 2. Supprimer tout le contenu de la carte
            cartePane.getChildren().clear();

            cartePane.getChildren().add(toastLabel);

            // 3. Regénérer la map
            map = new GameMap(map.getDifficulte(), this);
            afficherIles();

            // 4. Recréer le bateau
            boat = new Boat(new Position(53, 346));
            boatView = new BoatView(boat);
            cartePane.getChildren().add(boatView);

            // 5. Nouveau contrôleur bateau
            boatController = new BoatController(boatView, map);
            enableCarteDrop();

            // 6. Afficher tous les bossView
            for (BossView bossView : map.getBossViews()) {
                cartePane.getChildren().add(bossView);
            }

            // 7. Mettre à jour les cœurs
            updateHearts(gameState.getLives());
    }


    /**
     * Indique si le bateau est amarré (docké) à une île spécifique.
     * Délègue la vérification au contrôleur du bateau.
     *
     * @param island L'île à vérifier
     * @return true si le bateau est amarré à cette île, false sinon
     */
    public boolean isBoatDockedAtIsland(Island island) {
        return boatController.isBoatDockedAtIsland(island);
    }


    /**
     * Affiche un message toast temporaire.
     *
     * @param message Message à afficher.
     */

    public void showToast(String message, BossView bossView) {
        toastLabel.setText(message);
        toastLabel.setOpacity(1.0); // visible immédiatement
        toastLabel.toFront();
        toastLabel.setVisible(true);

        // Assurer le layout correct
        toastLabel.applyCss();
        toastLabel.layout();

        double bossCenterX = bossView.localToScene(bossView.getBoundsInLocal()).getMinX()
                + bossView.getBoundsInLocal().getWidth() / 2;
        double bossTopY = bossView.localToScene(bossView.getBoundsInLocal()).getMinY();

        double labelX = cartePane.sceneToLocal(bossCenterX, bossTopY).getX();
        double labelY = cartePane.sceneToLocal(bossCenterX, bossTopY).getY();

        toastLabel.setLayoutX(labelX - toastLabel.getWidth() / 2);
        toastLabel.setLayoutY(labelY - 30);

        // Affiche pendant 3 secondes puis disparait
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {toastLabel.setOpacity(0.0);
            toastLabel.setVisible(false);
        });
        pause.play();
    }

    /**
     * Trouve les îles les plus proches d'une île donnée.
     *
     * @param centre Ile de référence.
     * @param nombre Nombre maximum d'îles proches à retourner.
     * @return Liste des îles les plus proches.
     */
    private List<Island> trouverIlesLesPlusProches(Island centre, int nombre) {
        List<Island> ilesFiltrees = new ArrayList<>();
        Island end = map.getOnepiece();
        for (Island i : map.getIles()) {
            if (i != centre && !i.isVisible()&& i != end) {
                ilesFiltrees.add(i);
            }
        }

        ilesFiltrees.sort(Comparator.comparingDouble(i -> i.getPosition().distanceTo(centre.getPosition())));

        List<Island> ilesLesPlusProches = new ArrayList<>();
        int limit = Math.min(nombre, ilesFiltrees.size());
        for (int i = 0; i < limit; i++) {
            ilesLesPlusProches.add(ilesFiltrees.get(i));
        }

        return ilesLesPlusProches;
    }

}

