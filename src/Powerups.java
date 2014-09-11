
package johnsontcs3finalproject;

import java.util.Random;
import java.awt.*;

/**
 * Implements the four kinds of game powerups.
 * @author timothy
 */
public class Powerups
{
    private static int radius = 10; //The radius of each powerup.
    private static Random generator = new Random(); //The random number generator used to generate the powerups.
    private static final int typesOfPowerups = 4; //The number of types of powerups.
    private int ID; //The ID number of each powerups.
    private int xLocation, yLocation; //The x and y location of each powerup on the game window.

    /**
     * Creates a new powerup, and its location.
     * Types of powerups:
     * ID = 1: Bullet-proof armor
     * ID = 2: Super speed
     * ID = 3: Anti-gravity
     * ID = 4: Invincibility
     */
    public Powerups()
    {
        xLocation = generator.nextInt(GameFrame.Width);
        yLocation = generator.nextInt(GameFrame.Height - 75);
        ID = generator.nextInt(typesOfPowerups) + 1;
        while ((Math.abs(xLocation - GameFrame.Width/2) < 50 && Math.abs(yLocation - GameFrame.Height/2) < 50))
        {
            xLocation = generator.nextInt(GameFrame.Width);
            yLocation = generator.nextInt(GameFrame.Height - 75);
        }
    }

    /**
     * Draws the powerup onto the game window.
     * @param page The page onto which all game objects are drawn.
     */
    public void Draw(Graphics page)
    {
        page.setColor(Color.WHITE);
        page.fillOval((int)(xLocation - radius + 0.5), (int)(yLocation - radius + 0.5), 2*radius, 2*radius);
    }

    /**
     *
     * @return Returns the x location of the powerup.
     */
    public double getXLocation()
    {
        return xLocation;
    }

    /**
     *
     * @return Returns the y location of the powerup.
     */
    public double getYLocation()
    {
        return yLocation;
    }

    /**
     *
     * @return Returns the ID number of the powerup.
     */
    public int getID()
    {
        return ID;
    }

    /**
     *
     * @return Returns the radius of the powerup.
     */
    public static int getSize()
    {
        return radius;
    }
}
