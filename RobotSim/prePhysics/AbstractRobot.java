package prePhysics;

import RobotBrain;

import java.util.ArrayList;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public abstract class AbstractRobot extends Group {

	public static int numBots = 0;
	private int myNum;
	private SimPanel world;
	public RobotBrain brain;
	private Circle mainBody;
	private Rectangle bodyBounds, bigBounds;
	private Arc vision;
	private Line dirLine;
	private boolean collide = false;

	// public Group body;

	public AbstractRobot(SimPanel world, RobotBrain brain) {
		numBots++;
		myNum = numBots;
		this.world = world;
		this.brain = brain;

		mainBody = new Circle();
		mainBody.radiusProperty().bind(brain.radius);
		mainBody.centerXProperty().bind(brain.x);
		mainBody.centerYProperty().bind(brain.y);
		mainBody.fillProperty().set(new Color(0.5, 1, .6, 1));

		bodyBounds = new Rectangle();
		bodyBounds.widthProperty().bind(mainBody.radiusProperty().multiply(2));
		bodyBounds.heightProperty().bind(mainBody.radiusProperty().multiply(2));
		bodyBounds.setFill(Color.TRANSPARENT);
		bodyBounds.setStroke(Color.WHITE);
		bodyBounds.layoutXProperty().bind(
				mainBody.centerXProperty().subtract(mainBody.radiusProperty()));
		bodyBounds.layoutYProperty().bind(
				mainBody.centerYProperty().subtract(mainBody.radiusProperty()));
		// bodyBounds.setVisible(false);

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

		dirLine = new Line();
		dirLine.startXProperty().bind(brain.x);
		dirLine.startYProperty().bind(brain.y);
		dirLine.endXProperty().bind(brain.x.add(brain.radius.doubleValue()));
		dirLine.endYProperty().bind(brain.y);
		dirLine.setStroke(Color.WHITE);

		Rotate rot = new Rotate();
		rot.angleProperty().bind(brain.dir);
		rot.pivotXProperty().bind(brain.x);
		rot.pivotYProperty().bind(brain.y);

		this.getTransforms().add(rot);

		// This part is correct
		this.getChildren().addAll(mainBody, bodyBounds, vision, dirLine);
		// this.layoutXProperty().bind(
		// mainBody.centerXProperty().subtract(mainBody.radiusProperty()));
		// this.layoutYProperty().bind(
		// mainBody.centerYProperty().subtract(mainBody.radiusProperty()));

		world.getChildren().add(this);
		// System.out.println(this.getBoundsInParent());

		class Delta {
			double x, y;
		}

		final Delta dragDelta = new Delta();
		setOnMousePressed(e -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.x = brain.x.get() - e.getSceneX();
			dragDelta.y = brain.y.get() - e.getSceneY();
			setCursor(Cursor.MOVE);
		});

		setOnMouseReleased(e -> {
			setCursor(Cursor.HAND);
		});

		setOnMouseDragged(e -> {
			brain.x.set(e.getSceneX() + dragDelta.x);
			brain.y.set(e.getSceneY() + dragDelta.y);
		});

		setOnMouseEntered(e -> {
			setCursor(Cursor.HAND);
		});

	}

	public ArrayList<Node> getVisible() {
		ArrayList<Node> visible = world.getInBounds(vision.localToScene(vision
				.getBoundsInLocal()));
		visible.remove(this);

		ArrayList<Node> remove = new ArrayList<Node>();
		for (Node bot : visible) {
			if (bot.getClass() == AbstractRobot.class) {
				AbstractRobot tmp = (AbstractRobot) bot;
				Shape collision = Shape.intersect(tmp.mainBody, this.vision);
				if (collision.getBoundsInLocal().getWidth() != -1) {
					// System.out.println("Vision occured!");
				} else {
					// System.out.println(collision);
					remove.add(bot);
				}
			}
		}
		for (Node bot : remove) {
			visible.remove(bot);
		}
		return visible;
	}

	public ArrayList<Node> getCollisions() {

		ArrayList<Node> remove = new ArrayList<Node>();

		// System.out.println(bodyBounds.localToParent(bodyBounds.getBoundsInLocal()).getMinY());

		ArrayList<Node> collisions = world.getInBounds(bodyBounds
				.localToParent(bodyBounds.getBoundsInLocal()));

		collisions.remove(this);

		for (Node bot : collisions) {
			if (bot instanceof AbstractRobot) {
				AbstractRobot tmp = (AbstractRobot) bot;
				Shape collision = Shape.intersect(tmp.mainBody, this.mainBody);
				if (collision.getBoundsInLocal().getHeight() != -1) {
				} else {
					remove.add(bot);
				}
			}
			if (bot.getClass() == Rectangle.class) {
				Wall tmp = (Wall) bot;
				Shape collision = Shape.intersect(this.mainBody, tmp);
				if (collision.getBoundsInLocal().getHeight() != -1) {
					// world.addShape(collision);
					// System.out.println(collision.getBoundsInLocal().getWidth());
					// System.out.println(collision.getBoundsInLocal().getHeight());
					// System.out.println(tmp);
				} else {
					// System.out.println(collision.getBoundsInLocal().getWidth());
					// System.out.println(collision.getBoundsInLocal().getHeight());
					// System.out.println(tmp);
					remove.add(bot);
				}
			}
		}
		for (Node bot : remove) {
			collisions.remove(bot);
		}
		return collisions;
	}

	public void setColor(Color col) {
		mainBody.setFill(col);
	}

	public void update() {

		if (collide) {
			// System.out.println("Collided");
			doCollision();
		} else {
			doStep();
		}

		ArrayList<Node> collisions = getCollisions();
		ArrayList<Node> vision = getVisible();

		// If there are no collisions, we are not colliding with anything
		if (collisions.size() == 0) {
			// System.out.println("No collisions");
			moveForward(); // Check if moving forward will cause a collision.
			collisions = getCollisions();
			if (collisions.size() == 0) // Yay it didnt!
			{
				// System.out.println("Still no collisions");
				collide = false;
			} else {
				// System.out.println("Now there is collisions");
				collide = true;
			}
			moveBackward(); // Move back
		} else {
			moveForward(); // Check if moving forward will fix it.
			collisions = getCollisions();
			if (collisions.size() == 0) // Yay it did!
			{
				// System.out.println("Fixed forward");
				collide = false;
				moveBackward();
			} else {
				while (collide) // Resolve collisions by moving back until there
								// are no collisions
				{
					collisions = getCollisions();
					if (collisions.size() == 0) {
						// System.out.println("Fixed back");
						collide = false;
					} else {
						moveBack();
						// System.out.println(collisions.get(0).getClass());
					}
				}

				collide = true;
			}
		}

	}

	abstract void doStep();

	abstract void doCollision();

	public void rotateLeft() {
		brain.dir.set(brain.dir.doubleValue() - brain.vel.doubleValue());

	}

	public void rotateRight() {
		brain.dir.set(brain.dir.doubleValue() + brain.vel.doubleValue());

	}

	public void moveForward() {
		// TODO Auto-generated method stub
		double xVel = brain.vel.doubleValue()
				* Math.cos(Math.toRadians(brain.dir.doubleValue()));
		double yVel = brain.vel.doubleValue()
				* Math.sin(Math.toRadians(brain.dir.doubleValue()));
		brain.x.set(brain.x.doubleValue() + xVel);
		brain.y.set(brain.y.doubleValue() + yVel);
	}

	public void moveBackward() {
		// TODO Auto-generated method stub
		double xVel = -brain.vel.doubleValue()
				* Math.cos(Math.toRadians(brain.dir.doubleValue()));
		double yVel = -brain.vel.doubleValue()
				* Math.sin(Math.toRadians(brain.dir.doubleValue()));
		brain.x.set(brain.x.doubleValue() + xVel);
		brain.y.set(brain.y.doubleValue() + yVel);
	}

	private void moveBack() {
		// TODO Auto-generated method stub
		double xVel = -1 * Math.cos(Math.toRadians(brain.dir.doubleValue()));
		double yVel = -1 * Math.sin(Math.toRadians(brain.dir.doubleValue()));
		brain.x.set(brain.x.doubleValue() + xVel);
		brain.y.set(brain.y.doubleValue() + yVel);
	}

	public String toString() {
		return brain.x.doubleValue() + " and " + brain.y.doubleValue();
	}

}
