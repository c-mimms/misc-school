
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;



import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jbox2d.dynamics.*;

public class PhysicsWall extends Rectangle
{
	private Body body;
	private float width, height;

	public PhysicsWall(float width, float height, float posx, float posy)
	{
		super(width, height);
		this.width = width;
		this.height = height;
		setFill(Color.WHITE);
		setStroke(Color.WHITE);
		setLayoutX(posx);
		setLayoutY(posy);
		
		
		//Create an JBox2D body defination for the wall.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Utils.toPhysX(posx), Utils.toPhysY(posy));

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(Utils.pixToPhys(width/2), Utils.pixToPhys(height/2));
        for(Vec2 vert : ps.getVertices()){
        	System.out.println(vert);
        }
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 2f;
        fd.friction = 0.3f;        
        fd.restitution = 0f;

        /*
        * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
        * Forces, torques, and impulses can be applied to these bodies.
        */
        body = PhysicsPane.world.createBody(bd);
        body.createFixture(fd);

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
	        System.out.println(body.getPosition());
		});

		setOnMouseDragged(e ->
		{
			setLayoutX(e.getSceneX() + dragDelta.x);
			setLayoutY(e.getSceneY() + dragDelta.y);
			body.setTransform(new Vec2(Utils.toPhysX(getLayoutX()+width/2),Utils.toPhysY(getLayoutY()+height/2)), 0f);

		});

		setOnMouseEntered(e ->
		{
			setCursor(Cursor.HAND);
		});
	}

}
