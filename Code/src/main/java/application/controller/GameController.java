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


    public void afficherIles(List<Island> iles) {
        cartePane.getChildren().clear();
        for (IslandView vue : map.getIslandViews()) {
            cartePane.getChildren().add(vue);
        }
    }

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

    private void enableCarteDrop() {
        cartePane.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof BoatView &&
                    event.getDragboard().hasString() &&
                    event.getDragboard().getString().equals("BOAT")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cartePane.setOnDragDropped(event -> {
            double x = event.getX() - boatView.getWidth() / 2;
            double y = event.getY() - boatView.getHeight() / 2;


            boat.setPosition(x, y);


            boatView.updatePositionFromModel();

            event.setDropCompleted(true);
            event.consume();
        });
    }


    @FXML
    public void initialize() {
        map = new GameMap(GameManager.getInstance().getSelectedDifficulty(), this); // exemple difficulté 1
        afficherIles(map.getIles());
        cartePane.getChildren().add(toastLabel);


        // Initialiser le bateau
        boat=new Boat(new Position(53,346));
        boatView = new BoatView(boat);
        cartePane.getChildren().add(boatView);

        // Contrôleur
        boatController = new BoatController(boatView, map);
        enableCarteDrop();
        for (BossView bossView : map.getBossViews()) {
            cartePane.getChildren().add(bossView);
        }


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


        // Affichage des coeurs
        updateHearts(gameState.getLives());
        gameState.livesProperty().addListener((obs, oldVal, newVal) -> {
            updateHearts(newVal.intValue());
        });

        btnQuitter.setOnAction(e -> {
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
    }

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

            // Ajouter la vue combat par-dessus la carte
            cartePane.getChildren().add(combatRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quitterPartie() {
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


    public void quitterCombat() {

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

    public void rejouerPartie() {
            System.out.println("Nouvelle Partie créée");

            // 1. Réinitialiser GameState
            gameState.setLives(9);
            gameState.setLevel(1);
            gameState.setMultiplicator(1);

            // 2. Supprimer tout le contenu de la carte
            cartePane.getChildren().clear();

            cartePane.getChildren().add(toastLabel);

            // 3. Regénérer la map
            map = new GameMap(map.getDifficulte(), this);
            afficherIles(map.getIles());

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

    public boolean isBoatDockedAtIsland(Island island) {
        return boatController.isBoatDockedAtIsland(island);
    }

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

        // Affiche pendant 3 secondes puis disparait sans animation
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {toastLabel.setOpacity(0.0);
            toastLabel.setVisible(false);
        });
        pause.play();
    }

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

