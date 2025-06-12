package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * La classe GameState gère l'état courant de la partie.
 * Elle utilise des propriétés JavaFX pour permettre une liaison (binding)
 * directe avec l'interface utilisateur.
 *
 * Technologies utilisées :
 * - JavaFX : pour les propriétés observables (IntegerProperty, DoubleProperty),
 *            facilitant les mises à jour automatiques dans la vue.
 */
public class GameState {

    // Propriété représentant le niveau actuel du joueur (par défaut à 1)
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    // Propriété représentant le nombre de vies restantes (par défaut à 5)
    private final IntegerProperty lives = new SimpleIntegerProperty(5);

    // Propriété représentant un multiplicateur de score ou de difficulté (par défaut à 1.0)
    private final DoubleProperty multiplicator = new SimpleDoubleProperty(1);

    /**
     * Retourne le niveau actuel du joueur.
     * @return niveau (int)
     */
    public int getLevel() {
        return level.get();
    }

    /**
     * Définit le niveau du joueur.
     * @param value nouveau niveau à attribuer
     */
    public void setLevel(int value) {
        level.set(value);
    }

    /**
     * Retourne la propriété JavaFX associée au niveau.
     * Utile pour lier à des composants visuels.
     * @return propriété IntegerProperty
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Retourne le nombre actuel de vies.
     * @return vies (int)
     */
    public int getLives() {
        return lives.get();
    }

    /**
     * Met à jour le nombre de vies restantes.
     * @param value nouvelle valeur de vies
     */
    public void setLives(int value) {
        lives.set(value);
    }

    /**
     * Retourne la propriété JavaFX des vies.
     * @return propriété IntegerProperty
     */
    public IntegerProperty livesProperty() {
        return lives;
    }

    /**
     * Retourne la valeur actuelle du multiplicateur.
     * @return multiplicateur (double)
     */
    public double getMultiplicator() {
        return multiplicator.get();
    }

    /**
     * Définit la valeur du multiplicateur.
     * @param value nouvelle valeur du multiplicateur
     */
    public void setMultiplicator(double value) {
        multiplicator.set(value);
    }

    /**
     * Retourne la propriété JavaFX du multiplicateur.
     * @return propriété DoubleProperty
     */
    public DoubleProperty multiplicatorProperty() {
        return multiplicator;
    }
}
