
package johnsontcs3finalproject;

import java.awt.*;

/**
 * Implements the bullets fired by each spaceship.
 * @author timothy
 */
public class Bullet 
{
    private double xInit, yInit, xLocation, yLocation, xSpeed, ySpeed;
    private int angle;
    private static final double initSpeed = 20;

    /**
     * Initializes the initial location, current location, angle, and speed of the bullet.
     * @param xLoc The initial x location.
     * @param yLoc The initial y location.
     * @param Angle The angle at which the bullet is fired.
     */
    public Bullet(double xLoc, double yLoc, int Angle)
    {
        xInit = xLoc;
        yInit = yLoc;
        xLocation = xLoc;
        yLocation = yLoc;
        angle = Angle;
        xSpeed = initSpeed * Tables.cos[angle];
        ySpeed = initSpeed * Tables.sin[angle];
    }   

    /**
     * Moves the bullet each time the game is updated.
     *
     */
    public void Move()
    {
        xLocation += xSpeed;
        if (xLocation < 0)
            xLocation += GameFrame.Width;
        else if (xLocation >= GameFrame.Width)
            xLocation -= GameFrame.Width;
        
        yLocation += ySpeed;
        if (yLocation < 0)
            yLocation += GameFrame.Height;
        else if (yLocation >= GameFrame.Height)
            yLocation -= GameFrame.Height;
    }

    /**
     * Draws the bullet on the window.
     * @param page The page onto which all objects are drawn.
     */
    public void Draw(Graphics page)
    {
        page.setColor(Color.orange);
        page.fillOval((int)(xLocation+0.5), (int)(yLocation+0.5), 5, 5);
    }

    /**
     * Determines when the bullet has reached its maximum range.
     * @return
     */
    public boolean reachedRange()
    {
        return (Math.hypot((xLocation - xInit), (yLocation-yInit)) > 200);
    }

    /**
     *
     * @return Returns the current x location of the bullet.
     */
    public double getXLocation()
    {
        return xLocation;
    }

    /**
     *
     * @return Returns the current y location of the bullet.
     */
    public double getYLocation()
    {
        return yLocation;
    }
}
