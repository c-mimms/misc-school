/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.scene.control.SplitPane;

import javafx.scene.control.SplitPaneBuilder;

import javafx.scene.layout.RegionBuilder;



/**

 * A sample that demonstrates styling a hidden split pane with CSS.

 *

 * @see javafx.scene.control.SplitPane 

 * @resource HiddenSplitPane.css

 */

public class HiddenSplitPaneSample extends Application {



    private void init(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setScene(new Scene(root));

        String hidingSplitPaneCss = HiddenSplitPaneSample.class.getResource("HiddenSplitPane.css").toExternalForm();

        final SplitPane splitPane = SplitPaneBuilder.create().id("hiddenSplitter").items(

                RegionBuilder.create().styleClass("rounded").build(),

                RegionBuilder.create().styleClass("rounded").build(),

                RegionBuilder.create().styleClass("rounded").build()).dividerPositions(new double[]{0.33, 0.66}).build();

        splitPane.getStylesheets().add(hidingSplitPaneCss);

        root.getChildren().add(splitPane);

    }



    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }

}





    