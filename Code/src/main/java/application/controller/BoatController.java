package application.controller;

import application.model.GameMap;
import application.model.Island;
import application.view.BoatView;
import application.view.IslandView;
import javafx.scene.layout.Pane;
import javafx.animation.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

/**
 * Contrôleur gérant l’interaction et le déplacement du bateau dans l’interface.
 * Il permet de déplacer le bateau par glisser-déposer (drag & drop),
 * gère les effets visuels liés au déplacement,
 * et détecte l’accostage à proximité des îles présentes sur la carte.
 * Ressources :
 * - Images "sunny.png" et "boat_translate.png" pour représenter le bateau.
 */
public class BoatController {
    private final BoatView boatView;
    private double dragOffsetX;    // Décalage entre position souris et coin bateau lors du drag
    private double dragOffsetY;
    private RotateTransition rotateTransition; // Animation de tangage (balancement)
    private double oldX;           // Ancienne position X avant déplacement
    private double oldY;           // Ancienne position Y avant déplacement
    private final GameMap map;     // Référence à la carte avec ses îles
    private Island currentDockedIsland; // Île sur laquelle le bateau est actuellement accosté

    /**
     * Constructeur initialisant le contrôleur et les événements drag & drop sur la vue du bateau.
     * @param boatView Vue graphique du bateau.
     * @param map Modèle contenant les îles et la carte.
     */
    public BoatController(BoatView boatView, GameMap map) {
        this.boatView = boatView;
        this.map = map;
        initDragEvents();
    }

