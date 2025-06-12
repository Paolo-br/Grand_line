package application.model;

/**
 * Classe représentant un boss dans le modèle de l'application.

 * Un boss possède un nom, une vue (représentation graphique), des points de vie (PV)
 * et un état indiquant s'il a été vaincu ou non.
 */
public class Boss {

    /**
     * Nom du boss.
     */
    private final String nom;

    /**
     * Représentation graphique
     */
    private String view;

    /**
     * Points de vie (PV) du boss.
     */
    private float PV;

    /**
     * Statut indiquant si le boss a été vaincu.
     */
    private boolean vaincu;

    /**
     * Constructeur de la classe Boss.
     *
     * @param nom  Le nom du boss.
     * @param view La représentation visuelle du boss.
     * @param PV   Les points de vie initiaux du boss.
     */
    public Boss(String nom, String view, float PV) {
        this.nom = nom;
        this.view = view;
        this.PV = PV;
        this.vaincu = false;
    }

    /**
     * Retourne le nom du boss.
     *
     * @return Le nom du boss.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la représentation visuelle du boss.
     *
     * @return La vue du boss.
     */
    public String getView() {
        return view;
    }

    /**
     * Retourne les points de vie actuels du boss.
     *
     * @return Les PV du boss.
     */
    public float getPV() {
        return PV;
    }

    /**
     * Définit les points de vie du boss.
     *
     * @param PV Les nouveaux points de vie.
     */
    public void setPV(float PV) {
        this.PV = PV;
    }

    /**
     * Vérifie si le boss a été vaincu.
     *
     * @return true si le boss est vaincu, false sinon.
     */
    public boolean isVaincu() {
        return vaincu;
    }

    /**
     * Définit le statut de défaite du boss.
     *
     * @param vaincu true si le boss est vaincu, false sinon.
     */
    public void setVaincu(boolean vaincu) {
        this.vaincu = vaincu;
    }
}
