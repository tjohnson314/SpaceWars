
package johnsontcs3finalproject;

import java.util.Random;
import java.awt.*;

/**
 * Determines the strategy of the AI player.
 * @author timothy
 */
public class AI extends Spaceship
{
    private static Random generator = new Random(); //The random number generator for the random flight of the AI player.
    private double xDestination, yDestination; //The x and y location on the game window to which the AI's spaceship moves.
    private static double xPoints[] = new double[5]; //The x location of the centers of each of the five areas of the window.
    private static double yPoints[] = new double[5]; //The y location of the centers of each of the five areas of the window.
    private int destination; //The region of the window to which the AI's spaceship is moving.
    private boolean holeProximity; //True if the AI player is dangerously close to the black hole.
    private int safeDistance = 75;  //Minimum safe distance from black hole in x and y-coordinate.
    private int maxDiffAngle = 10;

    /**
     * Initializes the AI player's spaceship.
     *
     */
    public AI(double xLoc, double yLoc, int angle) 
    {
        super(-1, xLoc, yLoc, angle);
        destination = 1;
        initPoints();
    }

    /**
     * Initializes the centers of each of the five regions of the game window.
     * Region 1 is the left 2/5 of the window.  Region 2 is across the top.
     * Region 3 contains the black hole in the center.
     * Region 4 is across the bottom.  Region 5 is the right 2/5 of the window.
     *
     */
    private void initPoints()
    {
        xPoints[0] = GameFrame.Width/5;
        yPoints[0] = GameFrame.Height/2;

        xPoints[1] = GameFrame.Width/2;
        yPoints[1] = GameFrame.Height/5;

        xPoints[2] = GameFrame.Width/2;
        yPoints[2] = GameFrame.Height/2;

        xPoints[3] = GameFrame.Width/2;
        yPoints[3] = 4*GameFrame.Height/5;

        xPoints[4] = 4*GameFrame.Width/5;
        yPoints[4] = GameFrame.Width/2;
    }

    /**
     * Determines the destination of the AI player.
     *
     */
    public void ChoosePath()
    {
        holeProximity = false;
        Main.aiPlayer.removeBrake();

        /* If the opposing player has bullet-proof armor or invincibility, the AI's spaceship
         * flees to a random region.  It will avoid the black hole and the current location of
         * the opposing player.  Otherwise, the AI will chase the opposing player.
         */
        if (Main.Spaceship1.getPowerupID() == 1 || Main.Spaceship1.getPowerupID() == 4)
        {
            while(destination == 3 || destination == Main.Spaceship1.getRegion())
                destination = generator.nextInt(5) + 1;
        }      
        else
            destination = Main.Spaceship1.getRegion();

        /* The AI player checks the following priorities in order:
         * Priority 1- If the AI player is within the central black hole region and not 
         * invincible, it temporarily abandons chasing the opposing player, and moves to
         * a destination directly away from the black hole, twice as far from it as its
         * current location.
         */
        double holeDistance = Math.hypot(Main.aiPlayer.getXLoc() - Main.hole.getXLocation(),
                Main.aiPlayer.getYLoc() - Main.hole.getYLocation());
        if (holeDistance < safeDistance && Main.aiPlayer.getPowerupID() != 4)
        {
            holeProximity = true;
            xDestination = 2*Main.aiPlayer.getXLoc() - Main.hole.getXLocation();
            yDestination = 2*Main.aiPlayer.getYLoc() - Main.hole.getYLocation();
        }
        //Priority 2- If a powerup exists, the AI will attempt to reach it first.
        else if(GameFrame.powerupExists)
        {
            xDestination = GameFrame.powerup.getXLocation();
            yDestination = GameFrame.powerup.getYLocation();
        }
        //Priority 3- If the AI is in the same region as the opposing player, it will give chase.
        else if(destination == region)
        {
            xDestination = Main.Spaceship1.getXLoc();
            yDestination = Main.Spaceship1.getYLoc();
        }
        //Priority 4- The AI player will move towards the center of the opposing player's current region.
        else
        {
            xDestination = xPoints[destination - 1];
            yDestination = yPoints[destination - 1];
        }

        //If the AI's spaceship and its destination are on opposite sides of the black hole,
        //it will use the wrap-around feature of the map on the sides.
        if (Main.aiPlayer.getXLoc() > GameFrame.Width/2 && xDestination < GameFrame.Width/2)
        {
            xDestination += GameFrame.Width;
        }   
        else if (Main.aiPlayer.getXLoc() < GameFrame.Width/2 && xDestination > GameFrame.Width/2)
        {
            xDestination -= GameFrame.Width;
        }

        if (Main.aiPlayer.getYLoc() > GameFrame.Height/2 && yDestination < GameFrame.Height/2)
        {
            yDestination += GameFrame.Height;
        }
        else if (Main.aiPlayer.getYLoc() < GameFrame.Height/2 && yDestination > GameFrame.Height/2)
        {
            yDestination -= GameFrame.Height;
        }
    }

    /**
     * Draws the AI player's destination on the window, for testing purposes only.
     * @param page The page onto which all objects are drawn.
     */
    public void Draw(Graphics page)
    {
        super.Draw(page);
        
        page.fillOval((int)(xDestination + 0.5), (int)(yDestination + 0.5), 10, 10);
    }

    /**
     * Chooses the move commands for the AI player.
     *
     */
    public void Move()
    {
        ChoosePath();
        
        double deltaX = xDestination - Main.aiPlayer.getXLoc();
        double deltaY = yDestination - Main.aiPlayer.getYLoc();
        double beta, diffAngle;

        //Determines the angle difference between the AI's spaceship's current
        //orientation, and its desired destination, in degrees.
        if (deltaX != 0)
            beta = Math.atan(-deltaY/deltaX)*180/Math.PI;
        else
            beta = 0;

        //Math.atan returns values from -Pi/2 to Pi/2.  We must add 180 if deltaX > 0, to get the proper value.
        if(deltaX > 0)
            beta += 180;

        diffAngle = 180 - beta - Main.aiPlayer.getAngle();
        
        if (diffAngle < 0)
            diffAngle += 360;

        //If the angle difference is less than 15 degrees, the AI's spaceship
        //accelerates forward, and fires if the opposing player is within range.
        if (diffAngle <= maxDiffAngle || diffAngle >= 360 - maxDiffAngle)
        {
            Main.aiPlayer.Accelerate();
            Main.aiPlayer.stopTurnLeft();
            Main.aiPlayer.stopTurnRight();
            
            if (Math.hypot(deltaX, deltaY) < Spaceship.getRange())
                Main.aiPlayer.Fire();
        }

        //If the AI is oriented too far to the right or left, it chooses to turn.
        else if (diffAngle > maxDiffAngle && diffAngle < 180)
        {
            Main.aiPlayer.stopAccelerate();
            Main.aiPlayer.stopTurnLeft();
            Main.aiPlayer.turnRight();
        }
        else
        {
            Main.aiPlayer.stopAccelerate();
            Main.aiPlayer.stopTurnRight();
            Main.aiPlayer.turnLeft();
        }

        //If the AI is too close to the black hole, and is pointing towards it,
        //it sets the brake so that it won't fall in.
        if(holeProximity == true && diffAngle >= 90 && diffAngle <= 270)
            Main.aiPlayer.setBrake();

        //Finally, the move method for the spaceship is called.
        super.Move();
    }
}
