import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import prePhysics.AbstractRobot;
import prePhysics.PhysicsBall;
import prePhysics.Wall;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PhysicsPane extends Pane
{

	private boolean debug = false;
	HashMap<Node, Rectangle> debugRects = new HashMap<Node, Rectangle>();
	private ArrayList<Shape> newShapes = new ArrayList<Shape>();
	public static World world;
	public static int height = 800;
	public static int width = 600;

	public PhysicsPane()
	{
		super();
		this.setStyle("-fx-background-color: black;");
		this.setPrefSize(800, 800);
	    world = new World(new Vec2(0.0f, -10f));
	    world.setAllowSleep(false);
	}

	public ArrayList<Node> getInBounds(Bounds bound)
	{
		//System.out.println("Robot" + bound);
		List<Node> children = this.getChildren();
		ArrayList<Node> results = new ArrayList<Node>();
		for (Node bot : children)
		{
			if (bot instanceof AbstractRobot || bot instanceof Wall)
			{

				//System.out.println(bot.getLayoutX() + " " + bot.getBoundsInParent());
				if (bot.getBoundsInParent().intersects(bound))
				{

					results.add(bot);
					//System.out.println(bot.getBoundsInParent().getMaxY());
				}
			}
		}
		return results;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
		if (debug)
		{
			for (Node item : debugRects.values())
			{
				item.setVisible(true);
			}
		}
		else
		{
			for (Node item : debugRects.values())
			{
				item.setVisible(false);
			}
		}

	}

	public void toggleDebug()
	{
		debug = !debug;
		if (debug)
		{
			for (Node item : debugRects.values())
			{
				item.setVisible(true);
			}
		}
		else
		{
			for (Node item : debugRects.values())
			{
				item.setVisible(false);
			}
		}

	}

	public void update()
	{

		world.step(1.0f/60.f, 8, 3); 
		
		this.getChildren().addAll(newShapes);
		newShapes.clear();
		if (debug)
		{
			ArrayList<Rectangle> newRects = new ArrayList<Rectangle>();
			for (Node bot : this.getChildren())
			{
				if (bot instanceof AbstractRobot)
				{
					if (debugRects.containsKey(bot))
					{
						Rectangle tmp = debugRects.get(bot);
						tmp.setX(bot.getBoundsInParent().getMinX());
						tmp.setY(bot.getBoundsInParent().getMinY());
						tmp.setWidth(bot.getBoundsInParent().getWidth());
						tmp.setHeight(bot.getBoundsInParent().getHeight());
					}
					else
					{
						Rectangle tmp = new Rectangle();
						tmp.setX(bot.getBoundsInParent().getMinX());
						tmp.setY(bot.getBoundsInParent().getMinY());
						tmp.setWidth(bot.getBoundsInParent().getWidth());
						tmp.setHeight(bot.getBoundsInParent().getHeight());
						tmp.setFill(Color.TRANSPARENT);
						tmp.setStroke(Color.RED);
						tmp.setMouseTransparent(true);
						newRects.add(tmp);
						debugRects.put(bot, tmp);
					}
				}
			}
			this.getChildren().addAll(newRects);
			newRects.clear();
		}

		for (Node bot : this.getChildren())
		{
			if (bot instanceof AbstractPhysicsRobot)
			{
				((AbstractPhysicsRobot) bot).update();
			}
			if (bot instanceof PhysicsBall)
			{
				((PhysicsBall) bot).update(.1);
			}
		}
	}

	public void addShape(Shape collision)
	{
		newShapes.add(collision);
		collision.setVisible(true);
		collision.setMouseTransparent(true);
		collision.setStroke(Color.RED);
		collision.setFill(Color.TRANSPARENT);

	}

}
