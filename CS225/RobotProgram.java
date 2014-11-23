import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RobotProgram extends Application {

	public RobotProgram() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(Stage stage) throws Exception {

		
		SimPanel main = new SimPanel();
		Scene scene = new Scene(main);
		
		RobotBrain brain = new RobotBrain(0, 0, 50, 5, 5, 5, 5);
		Robot bot = new Robot(brain);
		
		main.getChildren().add(bot);

		// Configure and display the stage
		stage.setScene(scene);
		stage.setTitle("Robot Program");
		stage.show();
		stage.setMinHeight(stage.getHeight());
		stage.setMinWidth(stage.getWidth());
		
	}
	public static void main(String[] args) {
		RobotProgram.launch(args);
	}

}