    /**
     * Initialise les gestionnaires d’événements souris pour gérer le drag & drop du bateau.
     * Applique aussi des effets visuels pendant le drag et gère la validation de position au lâcher.
     */
    private void initDragEvents() {
        // Quand l'utilisateur presse le bouton souris sur le bateau
        boatView.setOnMousePressed(event -> {
            // Supprimer les effets d’highlight sur toutes les îles
            for (IslandView iv : map.getIslandViews()) {
                iv.setEffect(null);
            }

            // Calculer l’offset entre la position de la souris et le coin supérieur gauche du bateau
            dragOffsetX = event.getX();
            dragOffsetY = event.getY();

            // Sauvegarder l’ancienne position du bateau pour pouvoir revenir en arrière si besoin
            oldX = boatView.getLayoutX();
            oldY = boatView.getLayoutY();

            // Agrandir légèrement l’image du bateau pour indiquer le drag
            boatView.getImageView().setFitWidth(120);
            boatView.getImageView().setFitHeight(120);

            // Appliquer une ombre pendant le drag
            boatView.setEffect(new DropShadow(25, Color.BLACK));

            // Effet visuel
            boatView.setScaleX(1.1);
            boatView.setScaleY(1.1);

            // Lancer une animation de tangage (rotation avant/arrière)
            rotateTransition = new RotateTransition(Duration.millis(400), boatView.getImageView());
            rotateTransition.setFromAngle(-5);
            rotateTransition.setToAngle(5);
            rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
            rotateTransition.setAutoReverse(true);
            rotateTransition.play();
        });

        // Pendant que l’utilisateur déplace la souris avec le bouton appuyé
        boatView.setOnMouseDragged(event -> {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();

            var parent = boatView.getParent();
            if (parent != null) {
                // Convertir la position de la souris dans le repère local du parent
                var localPoint = parent.sceneToLocal(sceneX - dragOffsetX, sceneY - dragOffsetY);
                double newX = localPoint.getX();
                double newY = localPoint.getY();

                // Maintenir la taille de l’image pendant le drag
                boatView.getImageView().setFitWidth(120);
                boatView.getImageView().setFitHeight(120);

                // Déplacer directement le bateau
                boatView.setLayoutX(newX);
                boatView.setLayoutY(newY);
            }
        });

        // Lorsque l’utilisateur relâche le bouton souris
        boatView.setOnMouseReleased(event -> {
            // Supprimer les effets visuels appliqués pendant le drag
            boatView.setEffect(null);
            boatView.setScaleX(1.0);
            boatView.setScaleY(1.0);

            // Revenir à l’image de base du bateau
            boatView.setBoatImage("sunny.png");

            // Arrêter l’animation de tangage et remettre rotation à 0
            if (rotateTransition != null) {
                rotateTransition.stop();
                boatView.getImageView().setRotate(0);
            }

            if (boatView.getParent() instanceof Pane parent) {
                double x = boatView.getLayoutX();
                double y = boatView.getLayoutY();
                double w = boatView.getWidth() - 50;
                double h = boatView.getHeight() - 50;

                // Recherche d’une île proche pour accoster
                Island ileProche = getIsland(x, y);

                // Si île proche détectée, ajuster la position pour accoster précisément
                if (ileProche != null) {
                    x = ileProche.getPosition().getX() - 60;
                    y = ileProche.getPosition().getY() + 5 / 2.0;
                }

                // Vérification des zones interdites (hors carte, zones rouges, zones "world")
                boolean outOfBounds = x < -20 || y < -15 || x + w > parent.getWidth() + 50 || y + h > parent.getHeight() + 50;
                boolean inRedLine = (x >= 340 && x <= 470) && !(y >= 225 && y <= 425);
                boolean inWorldLeft = x >= -36 && x <= 30 && y >= 90 && y <= 500;
                boolean inWorldRight = x >= 802 && x <= 900 && y >= 132 && y <= 587;

                if (outOfBounds || inRedLine || inWorldLeft || inWorldRight) {
                    // Position invalide : retour immédiat à la position précédente
                    boatView.setLayoutX(oldX);
                    boatView.setLayoutY(oldY);
                    boatView.getBoat().setPosition(oldX, oldY);
                } else {
                    // Position valide : animation fluide de transition vers la nouvelle position

                    double finalX = x;
                    double finalY = y;

                    // Repositionner temporairement à l’ancienne position avant animation
                    boatView.setLayoutX(oldX);
                    boatView.setLayoutY(oldY);

                    // Changer l’image du bateau pour indiquer la translation
                    boatView.setBoatImage("boat_translate.png");

                    // Création de l’animation de translation
                    TranslateTransition tt = new TranslateTransition(Duration.millis(900), boatView.getImageView());
                    tt.setToX(finalX - oldX);
                    tt.setToY(finalY - oldY);

                    Island finalIleProche = ileProche;
                    Island finalIleProche1 = ileProche;

                    tt.setOnFinished(e -> {
                        // Mise à jour finale de la position du bateau
                        boatView.setLayoutX(finalX);
                        boatView.setLayoutY(finalY);

                        // Réinitialisation du translate appliqué à l’image
                        boatView.getImageView().setTranslateX(0);
                        boatView.getImageView().setTranslateY(0);

                        // Revenir à l’image standard
                        boatView.setBoatImage("sunny.png");

                        // Mise à jour du modèle avec la nouvelle position
                        boatView.getBoat().setPosition(finalX, finalY);

                        // Si accostage à une île, activer halo et afficher boss
                        if (finalIleProche != null) {
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
                        } else {
                            // Pas d’île accostée
                            this.currentDockedIsland = null;
                        }
                    });

                    // Lancer l’animation
                    tt.play();
                }
            }

            // Affichage console pour debug des coordonnées du bateau
            //System.out.println("x: " + boatView.getBoat().getPosition().getX() +
                    //", y: " + boatView.getBoat().getPosition().getY());
        });
    }

    /**
     * Recherche l'île la plus proche d'un point donné (x, y) dans un rayon défini.
     *
     * Parcourt la liste des îles à partir de l'indice 4, vérifie leur visibilité
     * et calcule la distance à la position donnée. Renvoie l'île la plus proche
     * dont la distance est inférieure au seuil de proximité (75).
     *
     * @param x Coordonnée X du point de référence.
     * @param y Coordonnée Y du point de référence.
     * @return L'île la plus proche dans le rayon défini, ou null si aucune île proche.
     */
    private Island getIsland(double x, double y) {
        Island ileProche = null;
        double minDistance = Double.MAX_VALUE;
        double seuilProximite = 75; // distance max pour accostage

        List<Island> iles = map.getIles();

        // Parcours des îles à partir de l’indice 4 (selon logique du jeu)
        for (int i = 4; i < iles.size(); i++) {
            Island ile = iles.get(i);
            double dx = ile.getPosition().getX() - x;
            double dy = ile.getPosition().getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (ile.isVisible() && distance < seuilProximite && distance < minDistance) {
                minDistance = distance;
                ileProche = ile;
            }
        }
        return ileProche;
    }

    /**
     * Indique si le bateau est actuellement accosté à une île donnée.
     *
     * @param island L’île à vérifier.
     * @return true si le bateau est accosté à cette île, false sinon.
     */
    public boolean isBoatDockedAtIsland(Island island) {
        return currentDockedIsland != null && currentDockedIsland.equals(island);
    }
}
