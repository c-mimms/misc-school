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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import prePhysics.SimPanel;

public class AbstractPhysicsRobot extends Group {

	public static int numBots = 0;
	private int myNum;
	private PhysicsPane world;
	public RobotBrain brain;
	private Circle mainBody;
	private Rectangle bodyBounds, bigBounds;
	private Arc vision;
	private Line dirLine;
	private boolean collide = false;
	private Body body;

	// public Group body;

	public AbstractPhysicsRobot(PhysicsPane world, RobotBrain brain) {
		numBots++;
		myNum = numBots;
		this.world = world;
		this.brain = brain;

		mainBody = new Circle();
		mainBody.radiusProperty().bind(brain.radius);
		mainBody.centerXProperty().bind(brain.x);
		mainBody.centerYProperty().bind(brain.y);
		mainBody.fillProperty().set(new Color(0.5, 1, .6, 1));

		// Create an JBox2D body defination for ball.
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
		fd.friction = 0.3f;
		fd.restitution = 0.3f;

		/*
		 * Virtual invisible JBox2D body of ball. Bodies have velocity and
		 * position. Forces, torques, and impulses can be applied to these
		 * bodies.
		 */
		body = PhysicsPane.world.createBody(bd);
		body.createFixture(fd);

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
			System.out.println(body.getPosition());
		});

		setOnMouseDragged(e -> {
			brain.x.set(e.getSceneX() + dragDelta.x);
			brain.y.set(e.getSceneY() + dragDelta.y);
			body.setTransform(new Vec2(Utils.toPhysX(mainBody.getCenterX()),
					Utils.toPhysY(mainBody.getCenterY())), 0f);

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
			if (bot.getClass() == AbstractPhysicsRobot.class) {
				AbstractPhysicsRobot tmp = (AbstractPhysicsRobot) bot;
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

	public void setColor(Color col) {
		mainBody.setFill(col);
	}

	public void update() {

		brain.x.set(Utils.toScreenX(body.getPosition().x));
		brain.y.set(Utils.toScreenY(body.getPosition().y));
		brain.dir.set(Math.toDegrees(-body.getAngle()));
	}

	public String toString() {
		return brain.x.doubleValue() + " and " + brain.y.doubleValue();
	}

}
