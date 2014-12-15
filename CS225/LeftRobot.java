
/**
 * Very simple robot that implements the Abstract robot class.
 * Robot will move forward until it collides with something, then turn left until there is no collision.
 * @author Chris
 *
 */
public class LeftRobot extends AbstractPhysicsRobot {


	public LeftRobot(PhysicsPane world, RobotBrain brain) {
		super(world, brain);

	}

	@Override
	public
	void doStep() {
		this.goForward(5);

	}

	@Override
	public
	void doCollision() {
		this.turnLeft(5);
	}

}
