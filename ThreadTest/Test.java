
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Test extends Application {

	// Pane
	private BorderPane borderPane;

	private TextArea javaText;
	private TextField fileText;
	private String emptyClass = "";
	private Button compileButton;
	private Button runButton;
	private HBox buttonGroup;
	Scanner scan;
	SimpleStringProperty filename = new SimpleStringProperty("Test");
	File defaultFile = new File(filename.get() + ".java");
	FileReader input = null;

	public Test() {

		// Create the BorderPane
		borderPane = new BorderPane();

		fileText = new TextField(filename.get());
		filename.bind(fileText.textProperty());
		
		javaText = new TextArea();
		javaText.setMinHeight(300);
		javaText.setText(emptyClass);
		
		
		compileButton = new Button("Compile");
		compileButton.setOnAction(e -> compileCode());
		runButton = new Button("Run");
		runButton.setOnAction(e -> runCode());
		
		buttonGroup = new HBox();
		buttonGroup.setPadding(new Insets(5, 5, 5, 5));
		buttonGroup.setSpacing(25);

		buttonGroup.getChildren().addAll(compileButton, runButton);

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

		Scene scene = new Scene(borderPane, 350, 350);
		javaText.setText(emptyClass);

		borderPane.setTop(fileText);
		borderPane.setCenter(javaText);
		borderPane.setBottom(buttonGroup);

		// Configure and display the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Compiler");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Test.launch(args);
	}

	private void compileCode(){
		FileWriter fw;
		try {
			defaultFile = new File(filename.get() + ".java");
			fw = new FileWriter(defaultFile);
			fw.write(javaText.getText());
			fw.close();
			Process pro = Runtime.getRuntime().exec(
					"C:\\Program Files\\Java\\jdk1.8.0_20\\bin\\javac " + filename.get()
							+ ".java");
			printLines(pro.getInputStream());
			printLines(pro.getErrorStream());
			pro.waitFor();
			System.out.println(" exitValue() " + pro.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void runCode() {
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

	private void printLines(InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
	}

}
