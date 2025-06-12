package application.model;

/**
 * La classe Island représente une île sur la carte du jeu.
 * Chaque île possède une position, un état d'exploration, des caractéristiques
 * spéciales (comme "Red Line", "World", ou "One Piece"), une apparence graphique (view),
 * une visibilité et éventuellement un boss associé.
 *
 * Ressources utilisées :
 * - Classe Position (pour la localisation de l'île)
 * - Classe Boss (un boss peut être assigné à une île)
 */
public class Island {

    // Position de l'île sur la carte (coordonnées X/Y)
    private Position position;

    // Indique si l'île a déjà été explorée par le joueur
    private boolean explored;

    // Indique si l'île est visible sur la carte
    private boolean visible;

    // L'île fait-elle partie de la "Red Line" (élément scénaristique spécifique)
    private boolean redLine;

    // L'île fait-elle partie du "nouveau monde" ou du "monde d'origine"
    private boolean world;

    // L'île contient-elle le One Piece ? (fin du jeu)
    private boolean onePiece;

    // Nom de l'image ou identifiant de la vue associée à cette île
    private String view;

    // Boss associé à l'île (facultatif)
    private Boss boss;

    /**
     * Constructeur principal de la classe Island.
     *
     * @param position Position de l'île (coordonnées x, y)
     * @param explored L'île a-t-elle été explorée ?
     * @param view Nom de l'image ou ressource graphique associée
     * @param redLine Est-ce une île de la Red Line ?
     * @param world Est-ce une île du monde principal ?
     * @param onePiece Contient-elle le One Piece ?
     * @param visible Est-elle visible sur la carte ?
     */
    public Island(Position position, boolean explored, String view,
                  boolean redLine, boolean world, boolean onePiece, boolean visible) {
        this.position = position;
        this.explored = explored;
        this.view = view;
        this.redLine = redLine;
        this.world = world;
        this.onePiece = onePiece;
        this.visible = visible;
    }

    /** Retourne la position de l'île. */
    public Position getPosition() {
        return position;
    }

    /** Retourne si l'île a été explorée. */
    public boolean isExplored() {
        return explored;
    }


    /** Met à jour l'état d'exploration de l'île. */
    public void setexplored(boolean explored) {
        this.explored = explored;
    }

    /** Retourne le nom de la ressource graphique associée. */
    public String getView() {
        return view;
    }

    /** Définit le nom de la ressource graphique. */
    public void setView(String view) {
        this.view = view;
    }

    /** Indique si l'île appartient à la Red Line. */
    public boolean isRedLine() {
        return redLine;
    }

    /** Indique si l'île appartient au monde extérieur. */
    public boolean isWorld() {
        return world;
    }

    /** Retourne vrai si cette île contient le One Piece. */
    public boolean isOnePiece() {
        return onePiece;
    }

    /** Définit si cette île contient le One Piece. (utile si on veit rendre la fin encore plus aléatoire) */
    public void setOnePiece(boolean onePiece) {
        this.onePiece = onePiece;
    }

    /** Retourne la visibilité actuelle de l'île. */
    public boolean isVisible() {
        return visible;
    }

    /** Modifie la visibilité de l'île sur la carte. */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /** Retourne le boss associé à cette île (s'il existe). */
    public Boss getBoss() {
        return boss;
    }

    /** Associe un boss à cette île. */
    public void setBoss(Boss boss) {
        this.boss = boss;
    }
}
