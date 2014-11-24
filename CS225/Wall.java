import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends Rectangle
{

	public Wall(double width, double height)
	{
		super(width, height);
		setFill(Color.WHITE);
		setStroke(Color.WHITE);

		// records relative x and y co-ordinates.
		class Delta
		{
			double x, y;
		}

		final Delta dragDelta = new Delta();
		setOnMousePressed(e ->
		{
			dragDelta.x = getLayoutX() - e.getSceneX();
			dragDelta.y = getLayoutY() - e.getSceneY();
			setCursor(Cursor.MOVE);
		});

		setOnMouseReleased(e ->
		{
			setCursor(Cursor.HAND);
		});

		setOnMouseDragged(e ->
		{
			setLayoutX(e.getSceneX() + dragDelta.x);
			setLayoutY(e.getSceneY() + dragDelta.y);

		});

		setOnMouseEntered(e ->
		{
			setCursor(Cursor.HAND);
		});
	}

}
