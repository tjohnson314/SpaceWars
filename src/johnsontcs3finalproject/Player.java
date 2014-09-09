
package johnsontcs3finalproject;

import java.io.*;

/**
 * Stores the data for each player.
 *
 * @author timothy
 */
public class Player implements Serializable
{
    private String Name;
    private int bulletLevel, speedLevel, armorLevel;
    private int gamesWon;
    private int balance;

    /**
     * Creates a new player.
     *
     * @param name The name of the player.
     * @param level The starting level of the new player.
     */
    public Player (String name, int level)
    {
        Name = name;
        bulletLevel = level;
        speedLevel = level;
        armorLevel = level;
        gamesWon = 0;
        balance = 0;
    }

    /**
     *
     * @return Returns the bullet level for this player.
     */
    public int getBulletLevel()
    {
        return bulletLevel;
    }

    /**
     * Increases the bullet level for this player.
     *
     */
    public void increaseBulletLevel()
    {
        bulletLevel++;
    }

    /**
     *
     * @return Returns the speed level for this player.
     */
    public int getSpeedLevel()
    {
        return speedLevel;
    }

    /**
     * Increases the speed level for this player.
     *
     */
    public void increaseSpeedLevel()
    {
        speedLevel++;
    }

    /**
     *
     * @return Returns the armor level for this player.
     */
    public int getArmorLevel()
    {
        return armorLevel;
    }

    /**
     * Increases the armor level for this player.
     *
     */
    public void increaseArmorLevel()
    {
        armorLevel++;
    }

    /**
     *
     * @return Returns the number of games won by this player.
     */
    public int getGamesWon()
    {
        return gamesWon;
    }

    /**
     * Increments the number of games won, and awards the prize cash.
     * @param levelDestroyed The level of the spaceshipo that was beaten.
     */
    public void WinGame(int levelDestroyed)
    {
        gamesWon++;
        balance += levelDestroyed*100;
        System.out.println("The game is won.");
        Main.printPlayers();
    }

    /**
     * Sets the name of this player.
     * @param name The name of this player.
     */
    public void setName(String name)
    {
        Name = name;
    }

    /**
     *
     * @return Returns the name of this player.
     */
    public String getName()
    {
        return Name;
    }

    /**
     * Increases the cash balance of this player.
     * @param amount The amount by which this player's cash is increased.
     */
    public void increaseBalance(int amount)
    {
        balance += amount;
    }

    /**
     * Decreases the cash balance of this player.
     * @param amount The amount by which this player's cash is decreased.
     */
    public void decreaseBalance(int amount)
    {
        balance -= amount;
    }

    /**
     *
     * @return Returns the cash balance of this player.
     */
    public int getBalance()
    {
        return balance;
    }

    /**
     * Returns the level of the player to 0 after their spaceship is destroyed.
     */
    public void restart()
    {
        bulletLevel = 0;
        speedLevel = 0;
        armorLevel = 0;
        gamesWon = 0;
    }

    /**
     * Determines whether a different player is identical to this player.
     * @param temp The name against which this player's name is compared.
     * @return
     */
    public boolean equals(Player temp)
    {
        if (temp.getName().equals(Name))
            return true;
        else
            return false;
    }
}
