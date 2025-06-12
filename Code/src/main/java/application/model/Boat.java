package application.model;

/**
 * Classe représentant un bateau dans le modèle de l'application.
 * Un bateau possède une position représentée par l'objet {@link Position}.
 * Cette classe fournit des méthodes pour accéder et modifier la position du bateau.
 */
public class Boat {

    /**
     * Position actuelle du bateau sur le plateau (coordonnées X et Y).
     */
    private Position position;

    /**
     * Constructeur de la classe Boat.
     *
     * @param position La position initiale du bateau.
     */
    public Boat(Position position) {
        this.position = position;
    }

    /**
     * Retourne la position actuelle du bateau.
     *
     * @return L'objet Position représentant la position du bateau.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Définit une nouvelle position pour le bateau.
     *
     * @param position La nouvelle position sous forme d'objet Position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Met à jour la position du bateau avec des coordonnées précises.
     *
     * @param x La coordonnée X.
     * @param y La coordonnée Y.
     */
    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }
}
