import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class EventsDemo extends Application {

	// Pane
	private BorderPane borderPane;
	
	// Menu stuff
	private MenuBar menuBar;							// MenuBar
	private Menu menuFile, menuShapes, menuHelp;		// Menus
	private MenuItem miClose;							// Close MenuItem
	private CheckMenuItem miCircle, miLine, miTriangle;	// CheckMenuItems for each shape
	private MenuItem miShowAll, miClearAll;				// Shows/clears all shapes
	private MenuItem miAbout;							// Displays info about the program
	
	// Shapes
	private Group shapeGroup;
	private Circle circle;
	private Line line;
	private Polygon triangle;
	private Slider slide;
	
	public EventsDemo(){
		
		// Create the BorderPane
		borderPane = new BorderPane();
				
		/* MENU CREATION */
		// Create MenuItems
		miClose = new MenuItem("Close");
		miCircle = new CheckMenuItem("Circle");
		miLine = new CheckMenuItem("Line");
		miTriangle = new CheckMenuItem("Triangle");
		miShowAll = new MenuItem("Show all");
		miClearAll = new MenuItem("Clear all");
		miAbout = new MenuItem("About...");		
		
		// Create Menus
		menuFile = new Menu("File");
		menuShapes = new Menu("Shapes");
		menuHelp = new Menu("Help");		
		
		// Create MenuBar
		menuBar = new MenuBar();		
		
		// Add menu items to respective menus
		menuFile.getItems().add(miClose);
		menuShapes.getItems().addAll(miCircle, miLine, miTriangle, new SeparatorMenuItem(), miShowAll, miClearAll);
		menuHelp.getItems().add(miAbout);
		
		// Add menus to menuBar
		menuBar.getMenus().addAll(menuFile, menuShapes, menuHelp);
		
		


		miShowAll.setOnAction(new ShowAllHandler());
		
		miClearAll.setOnAction(new ClearAllHandler());
		
		miAbout.setOnAction(e -> showAbout());
		
		miClose.setOnAction(e -> System.exit(0));
		
		slide = new Slider();
		
		/* SHAPE CREATION */
		circle = new Circle(100);
		circle.setFill(Color.YELLOW);
		line = new Line(0,0,200,200);
		line.setStroke(Color.RED);
		triangle = new Polygon(100, 10, 10, 100, 100, 100);
		triangle.setFill(Color.CYAN);
		shapeGroup = new Group(circle, line, triangle);

		circle.visibleProperty().bind(miCircle.selectedProperty());
		triangle.visibleProperty().bind(miTriangle.selectedProperty());
		line.visibleProperty().bind(miLine.selectedProperty());
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/* SETUP EVENT HANDLERS */


		
		
		/* PUT EVERYTHING TOGETHER */
		Scene scene = new Scene(borderPane, 350, 350);
		
		// Add the menubar and shapes to the borderpane
		borderPane.setTop(menuBar);
		borderPane.setCenter(shapeGroup);
		borderPane.setBottom(slide);
		
		
		// Configure and display the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Fun Stuff");
		primaryStage.show();
	}

	public static void main(String[] args) {
		EventsDemo.launch(args);
	}
	
	private void showAbout(){
		
		final String aboutText = "This program was written by Sean Holden for use in the CS225 lab sections at Embry-Riddle Aeronautical University.  "
				+ "Modification and redistribution is highly encouraged.  No rights reserved.  Void where prohibited.  Batteries not included.";
		
		// Create the text label
		Label aboutLabel = new Label();
		aboutLabel.setWrapText(true);
		aboutLabel.setTextAlignment(TextAlignment.CENTER);
		aboutLabel.setFont(Font.font("Comic Sans MS", 14));
		aboutLabel.setText(aboutText);
		
		// Add the label to a StackPane
		StackPane pane = new StackPane();
		pane.getChildren().add(aboutLabel);
		
		// Create and display said the aforementioned pane in a new stage 	
		Scene scene = new Scene(pane, 550, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("About this program");
		stage.setResizable(false);
		stage.show();
	}
	
	private class CircleHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent arg0) {
			circle.setVisible(miCircle.isSelected());			
		}		
	}

	private class ShowAllHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent arg0) {
			miCircle.setSelected(true);
			miLine.setSelected(true);
			miTriangle.setSelected(true);
		}
	}


	private class ClearAllHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent arg0) {
			miCircle.setSelected(false);
			miLine.setSelected(false);
			miTriangle.setSelected(false);
		}
	}
	
	
}
