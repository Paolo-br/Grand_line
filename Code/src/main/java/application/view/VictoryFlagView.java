package application.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * La classe {@code VictoryFlagView} représente graphiquement un drapeau de victoire sur la carte.
 * Elle est affichée à une position fixe lorsque le joueur bat un boss
 * Ressources utilisées :
 * - Image "flag.png"
 */
public class VictoryFlagView extends StackPane {

    /**
     * Constructeur du drapeau de victoire.
     *
     * @param x Coordonnée horizontale sur la carte.
     * @param y Coordonnée verticale sur la carte.
     */
    public VictoryFlagView(double x, double y) {
        // Chargement de l’image du drapeau
        ImageView flag = new ImageView(new Image("flag.png"));

        // Définition de la taille du drapeau
        flag.setFitWidth(55);
        flag.setFitHeight(55);

        // Ajout de l’image à la pile (StackPane)
        getChildren().add(flag);

        // Positionnement du drapeau sur la carte
        setLayoutX(x);
        setLayoutY(y);

        // Rend l’élément insensible aux clics souris
        setMouseTransparent(true);
    }
}
