import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SimPanel extends Pane
{

	private boolean debug = false;
	HashMap<Node, Rectangle> debugRects = new HashMap<Node, Rectangle>();
	private ArrayList<Shape> newShapes = new ArrayList<Shape>();

	public SimPanel()
	{
		super();
		this.setStyle("-fx-background-color: black;");
		this.setPrefSize(800, 800);
	}

	public ArrayList<Node> getInBounds(Bounds bound)
	{
		//System.out.println("Robot" + bound);
		List<Node> children = this.getChildren();
		ArrayList<Node> results = new ArrayList<Node>();
		for (Node bot : children)
		{
			if (bot.getClass() == Robot.class || bot.getClass() == Wall.class)
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
		this.getChildren().addAll(newShapes);
		newShapes.clear();
		if (debug)
		{
			ArrayList<Rectangle> newRects = new ArrayList<Rectangle>();
			for (Node bot : this.getChildren())
			{
				if (bot.getClass() == Robot.class)
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
			if (bot.getClass() == Robot.class)
			{
				((Robot) bot).update();
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
