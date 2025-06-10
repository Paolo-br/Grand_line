package application.view;

import application.model.Boat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class BoatView extends StackPane {
    private final Boat boat;
    private final ImageView imageView;

    public BoatView(Boat boat) {
        this.boat = boat;
        this.imageView = new ImageView(new Image("sunny.png"));
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        getChildren().add(imageView);
        updatePositionFromModel();
    }

    public void updatePositionFromModel() {
        setLayoutX(boat.getPosition().getX());
        setLayoutY(boat.getPosition().getY());
    }

    public Boat getBoat() {
        return boat;
    }

    public ImageView getImageView() {
        return imageView;
    }
    public void setBoatImage(String imagePath) {
        imageView.setImage(new Image(imagePath));
    }


}
