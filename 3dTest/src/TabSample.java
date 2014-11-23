/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.scene.control.Tab;

import javafx.scene.control.TabPane;

import javafx.geometry.Side;

import javafx.scene.layout.BorderPane;



/**

 * An implementation of tabs using the TabPane class.

 *

 * @see javafx.scene.control.TabPane

 * @see javafx.scene.control.Tab

 * @see javafx.scene.control.TabPane

 */

public class TabSample extends Application {

    private void init(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setScene(new Scene(root));

        BorderPane borderPane = new BorderPane();

        final TabPane tabPane = new TabPane();

        tabPane.setPrefSize(400, 400);

        tabPane.setSide(Side.TOP);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        final Tab tab1 = new Tab();

        tab1.setText("Tab 1");

        final Tab tab2 = new Tab();

        tab2.setText("Tab 2");

        final Tab tab3 = new Tab();

        tab3.setText("Tab 3");

        final Tab tab4 = new Tab();

        tab4.setText("Tab 4");

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);

        borderPane.setCenter(tabPane);

        root.getChildren().add(borderPane);

    }



    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }

}



    