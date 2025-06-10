package application.view;

import application.model.Boss;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class BossView extends StackPane {
    private final ImageView imageView;
    private final Boss boss;

    public BossView(Boss boss) {
        this.boss=boss;
        this.imageView = new ImageView(new Image(boss.getView()+".png"));
        imageView.setFitWidth(55);
        imageView.setFitHeight(70);
        getChildren().add(imageView);
        setPickOnBounds(true);
        setVisible(false); // Par défaut : invisible jusqu'à accostage
    }

    public void updateVisibility() {
        setVisible(!boss.isVaincu());
    }

}
