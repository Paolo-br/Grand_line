package application.controller;

import application.model.GameMap;
import application.model.Island;
import application.view.BoatView;
import application.view.IslandView;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.animation.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;


public class BoatController {
    private final BoatView boatView;
    private double dragOffsetX;
    private double dragOffsetY;
    private RotateTransition rotateTransition;
    private double oldX;
    private double oldY;
    private final GameMap map;
    private Island currentDockedIsland;

    public BoatController(BoatView boatView, GameMap map) {
        this.boatView = boatView;
        this.map = map;
        initDragEvents();
    }


    private void initDragEvents() {
        boatView.setOnMousePressed(event -> {

            for (IslandView iv : map.getIslandViews()) {
                iv.setEffect(null);
            }

            // Offset entre souris et coin du bateau
            dragOffsetX = event.getX();
            dragOffsetY = event.getY();

            // Sauvegarder l'ancienne position
            oldX = boatView.getLayoutX();
            oldY = boatView.getLayoutY();

            boatView.getImageView().setFitWidth(120);
            boatView.getImageView().setFitHeight(120);

            // Appliquer un effet d'ombre pendant le drag
            boatView.setEffect(new DropShadow(25, Color.BLACK));

            // Légère mise en valeur visuelle
            boatView.setScaleX(1.1);
            boatView.setScaleY(1.1);

            // Lancer tangage
            rotateTransition = new RotateTransition(Duration.millis(400), boatView.getImageView());
            rotateTransition.setFromAngle(-5);
            rotateTransition.setToAngle(5);
            rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
            rotateTransition.setAutoReverse(true);
            rotateTransition.play();
        });

        boatView.setOnMouseDragged(event -> {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();

            var parent = boatView.getParent();
            if (parent != null) {
                var localPoint = parent.sceneToLocal(sceneX - dragOffsetX, sceneY - dragOffsetY);
                double newX = localPoint.getX();
                double newY = localPoint.getY();

                boatView.getImageView().setFitWidth(120);
                boatView.getImageView().setFitHeight(120);

                // Déplacement direct sans transition pendant le drag
                boatView.setLayoutX(newX);
                boatView.setLayoutY(newY);
            }
        });

        boatView.setOnMouseReleased(event -> {
            // Supprimer les effets visuels
            boatView.setEffect(null);
            boatView.setScaleX(1.0);
            boatView.setScaleY(1.0);

            // Revenir à l'image de base
            boatView.setBoatImage("sunny.png");

            // Stop tangage
            if (rotateTransition != null) {
                rotateTransition.stop();
                boatView.getImageView().setRotate(0);
            }

            if (boatView.getParent() instanceof Pane parent) {
                double x = boatView.getLayoutX();
                double y = boatView.getLayoutY();
                double w = boatView.getWidth()-50;
                double h = boatView.getHeight()-50;

                // détection d'île proche
                Island ileProche = null;
                double minDistance = Double.MAX_VALUE;
                double seuilProximite = 75;

                List<Island> iles = map.getIles();

                for (int i = 4; i < iles.size(); i++) { // on commence à 4
                    Island ile = iles.get(i);
                    double dx = ile.getPosition().getX() - x;
                    double dy = ile.getPosition().getY() - y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if(ile.isVisible()) {
                        if (distance < seuilProximite && distance < minDistance) {
                            minDistance = distance;
                            ileProche = ile;
                        }
                    }
                }

                if (ileProche != null) {
                    x = ileProche.getPosition().getX() - 60;
                    y = ileProche.getPosition().getY() + 5 / 2.0;
                }

                // Zones interdites
                boolean outOfBounds = x < -20|| y < -15 || x + w > parent.getWidth() + 50 || y + h > parent.getHeight() + 50;
                boolean inRedLine = (x >= 340 && x <= 470) && !(y >= 225 && y <= 425);
                boolean inWorldLeft = x >= -36 && x <= 30 && y >= 90 && y <= 500;
                boolean inWorldRight = x >= 802 && x <= 900 && y >= 132 && y <= 587;

                if (outOfBounds || inRedLine || inWorldLeft || inWorldRight) {
                    // Position invalide → retour immédiat
                    boatView.setLayoutX(oldX);
                    boatView.setLayoutY(oldY);
                    boatView.getBoat().setPosition(oldX, oldY);
                } else {
                    // Position valide → animation de transition fluide
                    double finalX = x;
                    double finalY = y;

                    // On repositionne temporairement à l'ancienne position
                    boatView.setLayoutX(oldX);
                    boatView.setLayoutY(oldY);

                    TranslateTransition tt = new TranslateTransition(Duration.millis(600), boatView.getImageView());
                    tt.setToX(finalX - oldX);
                    tt.setToY(finalY - oldY);

                    Island finalIleProche = ileProche;
                    Island finalIleProche1 = ileProche;
                    tt.setOnFinished(e -> {
                        // Corriger la vraie position de BoatView
                        boatView.setLayoutX(finalX);
                        boatView.setLayoutY(finalY);

                        // Remettre à zéro le translate de l’image
                        boatView.getImageView().setTranslateX(0);
                        boatView.getImageView().setTranslateY(0);

                        // Mettre à jour le modèle
                        boatView.getBoat().setPosition(finalX, finalY);
                        if( finalIleProche !=null){
                            boatView.getImageView().setFitWidth(70);
                            boatView.getImageView().setFitHeight(70);
                            this.currentDockedIsland = finalIleProche;
                            for (IslandView iv : map.getIslandViews()) {
                                if (iv.getIsland() == finalIleProche1) {
                                    iv.activerHalo();
                                    iv.afficherBoss();
                                    finalIleProche1.setexplored(true);
                                    break;
                                }
                            }
                        }else {
                            this.currentDockedIsland = null;
                        }
                    });


                    tt.play();
                }
            }


            System.out.println("x: " + boatView.getBoat().getPosition().getX() +
                    ", y: " + boatView.getBoat().getPosition().getY());
        });
    }

    public boolean isBoatDockedAtIsland(Island island) {
        return currentDockedIsland != null && currentDockedIsland.equals(island);
    }

}
