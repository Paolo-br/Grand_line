package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameState {
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty lives = new SimpleIntegerProperty(5);
    private final DoubleProperty multiplicator = new SimpleDoubleProperty(1);

    public int getLevel() {
        return level.get();
    }

    public void setLevel(int value) {
        level.set(value);
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public int getLives() {
        return lives.get();
    }

    public void setLives(int value) {
        lives.set(value);
    }

    public IntegerProperty livesProperty() {
        return lives;
    }

    public double getMultiplicator() {
        return multiplicator.get();
    }

    public void setMultiplicator(double value) {
        multiplicator.set(value);
    }

    public DoubleProperty multiplicatorProperty() {
        return multiplicator;
    }
}
