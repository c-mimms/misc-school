


/**
 * Utility class to provide conversions between JavaFX screen coordinates and physics world coordinates.
 * @author Chris
 * Collaborator: 
 * 		This class is a heavily modified version of the Utils class found at 
 * 				http://thisiswhatiknowabout.blogspot.com/2011/11/hello-world-jbox2d-with-javafx-20.html
 * 		which was written by Dilip
 */
public class Utils {
    
    //Screen width and height
    public static int WIDTH = 800;
    public static int HEIGHT = 800;
    


    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
    public static float toScreenX(float posX) {
        float x = WIDTH*posX / 100.0f;
        return x;
    }
    public static float toScreenX(double posX) {
        float x = WIDTH*(float)posX / 100.0f;
        return x;
    }
    
    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    public static float toScreenY(double posY) {
        float y = HEIGHT - (1.0f*HEIGHT) * (float)posY / 100.0f;
        return y;
    }
    public static float toScreenY(float posY) {
        float y = HEIGHT - (1.0f*HEIGHT) * posY / 100.0f;
        return y;
    }
    
    
    

    //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
    public static float toPhysX(float posX) {
        float x =   (posX*100.0f)/WIDTH;
        return x;
    }  
    public static float toPhysX(double posX) {
        float x =   ((float)posX*100.0f)/WIDTH;
        return x;
    }
    
    
    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public static float toPhysY(float posY) {
        float y = 100.0f - ((posY * 100f) /HEIGHT) ;
        return y;
    }
    public static float toPhysY(double posY) {
        float y = 100.0f - (((float)posY * 100f) /HEIGHT) ;
        return y;
    }

    
    
    /**
     * Convert a JavaFX pixel distance to a JBox2D distance.
     * @param pixelSize
     * @return
     */
    public static float pixToPhys(float pixelSize){
    	return pixelSize*100f/WIDTH;
    }
    public static float pixToPhys(double pixelSize){
    	return (float)pixelSize*100f/WIDTH;
    }

    /**
     * Convert a JBox2d distance to a pixel distance.
     * @param physSize
     * @return
     */
    public static float physToPix(float physSize){
    	return physSize*WIDTH/100f;
    }

    public static float physToPix(double physSize){
    	return (float)physSize*WIDTH/100f;
    }
 
}
