
/**
 * Very simple robot that implements the Abstract robot class.
 * Robot will move forward until it collides with something, then turn right until there is no collision.
 * @author Chris
 *
 */
public class RightRobot extends AbstractPhysicsRobot {


	public RightRobot(PhysicsPane world, RobotBrain brain) {
		super(world, brain);

	}

	@Override
	public
	void doStep() {
		this.goForward(20);

	}

	@Override
	public
	void doCollision() {
		this.turnRight(5);
	}

}
