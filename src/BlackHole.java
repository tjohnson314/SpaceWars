
package johnsontcs3finalproject;

import java.awt.*;

/**
 * Implements the black hole object for the game.
 * As in the real world, the gravitational force is inversely proportional to the
 * square of the distance.
 * @author timothy
 */
public class BlackHole 
{
    private final double xLocation, yLocation; //The location of the black hole.
    private final static double Gravity = 10; //The gravitational constant of the black hole.
    private final static int damage = 5; //The damage done when a spaceship is caught in the black hole.
    private final static int radius = 20; //The radius of the black hole in pixels.

    /**
     * Initializes the location of the black hole in the center of the game window.
     *
     */
    public BlackHole()
    {
        xLocation = (double)GameFrame.Width/2; 
        yLocation = (double)GameFrame.Height/2;
    }

    /**
     * Draws the black hole onto the game window.
     * @param page The page onto which all objects are drawn.
     */
    public void Draw(Graphics page)
    {
        page.setColor(Color.BLACK);
        page.fillOval((int)(xLocation - radius + 0.5), (int)(yLocation - radius + 0.5), 2*radius, 2*radius);
    }

    /**
     *
     * @return Returns the radius of the black hole.
     */
    public static int getSize()
    {
        return radius;
    }

    /**
     *
     * @return Returns the damage done by the black hole.
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     *
     * @return Returns the x location of the black hole.
     */
    public double getXLocation()
    {
        return xLocation;
    }

    /**
     *
     * @return Returns the y location of the black hole.
     */
    public double getYLocation()
    {
        return yLocation;
    }

    /**
     * Determines the x acceleration from the black hole.
     * @param xLoc The x location of a spaceship.
     * @param yLoc The y location of a spaceship.
     * @return Returns the x acceleration from the black hole.
     */
    public double xAccelerate(double xLoc, double yLoc)
    {
        double deltaX = xLocation - xLoc;
        double deltaY = yLocation - yLoc;
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

        if(distance > radius)
            return Gravity*deltaX/(deltaX*deltaX + deltaY*deltaY);
        else
            return Gravity*deltaX/(radius*radius);

    }

    /**
     * Determines the y acceleration from the black hole.
     * @param xLoc The x location of a spaceship.
     * @param yLoc The y location of a spaceship.
     * @return Returns the y acceleration from the black hole.
     */
    public double yAccelerate(double xLoc, double yLoc)
    {
        double deltaX = xLoc - xLocation;
        double deltaY = yLoc - yLocation;
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        
        if(distance > radius)
            return Gravity*deltaY/(deltaX*deltaX + deltaY*deltaY + Math.ulp(0));
        else
            return Gravity*deltaY/(radius*radius);
    }
}
