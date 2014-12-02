import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import com.sun.javafx.geom.*;

public class PhysicsBall extends Circle {

	private SimPanel world;

	private double vel, dir, lastX, lastY;
	Point2D center = new Point2D(0,0);
	
	public PhysicsBall(SimPanel world, double rad) {
		super(rad);
		this.world = world;
		vel = 2;
		dir = 0;
		
		class Delta {
			double x, y;
		}
		
		final Delta dragDelta = new Delta();
		setOnMousePressed(e -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.x = this.getCenterX() - e.getSceneX();
			dragDelta.y = this.getCenterY() - e.getSceneY();
			setCursor(Cursor.MOVE);
		});

		setOnMouseReleased(e -> {
			setCursor(Cursor.HAND);
		});

		setOnMouseDragged(e -> {
			this.setCenterX(e.getSceneX() + dragDelta.x);
			this.setCenterY(e.getSceneY() + dragDelta.y);
		});

		setOnMouseEntered(e -> {
			setCursor(Cursor.HAND);
		});
	}

	public void update(double timeStep) {
		
		lastX = getCenterX();
		lastY = getCenterY();

		// Calculate distance to move
		double xMove = vel * timeStep * Math.cos(Math.toRadians(dir));
		double yMove = vel * timeStep * Math.sin(Math.toRadians(dir));

		// Move distance
		this.setCenterX(lastX + xMove);
		this.setCenterY(lastY + yMove);
		
		
		// Check for collisions
		ArrayList<Shape> collisions = getCollisions();
		if (collisions.size() != 0) { //If there are some collisions
			for (Shape shapes : collisions) {  //For each collision
				Bounds bound = shapes.getLayoutBounds();
				double centerX = (bound.getMinX()+bound.getMaxX())/2;
				double centerY = (bound.getMinY()+bound.getMaxY())/2;
				Point2D yourCenter = new Point2D(centerX,centerY);
				Point2D myCenter = new Point2D(getCenterX(), getCenterY());
				//myCenter.angle(arg0)
				System.out.println(centerX + ", " + centerY);
			}
			this.setCenterX(lastX);
			this.setCenterY(lastY);
		}
		

	}

	public ArrayList<Shape> getCollisions() {

		//List of shapes we collided with
		ArrayList<Shape> keep = new ArrayList<Shape>();
		//List of the overlapping regions
		ArrayList<Shape> intersections = new ArrayList<Shape>();

		//Get shapes that our bounds are overlapping (possible collisions)
		ArrayList<Node> collisions = world.getInBounds(this.localToParent(this.getBoundsInLocal()));
		
		//Remove this object from that list (can't collide with self)
		collisions.remove(this);

		for (Node bot : collisions) { //For each node in the list returned
			if (bot instanceof Shape) { //If the node is a Shape object (other objects can't be collided)
				Path collision = Path.intersect((Shape) bot, this); //Find the intersection of our shapes
				
				if (collision.getBoundsInLocal().getHeight() != -1) { //If the intersection exists
					keep.add((Shape) bot);  //Add the shape to list of shapes
					intersections.add(collision); //Add the intersection region to list of regions
				} else {
					
				}
			} else {
				// remove.add(bot);
			}
		}
		// for (Node bot : remove) {
		// collisions.remove(bot);
		// }
		return intersections;
	}
}
