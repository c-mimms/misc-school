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

public class RightRobot extends AbstractRobot
{

	
	
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

	public RightRobot(SimPanel world, RobotBrain brain)
	{
		super(world,brain);
		
	}

	@Override
	void doStep() {
		this.moveForward();
		
	}

	@Override
	void doCollision() {
		this.rotateRight();
	}

	

}
