import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.PopupWindow.AnchorLocation;

/**
 * Collaborations :  Erin Catto's Box2D library ported to java by Daniel Murphy
 * 
 * http://www.jbox2d.org/
 * https://code.google.com/p/jbox2d/wiki/QuickStart 
 * 
 * Any small code references are noted where they are used. (Only dragging Robots and Walls)
 * 
 * This program is a physics based, simple robot simulator.
 * It allows the user to define robot behaviors, add many of the same or different type robots to the simulation,
 * 	and place obstacles in the simulation.
 * 
 * Many features were planned but not implemented, including saving and loading the simulation state, controlling the simulation speed,
 * 	more complex robots and robot behaviors, and stupid-proofing the user code input. 
 * 
 * 
 * 
 * @author Chris Mimms
 *
 */
@SuppressWarnings("rawtypes")
public class RobotProgram extends Application {

	boolean playing = false;
	static PhysicsPane main;
	ColorPicker colorPicker;
	BorderPane borderPane;
	ComboBox<String> robotType;
	ObservableList<String> types;
	HashMap<String, Class> typeMap;
	JavaCompile customBots;
	
	Stage mystage;

	@Override
	public void start(Stage stage) throws Exception {

		mystage = stage;
		main = new PhysicsPane();
		borderPane = new BorderPane();
		// robotInfoPane = new VBox();
		main.setOnMousePressed(e -> play(false));
		main.setOnMouseReleased(e -> play(true));
		colorPicker = new ColorPicker(Color.GRAY);

		Scene scene = new Scene(borderPane);
		borderPane.setCenter(main);

		//Add walls to the edges of the window.
		PhysicsWall left = new PhysicsWall(20, 800, 0, 0);
		main.getChildren().add(left);
		PhysicsWall right = new PhysicsWall(20, 800, 780, 0);
		main.getChildren().add(right);
		PhysicsWall top = new PhysicsWall(800, 20, 0, 0);
		main.getChildren().add(top);
		PhysicsWall bottom = new PhysicsWall(800, 20, 0, 780);
		main.getChildren().add(bottom);

		
		//Setup the buttons and panes in the GUI
		//TODO: Make this prettier and more functional.
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

		robotType = new ComboBox<String>();
		types = FXCollections.observableArrayList("Right Robot", "Left Robot",
				"Custom Robot");

		typeMap = new HashMap<String, Class>();
		typeMap.put("Right Robot", RightRobot.class);
		typeMap.put("Left Robot", LeftRobot.class);
		typeMap.put("Custom Robot", null);

		robotType.setItems(types);
		robotType.setValue("Right Robot");

		Button addButton = new Button("Add Robot");
		addButton.setOnAction(e -> addRobot());

		Button addButton3 = new Button("Add Wall");
		addButton3.setOnAction(e -> addWall());

		borderPane.setTop(new HBox(stepButton, playButton, pauseButton,
				debugButton, robotType, addButton, addButton3, colorPicker));

		// Configure and display the stage
		stage.setScene(scene);
		stage.setTitle("Robot Program");
		stage.show();
		stage.setMinHeight(stage.getHeight());
		stage.setMinWidth(stage.getWidth());

		//Create timer to update the simulation
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				if (playing) {
					main.update();
				}
			}

		};
		timer.start();
		
		customBots = new JavaCompile(this);

	}

	/**
	 * Add a wall to the simulation
	 */
	private void addWall() {
		PhysicsWall wall = new PhysicsWall(200, 200, 400, 400);
		wall.setColor(colorPicker.getValue());
		main.getChildren().add(wall);
	}
	

	/**
	 * Add the type of robot that is selected to the simulation.
	 */
	@SuppressWarnings("unchecked")
	private void addRobot() {

		//Create a robot from the list of robot classes available.
		Class robot = typeMap.get(robotType.getValue());
		if (robot == null) {
			createNewRobot();
		} else {
			RobotBrain brain = new RobotBrain(450, 350, 50, 50, 30, 0, 1);
			AbstractPhysicsRobot bot;
			
			//These exceptions will happen all the time until I sanitize user input.
			try {
				bot = (AbstractPhysicsRobot) robot.getConstructor(
						PhysicsPane.class, RobotBrain.class).newInstance(main,
						brain);
				bot.setColor(colorPicker.getValue());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void createNewRobot() {
		// TODO Auto-generated method stub
		customBots.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
		//double x = mystage.getScene().getWindow().getX() + mystage.getScene().getWindow().getWidth();
		//double y = mystage.getScene().getWindow().getY();
		//customBots.setX(x);
		//customBots.setY(y);
		customBots.setAutoHide(true);
		customBots.show(mystage);
	}

	/**
	 * Old method to interface with robot properties
	 *TODO: Update this to work correctly with physics robots.
	 * @param bot
	 * @deprecated
	 */
	 @SuppressWarnings("unused")
	private void populateInfo(AbstractPhysicsRobot bot)
	 {
	 playing = false;
	 // TextField angle = new TextField();
	 // angle.setPromptText("Angle");
	 //
	 // angle.textProperty().addListener(new ChangeListener<String>()
	 // {
	 // @Override
	 // public void changed(ObservableValue<? extends String> observable,
	 // String oldValue, String newValue)
	 // {
	 // if (newValue.matches("\\d+"))
	 // {
	 // }
	 // else
	 // {
	 // angle.setText(oldValue);
	 // }
	 // }
	 //
	 // });
	
	 Slider slide = new Slider();
	 slide.setMax(360);
	 slide.setMin(0);
	 slide.setValue(bot.brain.dir.doubleValue());
	 bot.brain.dir.bindBidirectional(slide.valueProperty());
	
	 Slider slide2 = new Slider();
	 slide2.setMax(20);
	 slide2.setMin(0);
	 slide2.setValue(bot.brain.vel.doubleValue());
	 bot.brain.vel.bindBidirectional(slide2.valueProperty());
	
	 Slider slide3 = new Slider();
	 slide3.setMax(300);
	 slide3.setMin(0);
	 slide3.setValue(bot.brain.radius.doubleValue());
	 bot.brain.radius.bindBidirectional(slide3.valueProperty());
	
	 Slider slide4 = new Slider();
	 slide4.setMax(360);
	 slide4.setMin(0);
	 slide4.setValue(bot.brain.viewAngle.doubleValue());
	 bot.brain.viewAngle.bindBidirectional(slide4.valueProperty());
	
	 Slider slide5 = new Slider();
	 slide5.setMax(400);
	 slide5.setMin(0);
	 slide5.setValue(bot.brain.viewDistance.doubleValue());
	 bot.brain.viewDistance.bindBidirectional(slide5.valueProperty());
	
//	 robotInfoPane.getChildren().clear();
//	 robotInfoPane.getChildren().addAll(slide, slide2, slide3, slide4,
//	 slide5);
//	 borderPane.setRight(robotInfoPane);
	
	 }

	 /**
	  * Turn the physics simulation on or off.
	  * @param play
	  */
	private void play(boolean play) {
		playing = play;
	}

	public static void main(String[] args) {
		RobotProgram.launch(args);
	}

}
