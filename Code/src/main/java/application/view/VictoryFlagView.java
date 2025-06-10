package application.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class VictoryFlagView extends StackPane {

    public VictoryFlagView(double x, double y){
        ImageView flag = new ImageView(new Image("flag.png"));
        flag.setFitWidth(55);
        flag.setFitHeight(55);
        getChildren().add(flag);
        setLayoutX(x);
        setLayoutY(y);

        setMouseTransparent(true);

    }
}
