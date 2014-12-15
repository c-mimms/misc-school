import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Special type of Pane that implements a jBox2d physics world.
 * @author Chris
 *
 */
public class PhysicsPane extends Pane {

	private boolean debugMode = false;
	HashMap<Node, Rectangle> debugRects = new HashMap<Node, Rectangle>();
	public static World world;
	public static int height = 800;
	public static int width = 800;
	
	public Canvas canvas;
	public GraphicsContext gc;

	/**
	 * Constructor to create a physics pane.
	 * Future versions should allow user to set a "pixels per meter" parameter to change the physics world characteristics
	 */
	public PhysicsPane() {
		super();
		this.setStyle("-fx-background-color: black;");
		this.setPrefSize(height, width);
		
		//Create canvas for drawing functionality
		canvas = new Canvas(height, width);
		gc = canvas.getGraphicsContext2D();
		this.getChildren().add(canvas);
		
		//Change canvas size to always match the pane.
		this.heightProperty().addListener(e->{
			canvas.setHeight(this.heightProperty().get());
		});

		this.widthProperty().addListener(e->{
			canvas.setWidth(this.widthProperty().get());
		});
		
		
		world = new World(new Vec2(0.0f, -0f)); //Create physics world with no global accelerations
		world.setAllowSleep(false); //Don't allow physics objects to become inactive.
		
		world.setContactListener(new ContactListener() { //Contact listener to correctly set collision flags.

			@Override
			public void preSolve(Contact arg0, Manifold arg1) { //After a collision has been detected, but before it is resolved.

				//Create a world manifold to get collision coordinates in the world space.
				WorldManifold wm = new WorldManifold();
				arg0.getWorldManifold(wm);

				//Check if collision is between two robots
				if (arg0.m_fixtureA.m_body.getUserData() instanceof AbstractPhysicsRobot
						&& arg0.m_fixtureB.m_body.getUserData() instanceof AbstractPhysicsRobot) {

					//Check if robots local forward vectors are towards each other (colliding head on)
					float dir = Vec2.dot(arg0.m_fixtureB.m_body
							.getLocalVector(new Vec2(1, 0)),
							arg0.m_fixtureA.m_body
									.getLocalVector(new Vec2(1, 0)));
					if (dir < 0) { //If they are colliding head on, set collision flags for both robots.
						((AbstractPhysicsRobot) arg0.m_fixtureA.m_body
								.getUserData()).collide();
						((AbstractPhysicsRobot) arg0.m_fixtureB.m_body
								.getUserData()).collide();
					}

					//Check if the collision is happening at the front of Robot B
					dir = Vec2.dot(arg0.m_fixtureB.m_body
							.getLocalVector(new Vec2(1, 0)), wm.normal);
					if (dir < 0) { //If it is set the collision flag for robot B.
						((AbstractPhysicsRobot) arg0.m_fixtureB.m_body
								.getUserData()).collide();

					}
					//Check if the collision is happening at the front of Robot A
					dir = Vec2.dot(arg0.m_fixtureA.m_body
							.getLocalVector(new Vec2(1, 0)), wm.normal);
					//If it is set the collision flag for robot A.
					if (dir > 0) {
						((AbstractPhysicsRobot) arg0.m_fixtureA.m_body
								.getUserData()).collide();

					}

				}
				if (arg0.m_fixtureA.m_body.getUserData() instanceof AbstractPhysicsRobot) {

					float dir = Vec2.dot(arg0.m_fixtureA.m_body
							.getLocalVector(new Vec2(1, 0)), wm.normal);
					if (dir > 0) {
						((AbstractPhysicsRobot) arg0.m_fixtureA.m_body
								.getUserData()).collide();

					}

				} else if (arg0.m_fixtureB.m_body.getUserData() instanceof AbstractPhysicsRobot) {
					// System.out.println(arg0.m_fixtureB.m_body.getLocalVector(new
					// Vec2(1, 0)));
					float dir = Vec2.dot(arg0.m_fixtureB.m_body
							.getLocalVector(new Vec2(1, 0)),wm.normal);
					//System.out.println(dir);
					if (dir < 0) {
						((AbstractPhysicsRobot) arg0.m_fixtureB.m_body
								.getUserData()).collide();
					}
				}

			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {

			}

			@Override
			public void endContact(Contact arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginContact(Contact arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * Get list of objects contained by the Pane, that overlap the given bounds.
	 * @param bound
	 * @return List of overlapping objects
	 */
	public ArrayList<Node> getInBounds(Bounds bound) {
		
		List<Node> children = this.getChildren(); //Get all children of the pane.
		ArrayList<Node> results = new ArrayList<Node>(); //Create list to hold the results of the function.
		
		for (Node bot : children) {

			//Check if bounds intersect
			if (bot.getBoundsInParent().intersects(bound)) {

				results.add(bot); //Add overlapping nodes to the list of results
			}
		}
		return results;
	}

	/**
	 * Set the debug flag to draw bounding boxes of all objects in the pane.
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debugMode = debug;
		if (debug) {
			for (Node item : debugRects.values()) {
				item.setVisible(true);
			}
		} else {
			for (Node item : debugRects.values()) {
				item.setVisible(false);
			}
		}

	}

	/**
	 * Toggle the debug flag.
	 */
	public void toggleDebug() {
		debugMode = !debugMode;
		if (debugMode) {
			for (Node item : debugRects.values()) {
				item.setVisible(true);
			}
		} else {
			for (Node item : debugRects.values()) {
				item.setVisible(false);
			}
		}

	}

	/**
	 * Update is called to update all objects in the pane.
	 * 
	 */
	public void update() {

		//Run one step of the physics world
		world.step(1.0f / 60.f, 8, 3);

		//No longer used functions to draw JavaFX collision shapes to the pane.
//		this.getChildren().addAll(newShapes);
//		newShapes.clear();

		//Update each node contained by the pane.
		for (Node bot : this.getChildren()) { 
			if (bot instanceof AbstractPhysicsRobot) {
				((AbstractPhysicsRobot) bot).update();
			}
		}
		
		//If debug mode is active, update all the debug rectangles to coincide with the node bounding boxes.
		if (debugMode) {
			ArrayList<Rectangle> newRects = new ArrayList<Rectangle>();
			for (Node bot : this.getChildren()) {
				if (bot instanceof AbstractPhysicsRobot) {
					if (debugRects.containsKey(bot)) {
						Rectangle tmp = debugRects.get(bot);
						tmp.setX(bot.getBoundsInParent().getMinX());
						tmp.setY(bot.getBoundsInParent().getMinY());
						tmp.setWidth(bot.getBoundsInParent().getWidth());
						tmp.setHeight(bot.getBoundsInParent().getHeight());
					} else {
						Rectangle tmp = new Rectangle();
						tmp.setX(bot.getBoundsInParent().getMinX());
						tmp.setY(bot.getBoundsInParent().getMinY());
						tmp.setWidth(bot.getBoundsInParent().getWidth());
						tmp.setHeight(bot.getBoundsInParent().getHeight());
						tmp.setFill(Color.TRANSPARENT);
						tmp.setStroke(Color.RED);
						tmp.setMouseTransparent(true);
						newRects.add(tmp);
						debugRects.put(bot, tmp);
					}
				}
			}
			this.getChildren().addAll(newRects);
			newRects.clear();
		}

	}

	/**
	 * Add a collision shape to the list of collisions.
	 * Deprecated by use of the jBox2d library.
	 * @deprecated
	 * @param collision
	 */
	public void addShape(Shape collision) {
		//newShapes.add(collision);
		collision.setVisible(true);
		collision.setMouseTransparent(true);
		collision.setStroke(Color.RED);
		collision.setFill(Color.TRANSPARENT);

	}
	
	
}
