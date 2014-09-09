
package johnsontcs3finalproject;

import java.util.Random;
import java.awt.*;

/**
 * Implements the health powerup.
 * @author timothy
 */
public class Health 
{
    public static final int health = 25;
    private static Random generator;
    private int xLocation, yLocation;
    private static int radius = 10;

    /**
     * Determines the location of the health powerup, avoiding the black hole.
     *
     */
    public Health() 
    {
        generator = new Random();
        xLocation = generator.nextInt(GameFrame.Width);
        yLocation = generator.nextInt(GameFrame.Height - 75);
        
        while ((Math.abs(xLocation - GameFrame.Width/2) < 50 && Math.abs(yLocation - GameFrame.Height/2) < 50))
        {
            xLocation = generator.nextInt(GameFrame.Width);
            yLocation = generator.nextInt(GameFrame.Height - 75);
        }
    }

    /**
     * Draws the health powerup onto the game window.
     * @param page The page onto which all game objects are drawn.
     */
    public void Draw(Graphics page)
    {
        page.setColor(Color.RED);
        page.fillOval((int)(xLocation - radius + 0.5), (int)(yLocation - radius + 0.5), 2*radius, 2*radius);
    }

    /**
     *
     * @return Returns the x location of the health powerup.
     */
    public int getXLocation()
    {
        return xLocation;
    }

    /**
     *
     * @return Returns the y location of the health powerup.
     */
    public int getYLocation()
    {
        return yLocation;
    }

    /**
     *
     * @return Returns the radius of the health powerup.
     */
    public static int getSize()
    {
        return radius;
    }
}
