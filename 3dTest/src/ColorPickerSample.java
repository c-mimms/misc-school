/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.event.Event;

import javafx.event.EventHandler;

import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.scene.layout.VBox;

import javafx.scene.layout.VBoxBuilder;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;

import javafx.scene.text.Text;

/**
 * 
 * A sample that demonstrates the ColorPicker.
 * 
 *
 * 
 * @see javafx.scene.control.ColorPicker
 */

public class ColorPickerSample extends Application {

	private void init(Stage primaryStage) {

		Group root = new Group();

		primaryStage.setScene(new Scene(root));

		final ColorPicker colorPicker = new ColorPicker(Color.GRAY);

		ToolBar standardToolbar = new ToolBar(colorPicker);

		final Text coloredText = new Text("Colors");

		Font font = new Font(53);

		coloredText.setFont(font);

		final Button coloredButton = new Button("Colored Control");

		Color c = colorPicker.getValue();

		coloredText.setFill(c);

		coloredButton.setStyle(createRGBString(c));

		colorPicker.setOnAction(e -> {
			Color newColor = colorPicker.getValue();

			coloredText.setFill(newColor);

			coloredButton.setStyle(createRGBString(newColor));

		});
		
		VBox coloredObjectsVBox = new VBox(coloredText,coloredButton);
		coloredObjectsVBox.spacingProperty().set(20);
		coloredObjectsVBox.alignmentProperty().set(Pos.CENTER);
		
		
		
		VBox outerVBox = new VBox(standardToolbar, coloredObjectsVBox);
		outerVBox.spacingProperty().set(150);
		outerVBox.paddingProperty().set(new Insets(0,0,120,0));
		outerVBox.alignmentProperty().set(Pos.CENTER);
		
		root.getChildren().add(outerVBox);

	}

	private String createRGBString(Color c) {

		return "-fx-base: rgb(" + (c.getRed() * 255) + ","
				+ (c.getGreen() * 255) + "," + (c.getBlue() * 255) + ");";

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
