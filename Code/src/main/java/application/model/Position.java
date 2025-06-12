package application.model;

/**
 * La classe Position représente une position dans un espace 2D avec des coordonnées x et y.
 * Elle est utilisée pour placer des objets comme les îles, le bateau ou les boss sur la carte.
 */
public class Position {

    // Coordonnée horizontale (abscisse)
    private double x;

    // Coordonnée verticale (ordonnée)
    private double y;

    /**
     * Constructeur de la classe Position.
     *
     * @param x Coordonnée X
     * @param y Coordonnée Y
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** Retourne la coordonnée X. */
    public double getX() {
        return x;
    }

    /** Retourne la coordonnée Y. */
    public double getY() {
        return y;
    }

    /** Modifie la coordonnée X. */
    public void setX(double x) {
        this.x = x;
    }

    /** Modifie la coordonnée Y. */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Calcule la distance entre cette position et une autre.
     *
     * @param other L'autre position avec laquelle mesurer la distance
     * @return La distance euclidienne entre les deux positions
     */
    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
