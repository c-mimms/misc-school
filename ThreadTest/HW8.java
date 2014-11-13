import java.awt.Toolkit;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**************************************************
 *HW8.java Homework 8: GUI Widgets Author: Chris Mimms
 * Collaborations: Date 11/10/2014
 * 
 * Variable List: 
	screenRes : int
	gridPane : GridPane
	shapePane : Pane
	areaPane : Pane
	imagePane : VBox
	buttonPane : HBox
	circle, circle1, circle3: Circle
	triangle : Polygon
	bearImage : Image
	myImage : ImageView
	calcButton, resetButton : Button
	imageText, areaText : Text
	
 * 
 * Methods List: 
 * start() : Starts the application
 * reset() : Resets the shape pane.
 * recalculate() : Recalculates the area of the triangle
 * 
 * 
 ***************************************************/

public class HW8 extends Application {

	// Constants
	private final int WINDOW_WIDTH = 600;
	private final int WINDOW_HEIGHT = 600;

	// Pane
	private GridPane gridPane;

	// Shapes
	/** Contains the shapes */
	private Pane shapePane, areaPane;
	private VBox imagePane;
	private HBox buttonPane;
	private Circle circle, circle2, circle3;
	private Polygon triangle;

	// Image
	Image bearImage;
	ImageView myImage;

	// Buttons
	Button areaButton, perimeterButton, redrawButton;

	// TextFields
	Text imageText, areaText;
	
	private int screenRes;
	

	/** Default constructor */
	public HW8() {

		// Create the panes
		gridPane = new GridPane();
		shapePane = new Pane();
		imagePane = new VBox(5);
		buttonPane = new HBox(5);
		areaPane = new Pane();

		//Create all circles and triangle
		// Circle
		circle = new Circle(50);
		circle.centerXProperty().set(50 + Math.random() * (200));
		circle.centerYProperty().set(50 + Math.random() * (200));
		circle.setFill(null);
		circle.setStroke(Color.BLACK);

		// Circle
		circle2 = new Circle(50);
		circle2.centerXProperty().set(50 + Math.random() * (200));
		circle2.centerYProperty().set(50 + Math.random() * (200));
		circle2.setFill(null);
		circle2.setStroke(Color.BLACK);

		// Circle
		circle3 = new Circle(50);
		circle3.centerXProperty().set(50 + Math.random() * (200));
		circle3.centerYProperty().set(50 + Math.random() * (200));
		circle3.setFill(null);
		circle3.setStroke(Color.BLACK);

		//Triangle
		triangle = new Polygon(circle.getCenterX(), circle.getCenterY(),
				circle2.getCenterX(), circle2.getCenterY(),
				circle3.getCenterX(), circle3.getCenterY());
		triangle.setFill(null);
		triangle.setStroke(Color.BLACK);

		//Add circles and triangle to shape pane
		shapePane.getChildren().addAll(circle, circle2, circle3, triangle);
		shapePane.setPrefSize(300, 300);

		
		//Image pane
		//create image name label
		imageText = new Text("Modified Bear");
		imageText.setFont(Font.font("Verdana", 20));
		//Load image from internet, resized
		bearImage = new Image(
				"http://redcola.mx/wp-content/uploads/2014/08/Radiohead_bear_logo-cropped.png",
				200, 200, true, true, true);
		//Create image view node
		myImage = new ImageView(bearImage);
		//Add all to imagePane and setup size and layout
		imagePane.getChildren().addAll(imageText, myImage);
		imagePane.setAlignment(Pos.CENTER);
		imagePane.setMinWidth(myImage.getFitWidth());
		imagePane.setMinHeight(myImage.getFitHeight());

		//Create buttons and attach actions
		areaButton = new Button("Area");
		areaButton.setOnAction(e -> recalculate());
		perimeterButton = new Button("Perimeter");
		redrawButton = new Button("Redraw");
		redrawButton.setOnAction(e -> redraw());
		//Add to button pane and set alignment
		buttonPane.getChildren().addAll(areaButton,perimeterButton, redrawButton );
		buttonPane.setAlignment(Pos.CENTER);

		//Create filler area text and add to pane
		areaText = new Text("Bl;akjglkdgklnagklalkgnadlgna");
		areaText.setFont(Font.font("Verdana", 14));
		areaPane.getChildren().add(areaText);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		/* PUT EVERYTHING TOGETHER */
		Scene scene = new Scene(gridPane, WINDOW_WIDTH, WINDOW_HEIGHT);

		areaPane.layout();
		imagePane.layout();
		buttonPane.layout();
		shapePane.layout();
		
		//Add everything to gridpane and layout
		gridPane.add(shapePane, 0, 0);
		gridPane.add(imagePane, 1, 0);
		gridPane.add(buttonPane, 0, 1);
		gridPane.add(areaPane, 1, 1);
		gridPane.layout();
		
		//Set the scene and stage size
		primaryStage.setScene(scene);
		primaryStage.setTitle("Homework 8");
		primaryStage.setWidth(gridPane.getWidth());
		primaryStage.setHeight(gridPane.getHeight());
		primaryStage.setMinWidth(gridPane.getWidth());
		primaryStage.setMinHeight(gridPane.getHeight());
		
		primaryStage.show();
		
		//Find screen resolution (DPI)
		screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		//Calculate triangle size now that it's on screen in DPI
		//String result = String.format("The area is %.2f inches squared.", triangle.computeAreaInScreen() / (screenRes * screenRes));
		String result = String.format("The area is %.0f pixels.", triangle.computeAreaInScreen());
		//Display area
		areaText.setText(result);

	}

	public static void main(String[] args) {
		HW8.launch(args);
	}

	/**
	 * Function to move the circles and redraw the triangle
	 */
	private void redraw() {

		circle.centerXProperty().set(50 + Math.random() * (200));
		circle.centerYProperty().set(50 + Math.random() * (200));

		circle2.centerXProperty().set(50 + Math.random() * (200));
		circle2.centerYProperty().set(50 + Math.random() * (200));

		circle3.centerXProperty().set(50 + Math.random() * (200));
		circle3.centerYProperty().set(50 + Math.random() * (200));

		ObservableList<Double> points = triangle.getPoints();
		points.set(0, circle.getCenterX());
		points.set(1, circle.getCenterY());

		points.set(2, circle2.getCenterX());
		points.set(3, circle2.getCenterY());

		points.set(4, circle3.getCenterX());
		points.set(5, circle3.getCenterY());

	}

	/**
	 * Recalculate the area of the triangle
	 */
	private void recalculate() {


		ObservableList<Double> points = triangle.getPoints();
		for(double i : points){
			
		}
		String result = String.format("The area is %.2f pixels.", triangle.computeAreaInScreen());
		areaText.setText(result);
	}

}