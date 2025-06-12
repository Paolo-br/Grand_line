package application.view;

import application.model.Island;
import javafx.animation.FadeTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * La classe {@code IslandView} représente l'affichage graphique d'une île sur la carte.
 * Elle est associée à un objet {@link Island} du modèle, et ajuste dynamiquement son apparence
 * selon ses propriétés (Red Line, World Line, visibilité...).
 * Ressources utilisées :
 * - Images PNG
 * - Utilisation d'effets JavaFX (DropShadow)
 */
public class IslandView extends StackPane {

    // Référence au modèle Island
    private Island island;

    // Image principale représentant l'île
    private ImageView imageView;

    // Vue associée à un boss, si présent sur l’île
    private BossView bossView;

    // (Non utilisé actuellement, mais prévu pour un effet de brouillard)
    private ImageView fogOverlay;

    /**
     * Constructeur de la vue d’île.
     * Instancie l’image correspondante et l’adapte selon le type de l’île (normale, Red Line, World).
     *
     * @param island L’objet modèle représentant l’île à afficher.
     */
    public IslandView(Island island) {
        this.island = island;

        // Création et chargement de l’image selon le nom de la vue
        this.imageView = new ImageView(new Image(island.getView() + ".png"));

        // Dimensions par défaut pour une île normale
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);

        // Redimensionnement spécifique si l’île est une Red Line
        if (island.isRedLine()) {
            imageView.setFitHeight(300);
            imageView.setFitWidth(120);
        }

        // Redimensionnement spécifique si l’île est une World Line
        if (island.isWorld()) {
            imageView.setFitHeight(450);
            imageView.setFitWidth(90);
        }

        // Ajout de l’image à l’interface graphique
        getChildren().add(imageView);

        // Positionnement de la vue sur la carte
        setLayoutX(island.getPosition().getX());
        setLayoutY(island.getPosition().getY());

        // Initialisation de la visibilité
        setVisible(island.isVisible());
    }

    /**
     * Retourne l’objet {@link Island} associé à cette vue.
     */
    public Island getIsland() {
        return island;
    }

    /**
     * Ajoute un effet visuel en surbrillance autour de l'île.
     * Utilisé pour indiquer qu'une île est sélectionnée
     */
    public void activerHalo() {
        DropShadow halo = new DropShadow();
        halo.setRadius(30);
        halo.setColor(Color.ORANGE);
        halo.setSpread(0.6);
        setEffect(halo);
    }

    /**
     * Définit la vue du boss associée à cette île.
     * Par défaut, elle est invisible (jusqu’à accostage par le joueur).
     *
     * @param bossView La vue du boss à associer.
     */
    public void setBossView(BossView bossView) {
        this.bossView = bossView;
        if (bossView != null) bossView.setVisible(false);
    }

    /**
     * Rend le boss visible si celui-ci n’a pas encore été vaincu.
     * Appelle {@code updateVisibility()} sur la vue du boss.
     */
    public void afficherBoss() {
        if (bossView != null) bossView.updateVisibility();
    }

    /**
     * Retourne la vue du boss associée à cette île.
     *
     * @return l’objet {@link BossView} lié à l’île.
     */
    public BossView getBossView() {
        return bossView;
    }

    /**
     * Met à jour la visibilité de l’île selon sa propriété {@code visible}.
     */
    public void updateVisibility() {
        setVisible(island.isVisible());
    }
}
