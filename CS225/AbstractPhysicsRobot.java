import java.util.ArrayList;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Abstract class for different types of robots to implement.
 * @author Chris
 *
 */
public abstract class AbstractPhysicsRobot extends Group {

	public static int numBots = 0;
	public int myNum;
	private PhysicsPane world;
	public RobotBrain brain;
	private Circle mainBody;
	private Arc vision;
	private Line dirLine;
	private boolean collide = false;
	public Body body;
	public boolean draw = true;

/**
 * Create a new physics based robot within the World world, with a brain.
 * @param world
 * @param brain
 */
	public AbstractPhysicsRobot(PhysicsPane world, RobotBrain brain) {
		numBots++;
		myNum = numBots;
		this.world = world;
		this.brain = brain;

		//Create a JavaFX circle to represent the robots body.
		mainBody = new Circle();
		mainBody.radiusProperty().bind(brain.radius);
		mainBody.centerXProperty().bind(brain.x);
		mainBody.centerYProperty().bind(brain.y);
		mainBody.fillProperty().set(new Color(0.5, 1, .6, 1));

		// Create an JBox2D body definition for ball.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(Utils.toPhysX(brain.x.floatValue()),
				Utils.toPhysY(brain.y.floatValue()));
		
		CircleShape cs = new CircleShape();
		cs.m_radius = Utils.pixToPhys(brain.radius.floatValue());

		// Create a fixture for ball
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 2f;
		fd.friction = 0.0f;
		fd.restitution = 0.0f;

		//Create body from the body definition, and apply the fixture definition to it.
		body = PhysicsPane.world.createBody(bd);
		body.createFixture(fd);
		body.setUserData(this); //Keep a reference to this object within the physics body.

		//Create an arc to represent the extent of the robots vision.
		vision = new Arc();
		vision.radiusXProperty().bind(brain.viewDistance);
		vision.radiusYProperty().bind(brain.viewDistance);
		vision.centerXProperty().bind(brain.x);
		vision.centerYProperty().bind(brain.y);
		vision.startAngleProperty().bind(brain.viewAngle.multiply(-0.5));
		vision.lengthProperty().bind(brain.viewAngle);
		vision.setFill(Color.TRANSPARENT);
		vision.setStroke(Color.ALICEBLUE);
		vision.setType(ArcType.ROUND);

		//Create a line to display which direction the robot is facing.
		dirLine = new Line();
		dirLine.startXProperty().bind(brain.x);
		dirLine.startYProperty().bind(brain.y);
		dirLine.endXProperty().bind(brain.x.add(brain.radius.doubleValue()));
		dirLine.endYProperty().bind(brain.y);
		dirLine.setStroke(Color.WHITE);

		//Create a rotation transformation to ensure the robot pivots on the Robot body axis, not the center of the Group node.
		Rotate rot = new Rotate();
		rot.angleProperty().bind(brain.dir);
		rot.pivotXProperty().bind(brain.x);
		rot.pivotYProperty().bind(brain.y);
		this.getTransforms().add(rot);

		//Add robot components to the group node.
		this.getChildren().addAll(mainBody, vision, dirLine);

		//Add the group node to the world.
		world.getChildren().add(this);
		
		//Internal class to keep track of the initial click delta distance
		//All click and drag code adapted from : 
		//			http://stackoverflow.com/questions/10682107/correct-way-to-move-a-node-by-dragging-in-javafx-2
		//	 Written by jewelsea
		class Delta {
			double x, y;
		}

		final Delta dragDelta = new Delta();
		setOnMousePressed(e -> {
			//Record the difference between the click location and the center of the object clicked.
			dragDelta.x = brain.x.get() - e.getSceneX();
			dragDelta.y = brain.y.get() - e.getSceneY();
			setCursor(Cursor.MOVE);
		});

		setOnMouseReleased(e -> {
			setCursor(Cursor.HAND); //Reset the cursor to hand, while still over the node
		});

