import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Physics enabled wall to deflect physics robots.
 * @author Chris
 *
 */
public class PhysicsWall extends Rectangle {
	private Body body;
	Vec2[] vertices;
	PolygonShape ps;
	FixtureDef fd;
	Circle corner, rotater;

	/**
	 * Create a wall with the given width and height at the location (posx,posy)
	 * @param width
	 * @param height
	 * @param posx
	 * @param posy
	 */
	public PhysicsWall(float width, float height, float posx, float posy) {
		//Create the JavaFX rectangle shape node.
		super(width, height);
		setFill(Color.WHITE);
		setStroke(Color.WHITE);
		setLayoutX(posx);
		setLayoutY(posy);

		// Create an JBox2D body definition for the wall.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position.set(Utils.toPhysX(posx + width / 2),
				Utils.toPhysY(posy + height / 2));

		//Create a rectangle shape for the wall
		ps = new PolygonShape();
		ps.setAsBox(Utils.pixToPhys(width / 2), Utils.pixToPhys(height / 2));
		
		// Create a physics fixture for the wall (only friction matters as the body is static and will not move)
		fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 2f;
		fd.friction = 0.3f;
		fd.restitution = 0f;

		//Create physics body
		body = PhysicsPane.world.createBody(bd);
		body.createFixture(fd);
		body.setUserData(this);

		//Records relative x and y coordinates.
		class Delta {
			double x, y;
		}

		final Delta dragDelta = new Delta();
		//Create mouse actions to allow dragging walls around, and resizing walls.
		setOnMousePressed(e -> {

			vertices = ps.getVertices();
			
			if (corner == null) {
				corner = new Circle(10);
				corner.setFill(Color.WHITE);
				RobotProgram.main.getChildren().add(corner);
			}
//			if (rotater == null) {
//				rotater = new Circle(10);
//				rotater.setFill(Color.WHITE);
//				RobotProgram.main.getChildren().add(rotater);
//			}

			Vec2 cornerPos = body.getWorldPoint(vertices[1]);
			corner.setCenterX(Utils.toScreenX(cornerPos.x));
			corner.setCenterY(Utils.toScreenY(cornerPos.y));
			corner.setVisible(true);

//			Vec2 rotaterPos = body.getWorldPoint(vertices[1].clone().addLocal(4, vertices[1].y));
//			rotater.setCenterX(Utils.toScreenX(rotaterPos.x));
//			rotater.setCenterY(Utils.toScreenY(rotaterPos.y));
//					//)().y));

			final Delta cornerDelta = new Delta();
			//final Delta rotateDelta = new Delta();
			
			//Resize wall by dragging the corner circle.
			corner.setOnMousePressed(f -> {
				// System.out.println(getLayoutX());
				cornerDelta.x = corner.getCenterX() - f.getSceneX();
				cornerDelta.y = corner.getCenterY() - f.getSceneY();

			});
			corner.setOnMouseDragged(f -> {
				Vec2 cornerCenter = new Vec2(
						Utils.toPhysX(f.getSceneX()+cornerDelta.x), Utils
								.toPhysY(f.getSceneY()+cornerDelta.y));
				//System.out.println(cornerCenter);
				cornerCenter = body.getLocalPoint(cornerCenter);
				//System.out.println(cornerCenter);
				if (cornerCenter.x > ps.getVertex(3).x) {
					corner.setCenterX(f.getSceneX() + cornerDelta.x);
					this.setWidth(corner.getCenterX() - this.getLayoutX());
				}
				if (cornerCenter.y < ps.getVertex(3).y) {
					corner.setCenterY(f.getSceneY() + cornerDelta.y);
					this.setHeight(corner.getCenterY() - this.getLayoutY());
				}

			});
			corner.setOnMouseReleased(f -> {
				setCursor(Cursor.HAND);
				ps.setAsBox(Utils.pixToPhys(this.getWidth() / 2),
						Utils.pixToPhys(this.getHeight() / 2));
				body.destroyFixture(body.getFixtureList());
				body.createFixture(fd);
				body.setTransform(
						new Vec2(Utils.toPhysX(getLayoutX() + getWidth() / 2),
								Utils.toPhysY(getLayoutY() + getHeight() / 2)),
						0f);
				Vec2 cornerPos2 = body.getWorldPoint(vertices[1]);
				corner.setCenterX(Utils.toScreenX(cornerPos2.x));
				corner.setCenterY(Utils.toScreenY(cornerPos2.y));
			});

			// Rotating walls caused resizing to not work correctly..
			//TODO : Fix this later.
			
//			rotater.setOnMousePressed(f -> {
//				// System.out.println(getLayoutX());
//				rotateDelta.x = rotater.getCenterX() - f.getSceneX();
//				rotateDelta.y = rotater.getCenterY() - f.getSceneY();
//
//			});
//			rotater.setOnMouseDragged(f -> {
//				rotater.setCenterX(f.getSceneX() + rotateDelta.x);
//				rotater.setCenterY(f.getSceneY() + rotateDelta.y);
//				body.setTransform(
//						new Vec2(Utils.toPhysX(getLayoutX() + getWidth() / 2),
//								Utils.toPhysY(getLayoutY() + getHeight() / 2)),
//						(float) -Math.atan2(
//								rotater.getCenterY()
//										- Utils.toScreenY(body.getPosition().y),
//								rotater.getCenterX()
//										- Utils.toScreenX(body.getPosition().x)));
//				this.setRotate(-Math.toDegrees(body.getAngle()));
//
//			});
//			rotater.setOnMouseReleased(f -> {
//				setCursor(Cursor.HAND);
//				body.setTransform(
//						new Vec2(Utils.toPhysX(getLayoutX() + getWidth() / 2),
//								Utils.toPhysY(getLayoutY() + getHeight() / 2)),
//						(float) -Math.atan2(
//								rotater.getCenterY()
//										- Utils.toScreenY(body.getPosition().y),
//								rotater.getCenterX()
//										- Utils.toScreenX(body.getPosition().x)));
//				this.setRotate(-Math.toDegrees(body.getAngle()));
//			});

			dragDelta.x = getLayoutX() - e.getSceneX();
			dragDelta.y = getLayoutY() - e.getSceneY();
			setCursor(Cursor.MOVE);
		});

		setOnMouseReleased(e -> {
			setCursor(Cursor.HAND);
			// System.out.println(body.getPosition());
		});

		setOnMouseDragged(e -> {
			setLayoutX(e.getSceneX() + dragDelta.x);
			setLayoutY(e.getSceneY() + dragDelta.y);
			body.setTransform(
					new Vec2(Utils.toPhysX(getLayoutX() + getWidth() / 2),
							Utils.toPhysY(getLayoutY() + getHeight() / 2)), 0f);
			if (corner != null) {
				corner.setVisible(false);
			}

		});

		setOnMouseEntered(e -> {
			setCursor(Cursor.HAND);
		});
		
	}

	/**
	 * Set the color of the wall.
	 * @param value
	 */
	public void setColor(Color value) {
		this.setFill(value);
	}

}
