package application.model;

import javafx.scene.image.Image;

public class CharacterSelection {
    private  int head;
    private int body;
    private int legs;

    public CharacterSelection(int head, int body, int legs) {
        this.head = head;
        this.body = body;
        this.legs = legs;
    }

    public String getCharacterImageName() {
        return "" + (head + 1) + (body + 1) + (legs + 1) + ".png";
    }

    public int getHead() { return head; }
    public int getBody() { return body; }
    public int getLegs() { return legs; }
    public void setHead(int a) { this.head = a;}
    public void setBody(int a) { this.body = a;}
    public void setLegs(int a) { this.legs = a;}
}
