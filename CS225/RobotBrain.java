
import javafx.beans.property.SimpleDoubleProperty;

public class RobotBrain {

	SimpleDoubleProperty x, y, radius, viewDistance, viewAngle, dir, vel;

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param viewDistance
	 * @param viewAngle
	 * @param dir
	 * @param vel
	 */
	public RobotBrain(double x, double y,
			double radius, double viewDistance,
			double viewAngle, double dir, double vel) {
		super();
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		this.radius = new SimpleDoubleProperty(radius);
		this.viewDistance = new SimpleDoubleProperty(viewDistance);
		this.viewAngle = new SimpleDoubleProperty(viewAngle);
		this.dir = new SimpleDoubleProperty(dir);
		this.vel = new SimpleDoubleProperty(vel);
	}

	
	
	

}
