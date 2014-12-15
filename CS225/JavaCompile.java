import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.PopupWindow;

import com.sun.tools.javac.Main;

/**
 * Class to allow user to write original code for a new robot.
 * Utilizes file loading and saving to create class files.
 * 
 * @author Chris
 *
 */
public class JavaCompile extends PopupWindow {

	private BorderPane borderPane;
	private TextArea javaText;
	private TextField fileText;
	private String emptyClass = "";
	private Button compileButton;
	private HBox buttonGroup;
	Scanner scan;
	SimpleStringProperty filename = new SimpleStringProperty("CustomRobot");
	File defaultFile = new File(filename.get() + ".java");
	FileReader input = null;
	com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
	RobotProgram program;

	/**
	 * Create a new javacompile popup window.
	 * 
	 * @param program
	 */
	public JavaCompile(RobotProgram program) {

		this.program = program;
		// Create the BorderPane
		borderPane = new BorderPane();

		// Create a textfield to store the filename (class name)
		fileText = new TextField(filename.get());
		filename.bind(fileText.textProperty());
		fileText.setMinHeight(10);

		// Create a textarea to code in.
		javaText = new TextArea();
		javaText.setMinHeight(300);
		javaText.setText(emptyClass);

		// Create a button to compile the robot.
		compileButton = new Button("Create Robot");
		compileButton.setOnAction(e -> compileCode());
		buttonGroup = new HBox();
		buttonGroup.setAlignment(Pos.CENTER);
		buttonGroup.getChildren().addAll(compileButton);
		buttonGroup.layout();

		try {
			input = new FileReader(defaultFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Scanner will read all of the template class into the text area.
		scan = new Scanner(input);

		while (scan.hasNext()) {
			emptyClass += scan.nextLine() + "\n";
		}

		scan.close();
		try {
			input.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		javaText.setText(emptyClass);

		// Setup Javafx gui.
		borderPane.setTop(fileText);
		borderPane.setCenter(javaText);
		borderPane.setBottom(buttonGroup);
		borderPane.layout();
		this.getScene().setRoot(borderPane);

	}

	/**
	 * Compile the code in the editor and add the created class to the list of
	 * robots.
	 * 
	 * TODO : Use a stringbuilder to hide all the boilerplate code from the
	 * user, and avoid any errors. Give the user a list of available funtions.
	 * Check the input for errors - output compile errors.
	 */
	private void compileCode() {
		FileWriter fw;
		try {
			// Create a class file from the user input.
			defaultFile = new File(filename.get() + ".java");
			fw = new FileWriter(defaultFile);
			fw.write(javaText.getText());
			fw.close();

			// Compile class from user created file

			// System.out.println(System.getProperty("java.class.path"));
			// System.out.println(System.getProperty("user.dir"));
			String[] options = { "-classpath",
					System.getProperty("java.class.path"), "-d",
					System.getProperty("user.dir"), filename.get() + ".java" };
			Main.compile(options);

			//Create a class object from the class.
			Class<?> c = Class.forName(filename.get());

			//Add the robot name to the drop down menu of the application.
			program.types.add(filename.get());
			program.typeMap.put(filename.get(), c);
			program.robotType.autosize();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.hide();
	}

}
