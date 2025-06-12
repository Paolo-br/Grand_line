package application.view;

import application.model.Boat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * La classe {@code BoatView} représente la vue graphique d'un bateau dans l'application JavaFX.
 * Elle affiche l'image du bateau et synchronise sa position avec le modèle {@link Boat}.
 * Ressource utilisée :
 * - Image : "sunny.png" (doit être présente dans les ressources du projet)
 */
public class BoatView extends StackPane {

    // Référence au modèle Boat lié à cette vue
    private final Boat boat;

    // ImageView pour afficher l’image du bateau
    private final ImageView imageView;

    /**
     * Constructeur de la vue du bateau.
     * Initialise l’image du bateau, ajuste sa taille et place la vue selon la position du modèle.
     *
     * @param boat Le modèle de bateau associé à cette vue
     */
    public BoatView(Boat boat) {
        this.boat = boat;
        this.imageView = new ImageView(new Image("sunny.png")); // image par défaut du bateau

        // Définir la taille de l’image
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

        // Ajouter l’image au conteneur StackPane
        getChildren().add(imageView);

        // Positionner le bateau selon les coordonnées du modèle
        updatePositionFromModel();
    }

    /**
     * Met à jour la position visuelle de la vue du bateau
     * en se basant sur la position actuelle dans le modèle {@link Boat}.
     */
    public void updatePositionFromModel() {
        setLayoutX(boat.getPosition().getX());
        setLayoutY(boat.getPosition().getY());
    }

    /**
     * Retourne l’objet modèle associé à cette vue.
     * @return le bateau
     */
    public Boat getBoat() {
        return boat;
    }

    /**
     * Retourne l’objet ImageView contenant l’image du bateau.
     * @return l’image du bateau
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Change dynamiquement l’image du bateau à l’écran.
     *
     * @param imagePath chemin relatif vers la nouvelle image à utiliser
     */
    public void setBoatImage(String imagePath) {
        imageView.setImage(new Image(imagePath));
    }
}
