package application.model;

import javafx.scene.image.Image;

import java.util.Random;

public class CharacterModel {

    private final String[] heads = {"headLuffy.png", "headNami.png", "headZorro.png"};
    private final String[] bodies = {"bodyLuffy.png", "bodyNami.png", "bodyZorro.png"};
    private final String[] legs = {"legsLuffy.png", "legsNami.png", "legsZorro.png"};



    private int headIndex = 0;
    private int bodyIndex = 0;
    private int legsIndex = 0;

    public Image getHead() {
        return new Image(heads[headIndex]);
    }

    public Image getBody() {
        return new Image(bodies[bodyIndex]);
    }

    public Image getLegs() {
        return new Image(legs[legsIndex]);
    }

    public void nextHead() {
        headIndex = (headIndex + 1) % heads.length;
    }

    public void prevHead() {
        headIndex = (headIndex + heads.length - 1) % heads.length;
    }
    public void nextBody() {
        bodyIndex = (bodyIndex + 1) % bodies.length;
    }

    public void prevBody() {
        bodyIndex = (bodyIndex + bodies.length - 1) % bodies.length;
    }
    public void nextLegs() {
        legsIndex = (legsIndex + 1) % legs.length;
    }

    public void prevLegs() {
        legsIndex = (legsIndex + legs.length - 1) % legs.length;
    }


    public CharacterSelection getCurrentSelection() {
        return new CharacterSelection(getHeadIndex(), getBodyIndex(), getLegsIndex());
    }

    public String getCharacterImageName() {
        return "" + (headIndex + 1) + (bodyIndex + 1) + (legsIndex + 1) + ".png";
    }



    public int getHeadIndex() { return headIndex; }
    public int getBodyIndex() { return bodyIndex; }
    public int getLegsIndex() { return legsIndex; }

    public void randomize() {
        Random rand = new Random();
        headIndex = rand.nextInt(heads.length);
        bodyIndex = rand.nextInt(bodies.length);
        legsIndex = rand.nextInt(legs.length);
    }

}

