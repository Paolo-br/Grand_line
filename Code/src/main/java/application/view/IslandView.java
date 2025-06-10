package application.view;

import application.model.Island;
import javafx.animation.FadeTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;

public class IslandView extends StackPane {
    private Island island;
    private ImageView imageView;
    private BossView bossView;
    private ImageView fogOverlay;

    public IslandView(Island island){
        this.island=island;
        this.imageView= new ImageView(new Image(island.getView()+".png"));
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        if (island.isRedLine()){
            imageView.setFitHeight(300);
            imageView.setFitWidth(120);
        }
        if (island.isWorld()){
            imageView.setFitHeight(450);
            imageView.setFitWidth(90);
        }
        getChildren().add(imageView);
        setLayoutX(island.getPosition().getX());
        setLayoutY(island.getPosition().getY());
        setVisible(island.isVisible());


    }

    public Island getIsland() {
        return island;
    }

    public void activerHalo() {
        DropShadow halo = new DropShadow();
        halo.setRadius(30);
        halo.setColor(Color.ORANGE);
        halo.setSpread(0.6);
        setEffect(halo);
    }

    public void setBossView(BossView bossView) {
        this.bossView = bossView;
        if (bossView != null) bossView.setVisible(false); // caché par défaut
    }

    public void afficherBoss() {
        if (bossView != null) bossView.updateVisibility();
    }

    public BossView getBossView() {
        return bossView;
    }
    public void updateVisibility() {
        setVisible(island.isVisible());
    }



}
