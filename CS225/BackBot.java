/**
 * Very simple robot that implements the Abstract robot class.
 * Robot will move forward until it collides with something, then turn left until there is no collision.
 * @author Chris
 *
 */
public class BackBot extends AbstractPhysicsRobot {
	
	public BackBot(PhysicsPane world, RobotBrain brain) {
		super(world, brain);

	}

	@Override
	public
	void doStep() {
		
		//Add code here to be run when there are no collisions
		//Available functions are:
		this.goForward(10);
		
	}

	@Override
	public
	void doCollision() {

		//Add code here to be run when there are collisions
		this.goBackward(10);
	}

}
