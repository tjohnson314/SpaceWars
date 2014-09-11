
package johnsontcs3finalproject;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class Spaceship 
{
    private static int imageSize = 10; //A constant describing the size of the spaceship.
    private static int hitPointsInit = 100; //The starting number of hit points.
    private int bulletDamage = 20; //The amount of damage done by a bullet hit.
    private double xLocation, yLocation, xTip, yTip; //The location of the center of spaceship, and of its tip.
    private double xSpeed, ySpeed; //The speed of the spaceship.
    private double acceleration = 0.4, brakeAcc = 2.0; //The acceleration and braking ability of the spaceship.
    private int angle; //The current angle orientation.
    private final int angleChange = 5; //The amount that the angle changes during a turn in a single update.
    private int powerupID, hitPoints; //The number describing the powerup the spaceship has, and the current number of hit points.
    private int playerIndex; //The index of the player who controls this spaceship in the list of players.
    private static double friction = 0.075; //The frictional constant.
    private int color; //The color of the spaceship.
    //Variables describing whether the spaceship is turning left or right, or braking or accelerating.
    private boolean turnLeft, turnRight, Accelerate, brake;
    //Variables describing whether the spaceship has fired or has been hit.
    private boolean hasFired, hit = false;
    private boolean playerInit = false; //True when the player for the spaceship has logged in.
    ArrayList Bullets = new ArrayList(); //A list of the various bullets fired by the spaceship.
    private static final double bulletRange = 500; //The range of the bullets fired.
    private static final int bulletDelay = 25; //25*20 milliseconds = 0.5 seconds between firing bullets.
    private int currentBulletDelay = 0;
    Player player; //The player who controls this spaceship.
    protected int region; //The current region of the spaceship.

    /**
     * Initializes the location, speed, angle, and hit points of the spaceship.
     * @param index The index of the player controlling this spaceship in the list of players.
     * @param xLoc The starting x location of the spaceship.
     * @param yLoc The starting y location of the spaceship.
     * @param angle The starting angle of the spaceship.
     */
    public Spaceship(int index, double xLoc, double yLoc, int angle)
    {
        xLocation = xLoc;
        yLocation = yLoc;
        xSpeed = 0;
        ySpeed = 0;
        this.angle = angle;
        hitPoints = hitPointsInit;

        if(!playerInit)
            playerIndex = index;

        if (playerIndex >= 0)
        {
            player = Main.Players.get(playerIndex);
            playerInit = true;
        }
    }

    /**
     * Draws the spacehsip and its bullets onto the game window.
     * The spaceship is shaped like the capital letter A, oriented in space.
     * The x location and y location are in the center of the cross bar.
     * The x tip and y tip are at the tip of the A.
     * The points labeled (xCross1, yCross1) and (xCross2, yCross2) are at
     * either end of the cross bar.
     * The points labeled (xPoint1, yPoint1) and (xPoint2, yPoint2) are at the
     * point of each leg of the A.
     * Calculating the location of each point requires several calculations.
     * @param page The page onto which all game objects are drawn.
     */
    public void Draw(Graphics page)
    {
        xTip = xLocation + imageSize*Tables.cos[angle];
        yTip = yLocation + imageSize*Tables.sin[angle];

        double xCross1 = xLocation + imageSize/2*Tables.sin[angle];
        double yCross1 = yLocation - imageSize/2*Tables.cos[angle];

        double xCross2 = xLocation - imageSize/2*Tables.sin[angle];
        double yCross2 = yLocation + imageSize/2*Tables.cos[angle];

        double xPoint1 = 2*(xCross1 - xTip) + xTip;
        double yPoint1 = 2*(yCross1 - yTip) + yTip;

        double xPoint2 = 2*(xCross2 - xTip) + xTip;
        double yPoint2 = 2*(yCross2 - yTip) + yTip;
        
        page.drawLine((int)(xTip + 0.5), (int)(yTip + 0.5), (int)(xPoint1 + 0.5), (int)(yPoint1 + 0.5));
        page.drawLine((int)(xTip + 0.5), (int)(yTip + 0.5), (int)(xPoint2 + 0.5), (int)(yPoint2 + 0.5));
        page.drawLine((int)(xCross1 + 0.5), (int)(yCross1 + 0.5), (int)(xCross2 + 0.5), (int)(yCross2 + 0.5));
        
        for (int i = 0; i < Bullets.size(); i++)
        {
            Bullet temp = (Bullet)Bullets.get(i);
            temp.Draw(page);
        }
        
        if (hit)
        {
            page.setColor(Color.ORANGE);
            page.fillOval((int)(xLocation - 15 + 0.5), (int)(yLocation - 15 + 0.5), 30, 30);
            (new Sound("Explosion")).start();
            (new Sound("Explosion")).start();
        }
    }

    /**
     *
     * @return Returns true is the player for this spaceship has been initialized.
     */
    public boolean Initialized()
    {
        return playerInit;
    }

    /**
     * Determines the current region ofthe map in which the spaceship is located.
     *
     */
    public void setRegion()
    {
        if (xLocation <= GameFrame.Width/2.5)
            region = 1;
        else if (xLocation >= 1.5*GameFrame.Width/2.5)
            region = 5;
        else
        {
            if (yLocation < GameFrame.Height/2.5)
                region = 2;
            else if (yLocation > 1.5*GameFrame.Height/2.5)
                region = 4;
            else
                region = 3;
        }
    }

    /**
     *
     * @return Returns the size of the spaceship.
     */
    public static int getSize()
    {
        return imageSize;
    }

    /**
     *
     * @return Returns the current region in which the spaceship is located.
     */
    public int getRegion()
    {
        return region;
    }

    /**
     * Sets the variable that determines whether the player has been initialized.
     * @param init True if the player has been initialized, false otherwise.
     */
    public void playerInit(boolean init)
    {
        playerInit = init;
    }

    /**
     * Gives damage to the spaceship when it is hit.
     * @param damage The amount of damage that is given if the spaceship has no armor.
     */
    public void Hit(int damage)
    {
        if(playerIndex >= 0)
            hitPoints -= damage/(1 + 0.2*player.getArmorLevel());
        else
            hitPoints -= damage;
    }

    /**
     *
     * @return Returns the current angle orientation of the spaceship.
     */
    public int getAngle()
    {
        return angle;
    }

    /**
     *
     * @return Returns the current number of hit points.
     */
    public int getHitPoints()
    {
        return hitPoints;
    }

    /**
     *
     * @return Returns the damage from the player's bullets.
     */
    public int getBulletDamage()
    {
        if(playerIndex >= 0)
            return (int)(bulletDamage*(1.0 + 0.2*player.getBulletLevel()));
        else
            return bulletDamage;
    }

    /**
     * Sets the index of the player in the list of players.
     * @param index The index that is set.
     */
    public void setIndex(int index)
    {
        playerIndex = index;
        playerInit = true;
        player = Main.Players.get(index);
    }

    /**
     *
     * @return Returns the index of the player in the list of players.
     */
    public int getIndex()
    {
        return playerIndex;
    }

    /**
     *
     * @return Returns the x location of the spaceship.
     */
    public double getXLoc()
    {
        return xLocation;
    }

    /**
     *
     * @return Returns the y location of the spaceship.
     */
    public double getYLoc()
    {
        return yLocation;
    }

    /**
     *
     * @return Returns the current horizontal speed.
     */

    public double getXSpeed()
    {
        return xSpeed;
    }

    /**
     * Sets the current horizontal speed.
     * @param speed The horizontal speed set.
     */
    public void setXSpeed(double speed)
    {
        xSpeed = speed;
    }

    /**
     *
     * @return Returns the current vertical speed.
     */
    public double getYSpeed()
    {
        return ySpeed;
    }

    /**
     * Sets the current vertical speed.
     * @param speed The speed that is set.
     */
    public void setYSpeed(double speed)
    {
        ySpeed = speed;
    }

    /**
     *
     * @return Returns the current player index.
     */
    public int getPlayerIndex()
    {
        return playerIndex;
    }

    /**
     *
     * @return Returns the range of the spaceship's bullets.
     */
    public static double getRange()
    {
        return bulletRange;
    }

    /**
     * Changes the location, angle, and speed of the spaceship and its bullets each time the game updates.
     */
    public void Move()
    {  
        double xAcc = 0, yAcc = 0;
        
        xLocation += xSpeed;
        yLocation += ySpeed;

        if (xLocation < 0)
            xLocation += GameFrame.Width;
        else if (xLocation >= GameFrame.Width)
            xLocation -= GameFrame.Width;

        if(yLocation < 0)
            yLocation += GameFrame.Height - 50;
        else if (yLocation > GameFrame.Height - 50)
            yLocation -= GameFrame.Height - 50;
        
        if(turnRight)
        {
            angle += angleChange;
            if(angle >= 360)
                angle -= 360;
        }
        if (turnLeft)
        {
            angle -= angleChange;
            if(angle < 0)
                angle += 360;
        }
        
        if (Accelerate)
        {
            xAcc += acceleration* Tables.cos[angle];
            yAcc += acceleration* Tables.sin[angle];
        }
        
        if(playerInit)
        {
            xAcc *= (1 + 0.2*player.getSpeedLevel());
            yAcc *= (1 + 0.2*player.getSpeedLevel());
        }
        
        if (powerupID == 2)
        {
            xAcc *= 2;
            yAcc *= 2;
        }
        
        xAcc -= friction*xSpeed;
        yAcc -= friction*ySpeed;
        
        if (powerupID != 3)
        {
            xAcc += Main.hole.xAccelerate(xLocation, yLocation);
            yAcc -= Main.hole.yAccelerate(xLocation, yLocation);
        }
        
        xSpeed += xAcc;
        ySpeed += yAcc;
        
        if (brake)
        {
            double speed = Math.hypot(xSpeed, ySpeed);
            
            if(xSpeed > brakeAcc)
                xSpeed -= brakeAcc;
            else 
                xSpeed = 0;
            
            if(ySpeed > brakeAcc)
                ySpeed -= brakeAcc;
            else
                ySpeed = 0;
        }
        
        setRegion();

        for (int i = 0; i < Bullets.size(); i++)
        {
            Bullet temp = (Bullet)Bullets.get(i);
            Bullets.remove(i);
            if(!temp.reachedRange())
            {
                temp.Move();
                Bullets.add(temp);
            }
        }

        if(currentBulletDelay > 0)
            currentBulletDelay--;
    }

    /**
     * Fires a new bullet.
     *
     */
    public void Fire()
    {
        if (currentBulletDelay == 0)
        {
            Bullets.add(new Bullet(xTip, yTip, angle));
            (new Sound("Bullet")).start();
            currentBulletDelay = bulletDelay;
        }
    }

    /**
     * Sets the spaceship's accelerator.
     *
     */
    public void Accelerate()
    {
        Accelerate = true;
        //(new Sound("Thrust")).start();
    }

    /**
     * Sets the spaceship to turn left.
     *
     */
    public void turnLeft()
    {
        turnLeft = true;
    }

    /**
     * Sets the spaceship to turn right.
     *
     */
    public void turnRight()
    {
        turnRight = true;
    }

    /**
     * Stops the spaceship's acceleration.
     *
     */
    public void stopAccelerate()
    {
        Accelerate = false;
    }

    /**
     * Stops the spaceship's left turn.
     *
     */
    public void stopTurnLeft()
    {
        turnLeft = false;
    }

    /**
     * Stops the spaceship's right turn.
     *
     */
    public void stopTurnRight()
    {
        turnRight = false;
    }

    /**
     * Sets the brake.
     *
     */
    public void setBrake()
    {
        brake = true;
    }

    /**
     * Removes the brake.
     *
     */
    public void removeBrake()
    {
        brake = false;
    }

    /**
     * Destroys the spaceship when its hit points reach zero.
     */
    public void Destroy()
    {
        hit = true;
    }

    /**
     * Destroys the given bullet when it impacts an object or reaches the end of its range.
     * @param temp The bullet to be destroyed.
     */
    public void DestroyBullet(Bullet temp)
    {
        Bullets.remove(Bullets.indexOf(temp));
    }

    /**
     * Sets the powerup collected.
     * @param ID The ID of the powerup collected.
     */
    public void CollectPowerup (int ID)
    {
        powerupID = ID;
    }

    /**
     * Adds hit points to the spaceship, up to a maximum of 100.
     */
    public void CollectHealth()
    {
        if(hitPoints <= 100 - Health.health)
            hitPoints += Health.health;
        else
            hitPoints = 100;
    }

    /**
     *
     * @return Returns the powerup of the spaceship.
     */
    public int getPowerupID()
    {
        return powerupID;
    }

    /**
     * Sets the powerup ID of the spaceship.
     * @param ID The powerup ID set.
     */
    public void setPowerupID(int ID)
    {
        powerupID = ID;
    }

    /**
     * Upgrades the player's bullets to the next level, at a cost of 100 times that level.
     *
     */
    public void upgradeBullets()
    {
        int cost = (player.getBulletLevel() + 1)*100;
        if (player.getBalance() >= cost)
        {
            int choice = JOptionPane.showConfirmDialog(null, "Would you like to upgrade your bullets for $" + 
                        cost + "?" + "\nCurrent Balance: $" + player.getBalance());

            if (choice == JOptionPane.YES_OPTION)
            {
                player.decreaseBalance(cost);
                player.increaseBulletLevel();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "You do not have enough money to upgrade your bullets." +
                        "\nRequired: " + cost + "\nBalance: " + player.getBalance());
    }

    /**
     * Upgrades the player's acceleration to the next level, at a cost of 100 times that level.
     *
     */
    public void upgradeSpeed()
    {
        int cost = (player.getSpeedLevel() + 1)*100;
        
        if(player.getBalance() >= cost)
        {
            int choice = JOptionPane.showConfirmDialog(null, "Would you like to upgrade your speed for $" + 
                        cost + "?" + "\nCurrent Balance: $" + player.getBalance());

            if (choice == JOptionPane.YES_OPTION)
            {
                player.decreaseBalance(cost);
                player.increaseSpeedLevel();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "You do not enough enough money to upgrade your speed." +
                        "\nRequired: " + cost + "\nBalance: " + player.getBalance());
    }

    /**
     * Upgrades the spaceship's armor to the next level, at a cost of 100 times that level.
     *
     */
    public void upgradeArmor()
    {
        int cost = (player.getArmorLevel() + 1)*100;
        if (player.getBalance() >= cost)
        {
            int choice = JOptionPane.showConfirmDialog(null, "Would you like to upgrade your armor for $" + 
                        cost + "?" + "\nCurrent Balance: $" + player.getBalance());

            if (choice == JOptionPane.YES_OPTION)
            {
                player.decreaseBalance(cost);
                player.increaseArmorLevel();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "You do not enough enough money to upgrade your armor." +
                        "\nRequired: " + cost + "\nBalance: " + player.getBalance());
    }
}