		setOnMouseDragged(e -> {
			//Move the robot when the mouse is dragged.
			brain.x.set(e.getSceneX() + dragDelta.x);
			brain.y.set(e.getSceneY() + dragDelta.y);
			//Move the physics body in the simulation.
			body.setTransform(new Vec2(Utils.toPhysX(mainBody.getCenterX()),
					Utils.toPhysY(mainBody.getCenterY())), body.getAngle());

		});

		setOnMouseEntered(e -> {
			setCursor(Cursor.HAND);
		});

	}

	/**
	 * Get the list of nodes in the pane that fall within the robots vision arc.
	 * @return
	 */
	public ArrayList<Node> getVisible() {
		ArrayList<Node> visible = world.getInBounds(vision.localToScene(vision
				.getBoundsInLocal()));
		visible.remove(this);

		ArrayList<Node> remove = new ArrayList<Node>();
		for (Node bot : visible) {
			if (bot.getClass() == AbstractPhysicsRobot.class) {
				AbstractPhysicsRobot tmp = (AbstractPhysicsRobot) bot;
				Shape collision = Shape.intersect(tmp.mainBody, this.vision); //Check if the object intersects the vision arc.
				if (collision.getBoundsInLocal().getWidth() != -1) {
					// System.out.println("Vision occured!");
				} else {
					// If the object does not intersect, remove it from the scene.
					remove.add(bot);
				}
			}
		}
		for (Node bot : remove) { //Can't modify the loop that we were in, so we remove everything after the loop is complete.
			visible.remove(bot);
		}
		return visible;
	}

	/**
	 * Set the robot color.
	 * @param col
	 */
	public void setColor(Color col) {
		mainBody.setFill(col);
		
	}

	/**
	 * Update the robot JavaFX representation with the Physics object coordinates.
	 */
	public void update() {

		double x = brain.x.get();
		double y = brain.y.get();
		brain.x.set(Utils.toScreenX(body.getPosition().x));
		brain.y.set(Utils.toScreenY(body.getPosition().y));
		brain.dir.set(Math.toDegrees(body.getAngle()));
		
		if(draw){
		world.gc.setStroke(mainBody.getFill());
		world.gc.strokeLine(x, y, brain.x.get(), brain.y.get());
		}
		
		//Set behavior of robot based on if a collision flag is set.
		if (collide) {
			doCollision();
			collide = false;
		} else {
			doStep();
		}
		
	}
	
	/**
	 * Set collision flag to true.
	 */
	public void collide(){
		collide = true;
	}

	/**
	 * Behavior to be executed every step that the robot does not collide.
	 */
	public abstract void doStep();

	/**
	 * Behavior to be executed the step after the robot senses a collision in front of it.
	 */
	public abstract void doCollision();

	/**
	 * Returns a string object of the robot.
	 */
	public String toString() {
		return brain.x.doubleValue() + " and " + brain.y.doubleValue();
	}
	
	/**
	 * Behavior to move the robot forward at a given velocity.
	 * @param vel
	 */
	public void goForward(float vel){

		body.setAngularVelocity(0);
		body.setLinearVelocity(body.getLocalVector(new Vec2(vel,0)));
		//body.applyForceToCenter(body.getLocalVector(new Vec2(1,0)));
	}
	
	/**
	 * Behavior to move the robot backwards at a given velocity.
	 * @param vel
	 */
	public void goBackward(float vel){

		body.setAngularVelocity(0);
		body.setLinearVelocity(body.getLocalVector(new Vec2(-vel,0)));
		//body.applyForceToCenter(body.getLocalVector(new Vec2(1,0)));
	}

	/**
	 * Behavior to turn the robot right at an angular velocity.
	 * @param vel
	 */
	public void turnRight(float vel){

		body.setAngularVelocity(vel);
		body.setLinearVelocity(body.getLocalVector(new Vec2(1,0)));
		//body.applyTorque(-1);
	}

	/**
	 * Behavior to turn the robot left at an angular velocity.
	 * @param vel
	 */
	public void turnLeft(float vel){

		body.setAngularVelocity(-vel);
		body.setLinearVelocity(body.getLocalVector(new Vec2(1,0)));
		//body.applyTorque(-1);
	}

}
