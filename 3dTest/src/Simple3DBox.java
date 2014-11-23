
 
import java.security.KeyException;

import javafx.animation.AnimationTimer;

import java.security.PublicKey;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.sun.javafx.sg.prism.NGPhongMaterial;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
 
public class Simple3DBox extends Application {

    double anchorX, anchorY,anchorAngle;
    
    public Parent createContent() throws Exception {
 
        // Box
        Box testBox = new Box(5, 5, 5);
        Box testBox2 = new Box(2, 3, 7);
        testBox2.setMaterial(new PhongMaterial(Color.RED));
        testBox.setDrawMode(DrawMode.FILL);
        
      //Create Material
        PhongMaterial mat = new PhongMaterial();
        Image diffuseMap = new Image("http://fc04.deviantart.net/fs71/f/2010/124/c/8/Wood_Box_Texture_by_jackzeenho.jpg");
        Image normalMap = new Image("https://www.filterforge.com/filters/5231-normal.jpg");

        // Set material properties
        mat.setDiffuseMap(diffuseMap);
        mat.setBumpMap(normalMap);
        
        mat.setSpecularColor(Color.WHITE);

        testBox.setMaterial(mat);
        testBox2.setDrawMode(DrawMode.FILL);

        
 
        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -15));
 
        // Build the Scene Graph
        
        
        Group root = new Group();       
        root.getChildren().add(camera);
        root.getChildren().add(testBox);
        root.getChildren().add(testBox2);
 
        // Use a SubScene       
        SubScene subScene = new SubScene(root, 800,800,true,SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        
        subScene.setDepthTest(DepthTest.ENABLE);
        root.setDepthTest(DepthTest.ENABLE);
        
        Group group = new Group();
        group.getChildren().add(subScene);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                testBox.getTransforms().add(new Rotate(-0.1, Rotate.Y_AXIS));
                testBox2.getTransforms().addAll(new Rotate(0.2, Rotate.Y_AXIS),new Rotate(0.2, Rotate.Z_AXIS));
                //testBox.translateXProperty().set(l/10000); 
                //camera.getTransforms().addAll(new Rotate(-0.1, Rotate.Y_AXIS));
            }
 
        };
        
        camera.setRotationAxis(Rotate.Y_AXIS);
        
        subScene.setOnMousePressed(e -> {
        	anchorX = e.getSceneX();
        	anchorY = e.getSceneY();
        	anchorAngle = camera.getRotate();
        	PickResult pr = e.getPickResult();
        	System.out.println(pr.getIntersectedTexCoord());
        });
        
        subScene.setOnMouseDragged(e ->{
        	camera.setRotate(anchorAngle + anchorX - e.getSceneX());
        	camera.setRotate(anchorAngle + anchorX - e.getSceneX());
        });
        
        timer.start();
        
      //  Timeline anim = new Timeline();
        return group;
        
    }


	@Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Scene scene = new Scene(createContent(),800,800,true,
                SceneAntialiasing.BALANCED);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }
}