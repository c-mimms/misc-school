import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import com.sun.tools.javac.Main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class JavaCompile extends Application {

	// Pane
	private BorderPane borderPane;
	private Pane shapePane;
	private TextArea javaText;
	private TextField fileText;
	private String emptyClass = "";
	private Button compileButton;
	private Button runButton;
	private HBox buttonGroup;
	private Circle circle, circle2, circle3;
	private Polygon triangle;
	Scanner scan;
	SimpleStringProperty filename = new SimpleStringProperty("JavaCompile");
	File defaultFile = new File(filename.get() + ".java");
	FileReader input = null;
	com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();

	public JavaCompile() {
		// Create the panes
				shapePane = new Pane();

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
				shapePane.setMinSize(300,200);
				
		// Create the BorderPane
		borderPane = new BorderPane();

		fileText = new TextField(filename.get());
		filename.bind(fileText.textProperty());
		fileText.setMinHeight(10);
		
		javaText = new TextArea();
		javaText.setMinHeight(300);
		javaText.setText(emptyClass);

		compileButton = new Button("Compile");
		compileButton.setOnAction(e -> compileCode());
		runButton = new Button("Run");
		runButton.setOnAction(e -> runCode());

		buttonGroup = new HBox();
		buttonGroup.setPadding(new Insets(5, 5, 5, 5));
		buttonGroup.setSpacing(5);
		buttonGroup.getChildren().addAll(compileButton, runButton);
		buttonGroup.layout();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			input = new FileReader(defaultFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		scan = new Scanner(input);

		while (scan.hasNext()) {
			emptyClass += scan.nextLine() + "\n";
		}

		scan.close();
		input.close();
		BorderPane mainPane = new BorderPane();
		
		javaText.setText(emptyClass);
		
		SimWorld world = new SimWorld();
		Robot test = new Robot(world);
		test.goForward();
		world.setPrefWidth(300);
		world.setPrefHeight(300);
		world.setMaxWidth(300);
		world.setMaxHeight(300);
		world.layout();
		world.addRobot(test);
		
		mainPane.setRight(borderPane);
		mainPane.setCenter(world);
		
		
		borderPane.setTop(fileText);
		borderPane.setCenter(javaText);
		borderPane.setBottom(buttonGroup);
		borderPane.layout();
		
		Scene scene = new Scene(mainPane);
		
		//Use timer to update SimWorld every frame
		AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                world.update(now);
            }

        };
        timer.start();

		// Configure and display the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Compiler");
		primaryStage.show();
		primaryStage.setMinHeight(primaryStage.getHeight());
		primaryStage.setMinWidth(primaryStage.getWidth());
	}

	public static void main(String[] args) {
		JavaCompile.launch(args);
	}

	private void compileCode() {
		FileWriter fw;
		try {
			defaultFile = new File(filename.get() + ".java");
			fw = new FileWriter(defaultFile);
			fw.write(javaText.getText());
			fw.close();

			System.out.println(System.getProperty("java.class.path"));
			System.out.println(System.getProperty("user.dir"));
			String[] options = {"-classpath", System.getProperty("java.class.path"), "-d", 
					System.getProperty("user.dir"), filename.get() + ".java"};
			Main.compile(options);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void runCode2() {
		try {
			Process pro = Runtime.getRuntime().exec("java " + filename.get());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// printLines(pro.getInputStream());
		// printLines(pro.getErrorStream());
		// pro.waitFor();
		// sSystem.out.println(" exitValue() " + pro.exitValue());

	}

	private void runCode() {

		try {
			Class<?> c = Class.forName(filename.get());
//			System.out.println(c);
			Constructor<?> cons = c.getConstructor();
//			System.out.println(cons);
//			System.out.println(c.getSuperclass().getName());
			
			
			if (c.getSuperclass().getName() == "java.lang.Thread") {
				Thread object = (Thread) cons.newInstance();
//				System.out.println(object);
				object.start();
				object.join();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		ThreadsTest te = new ThreadsTest();
//		te.start();

	}


}
