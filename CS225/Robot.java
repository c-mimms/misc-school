import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Robot extends StackPane {

	private RobotBrain brain;
	public Group body;

	public Robot(RobotBrain brain) {

		this.brain = brain;

		Circle body = new Circle();
		body.centerXProperty().bind(brain.x);
		body.centerYProperty().bind(brain.y);
		body.radiusProperty().bind(brain.radius);
		body.fillProperty().set(new Color(0.5, 1, .6, 1));

		this.getChildren().add(body);
		// this.layoutXProperty().bind()
		// this.layoutYProperty().bind()

		System.out.println(body.getBoundsInLocal());
		System.out.println(body.getBoundsInParent());
		System.out.println(body.getLayoutX());
		System.out.println(body.getLayoutY());
	}

}
