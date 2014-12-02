import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class RobotProgram extends Application {

	boolean playing = false;
	PhysicsPane main;
	ColorPicker colorPicker;
	BorderPane borderPane;

	// private VBox robotInfoPane;

	public RobotProgram() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(Stage stage) throws Exception {

		main = new PhysicsPane();
		borderPane = new BorderPane();
		// robotInfoPane = new VBox();

		colorPicker = new ColorPicker(Color.GRAY);

		Scene scene = new Scene(borderPane);

		borderPane.setCenter(main);

		// Wall left = new Wall(10, 800);
		// left.setX(0);
		// left.setY(0);
		// main.getChildren().add(left);
		// Wall right = new Wall(10, 800);
		// right.setX(790);
		// right.setY(0);
		// main.getChildren().add(right);
		// Wall top = new Wall(800, 10);
		// top.setLayoutX(0);
		// top.setLayoutY(0);
		// main.getChildren().add(top);
		// Wall bottom = new Wall(800, 10);
		// bottom.setX(0);
		// bottom.setY(790);
		// main.getChildren().add(bottom);

		Button stepButton = new Button("Step");
		stepButton.setOnAction(e -> main.update());
		Button playButton = new Button("Play");
		playButton.setOnAction(e -> {
			play(true);
			// clearPanes();
			});
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> play(false));

		Button debugButton = new Button("Debug");
		debugButton.setOnAction(e -> main.toggleDebug());

		Button addButton = new Button("Add Robot");
		addButton.setOnAction(e -> addRobot());

		// Button addButton2 = new Button("Add Right Robot");
		// addButton2.setOnAction(e -> addRobot2());

		Button addButton3 = new Button("Add Ball");
		addButton3.setOnAction(e -> addBall());

		borderPane.setTop(new HBox(stepButton, playButton, pauseButton,
				debugButton, addButton, addButton3, colorPicker));

		// Configure and display the stage
		stage.setScene(scene);
		stage.setTitle("Robot Program");
		stage.show();
		stage.setMinHeight(stage.getHeight());
		stage.setMinWidth(stage.getWidth());

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				if (playing) {
					main.update();
				}
			}

		};
		timer.start();

	}

	private void addBall() {
		// PhysicsBall ball = new PhysicsBall(main,40);
		// ball.setCenterX(350);
		// ball.setCenterY(450);
		// ball.setFill(colorPicker.getValue());
		// main.getChildren().add(ball);
		PhysicsWall wall = new PhysicsWall(200, 20, 20, 20);
		main.getChildren().add(wall);
	}

	// private void clearPanes()
	// {
	// for(Node tmp : robotInfoPane.getChildren()){
	// tmp.getProperties().cl
	// }
	// robotInfoPane.getChildren().clear();
	// borderPane.setRight(null);
	// }

	private void addRobot() {
		RobotBrain brain = new RobotBrain(450, 350, 50, 50, 30, 0, 1);
		AbstractPhysicsRobot bot = new AbstractPhysicsRobot(main, brain);
		bot.setColor(colorPicker.getValue());

	}

	//
	// private void addRobot2()
	// {
	// RobotBrain brain = new RobotBrain(450, 350, 50, 50, 30, 0, 1);
	// AbstractRobot bot = new RightRobot(main, brain);
	// bot.setColor(colorPicker.getValue());
	// bot.setOnMouseClicked(e ->
	// {
	// populateInfo(bot);
	// });
	//
	// }

	// private void populateInfo(AbstractRobot bot)
	// {
	// playing = false;
	// // TextField angle = new TextField();
	// // angle.setPromptText("Angle");
	// //
	// // angle.textProperty().addListener(new ChangeListener<String>()
	// // {
	// // @Override
	// // public void changed(ObservableValue<? extends String> observable,
	// // String oldValue, String newValue)
	// // {
	// // if (newValue.matches("\\d+"))
	// // {
	// // }
	// // else
	// // {
	// // angle.setText(oldValue);
	// // }
	// // }
	// //
	// // });
	//
	// Slider slide = new Slider();
	// slide.setMax(360);
	// slide.setMin(0);
	// slide.setValue(bot.brain.dir.doubleValue());
	// bot.brain.dir.bindBidirectional(slide.valueProperty());
	//
	// Slider slide2 = new Slider();
	// slide2.setMax(20);
	// slide2.setMin(0);
	// slide2.setValue(bot.brain.vel.doubleValue());
	// bot.brain.vel.bindBidirectional(slide2.valueProperty());
	//
	// Slider slide3 = new Slider();
	// slide3.setMax(300);
	// slide3.setMin(0);
	// slide3.setValue(bot.brain.radius.doubleValue());
	// bot.brain.radius.bindBidirectional(slide3.valueProperty());
	//
	// Slider slide4 = new Slider();
	// slide4.setMax(360);
	// slide4.setMin(0);
	// slide4.setValue(bot.brain.viewAngle.doubleValue());
	// bot.brain.viewAngle.bindBidirectional(slide4.valueProperty());
	//
	// Slider slide5 = new Slider();
	// slide5.setMax(400);
	// slide5.setMin(0);
	// slide5.setValue(bot.brain.viewDistance.doubleValue());
	// bot.brain.viewDistance.bindBidirectional(slide5.valueProperty());
	//
	// robotInfoPane.getChildren().clear();
	// robotInfoPane.getChildren().addAll(slide, slide2, slide3, slide4,
	// slide5);
	// borderPane.setRight(robotInfoPane);
	//
	// }

	private void play(boolean play) {
		playing = play;
	}

	public static void main(String[] args) {
		RobotProgram.launch(args);
	}

}
