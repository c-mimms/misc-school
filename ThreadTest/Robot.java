
public class Robot{

	SimWorld world;
	int leftMotor = 0;
	int rightMotor = 0;
	long lastTime = 0;
	
	public Robot(SimWorld theWorld){
		world = theWorld;
	}
	
	//public abstract void loop();
	
	void goForward(){
		leftMotor = 1;
		rightMotor = 1;
	}
	
	void goBackward(){
		leftMotor = -1;
		rightMotor = -1;
	}
	
	void pivotRight(){
		leftMotor = 1;
		rightMotor = -1;
	}
	
	void pivotLeft(){
		leftMotor = -1;
		rightMotor = 1;
	}
	
	public int[] getMotors(){
		return new int[]{leftMotor, rightMotor};
	}
	
	boolean leftBump(){
		return world.leftBump(this);
	}
	
	void update(long time){
		if(time > lastTime){
			lastTime = time;
		}
	}
	
	void loveYou(long time){
		System.out.println("I love you! " + time);
	}
}
