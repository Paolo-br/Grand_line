package application.view;

import application.model.Boss;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * La classe {@code BossView} représente l'affichage visuel d'un boss sur la carte.
 * Elle utilise une image liée à l'objet {@link Boss} fourni en paramètre.
  * Ressources utilisées :
 * - Une image PNG
 */
public class BossView extends StackPane {

    // Composant graphique qui affiche l’image du boss
    private final ImageView imageView;

    // Référence au modèle Boss correspondant à cette vue
    private final Boss boss;

    /**
     * Constructeur de la vue BossView.
     * Crée une image à partir du nom de la vue fourni par le boss
     * et initialise les dimensions et la visibilité.
     *
     * @param boss Le modèle de boss lié à cette vue
     */
    public BossView(Boss boss) {
        this.boss = boss;

        // Chargement de l'image du boss à partir de son identifiant visuel
        this.imageView = new ImageView(new Image(boss.getView() + ".png"));

        // Définition des dimensions de l'image
        imageView.setFitWidth(55);
        imageView.setFitHeight(70);

        // Ajout de l’image au StackPane
        getChildren().add(imageView);

        // Permet de détecter les clics même sur les zones transparentes
        setPickOnBounds(true);

        // La vue est invisible par défaut jusqu'à ce que le joueur accoste l'île
        setVisible(false);
    }

    /**
     * Met à jour la visibilité du boss en fonction de son état (vaincu ou non).
     * Si le boss est vaincu, la vue devient invisible.
     */
    public void updateVisibility() {
        setVisible(!boss.isVaincu());
    }
}
