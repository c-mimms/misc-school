import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class SimWorld extends Pane{

	ArrayList<Robot> robots = new ArrayList<Robot>();
	HashMap<Robot, RobotDraw> robotNodes = new HashMap<Robot, RobotDraw>();
	
	public boolean leftBump(Robot rob){
		return false;
	}
	
	public boolean rightBump(Robot rob){
		return false;
	}
	
	public SimWorld(){
		super();
	}

	public void addRobot(Robot rob){
		robots.add(rob);

		RobotDraw tmp = new RobotDraw();
		tmp.layoutXProperty().set(150);
		tmp.layoutYProperty().set(150);
		tmp.setRotate(90);
		this.getChildren().add(tmp);
		robotNodes.put(rob, tmp);
	}
	
	public void removeRobot(Robot rob){
		robots.remove(rob);
	}
	
	public void update(long time){
		 for (Robot robot : robots) {
			robot.update(time);
			int[] motors = robot.getMotors();
			Rectangle tmp = robotNodes.get(robot);
			
			if(!(tmp.getBoundsInParent().getMaxX()>this.getWidth())){
				tmp.setLayoutX(tmp.layoutXProperty().doubleValue() + motors[0]);
			}
			
			
//			for (Map.Entry<String, Object> entry : map.entrySet()) {
//			    String key = entry.getKey();
//			    Object value = entry.getValue();
//			}
		}
	}
	
}
