
package johnsontcs3finalproject;

import javax.swing.*;
import java.util.ArrayList;
import java.io.*;

/**
 * Contains the functions for the overall flow of the game.
 *
 * @author timothy
 */
public class Main implements Serializable
{
    public static GameFrame frame; //The frame for the game window.
    public static Player Player1; //The first human player.
    public static Player Player2; //The second human player in a 2-player game.
    public static Spaceship Spaceship1; //The first spaceship object.
    public static Spaceship Spaceship2; //The second spaceship object.
    public static AI aiPlayer; //The AI player in a 1-player game.
    public static Spaceship Temp; //Can be set to either Player 2 or the AI player.
    public static BlackHole hole; //The black hole.
    public static GamePanel panel; //The panel inside the game window, on which all of the game info is printed.
    public static ArrayList<Player> Players; //The list of players, read in from the file.
    public static boolean ComputerPlayer; //True for a 1-player game against the computer, false otherwise.
    private static final String fileName = "players.txt"; //The file where the player data is stored.

    /**
     * Initializes the window for the game, and the list of players.
     *
     * @param args Unused, as there are no arguments expected.
     */
    public static void main(String[] args)
    {
        frame = new GameFrame ("Space Wars");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        panel = new GamePanel();
        
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        Spaceship1 = new Spaceship(-1, 3*(double)GameFrame.Width/4, (double)GameFrame.Height/2, 180);
        Spaceship2 = new Spaceship(-1, (double)GameFrame.Width/4, (double)GameFrame.Height/2, 0);
        aiPlayer = new AI((double)GameFrame.Width/4, (double)GameFrame.Height/2, 0);
        
        hole = new BlackHole();

        Players = new ArrayList<Player>();
        
        Player1 = new Player("Player 1", 0);
        Player2 = new Player("Player 2", 0);

        //Main.savePlayers();
        Main.readPlayers();
        Main.printPlayers();
    }

    /**
     * Initializes the spaceships and begins the game.
     *
     * @param choice The variable describing whether a 1-player or 2-player game has been chosen.
     */
    public static void Begin(int choice)
    {
        int index1 = Spaceship1.getIndex();
        Spaceship1 = new Spaceship(index1, 3*(double)GameFrame.Width/4, (double)GameFrame.Height/2, 180);

        if(choice == 1)
        {
            aiPlayer = new AI((double)GameFrame.Width/4, (double)GameFrame.Height/2, 0);
            Temp = aiPlayer;
            aiPlayer.ChoosePath();
            ComputerPlayer = true;
        }
        else if(choice == 2)
        {
            int index2 = Spaceship2.getIndex();
            Spaceship2 = new Spaceship(index2, (double)GameFrame.Width/4, (double)GameFrame.Height/2, 0);
            Temp = Spaceship2;
            ComputerPlayer = false;
        }
        
        GameFrame.timeKeeper.start();
    }

    /**
     * Creates a new player from the player's name, if that name does not already exist.
     *
     */
    public static void newPlayer()
    {
        boolean nameExists = false;
        
        String name = JOptionPane.showInputDialog("Enter the name of this player");
        Player temp = new Player(name, 0);
        
        for(int i = 0; i < Players.size() && !nameExists; i++)
            if (Players.get(i).equals(temp))
                nameExists = true;
                
        if(!nameExists)
            Players.add(new Player(name, 0));
        else
            JOptionPane.showMessageDialog(null, "That name already exists.  Please choose a new name: ");
    }

    /**
     * Prints out the name, number of games won, and current cash for each player.
     *
     */
    public static void printPlayers()
    {
        for(int i = 0; i < Players.size(); i++)
        {
            System.out.println("Name: " + Players.get(i).getName());
            System.out.println("Games Won:" + Players.get(i).getGamesWon());
            System.out.println("Balance: " + Players.get(i).getBalance());
        }
    }

    /**
     * Searches through the list of players by name when a player logs in.
     *
     * @param name The name a user has entered to log in.
     * @return Returns the index in the list of the player found, or returns -1 if the player is not found.
     *
     */
    public static int findName(String name)
    {
        boolean found = false;
        int index = -1;
        
        for(int i = 0; i < Players.size() && !found; i++)
            if (Players.get(i).getName().equals(name))
            {
                found = true;
                index = i;
            }

        if (!found)
        {
            JOptionPane.showMessageDialog(null, "Player not found.");
        }
        
        else
            JOptionPane.showMessageDialog(null, "Player found");
        
        System.out.println(index);
        return index;
    }

    /**
     * Reads in the list of players from the players.txt file.
     *
     */
    public static void readPlayers()
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream (fis);
            Players = (ArrayList<Player>)in.readObject();
            System.out.println("Reading Players");
        }
        
        catch(FileNotFoundException e)
        {
            System.out.println(e);
        }
        
        catch(java.io.IOException e)
        {
            System.out.println(e);
        }
        
        catch(java.lang.ClassNotFoundException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Saves the current list of players into the players.txt file.
     *
     */
    public static void savePlayers()
    {
        try
        {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream out = new ObjectOutputStream (fos);
        out.writeObject(Players);
        System.out.println("Saving Players");
        }

        catch(FileNotFoundException e)
        {
            System.out.println(e);
        }

        catch(java.io.IOException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Creates a tabbed pane containing the description of and instructions for Spaceship Wars.
     *
     */
    public static void PrintInstructions()
    {
        String controlsMessage, powerupsMessage;
        JFrame iFrame = new JFrame("Instructions");
        
	JTabbedPane tp = new JTabbedPane();

        JPanel Controls = new JPanel();
        JTextArea controls = new JTextArea(30, 70);
        controlsMessage = "\nWelcome to Space Wars!  In this game, two spaceships" +
                   "\nduel to the death, shooting at each other until one is destroyed." +
                   "\n\n Player 1 Controls: \n\t Accelerate: Up arrow" +
                   "\n\t Turn left: Left arrow \n\t Turn right: Right arrow" +
                   "\n\t Brake: Down Arrow \n\t Fire: Right Control \n\n Player 2 Controls: " +
                   "\n\t Accelerate: W \n\t Turn left: A \n\t" +
                   " Turn right: D \n\t Brake: S \n\t Fire: E";
        
        controlsMessage += "\n\nThis game also includes four powerups, which appear randomly" +
                   "\nevery 10 seconds.  Finally, look out for the black hole, and HAVE FUN!\n";
	
        controls.setText(controlsMessage);
        Controls.add(controls);
	
	JPanel Powerups = new JPanel();
        JTextArea powerups = new JTextArea(30, 70);
	powerupsMessage = "\nThe four powerups are bullet-proof armor, speed, anti-gravity, and invincibility." +
                          "\nAll powerups last for 10 seconds, then disappear, or can be sucked into the black hole." +
                          "\n\nBullet-proof armor: This powerup makes you invulnerable to enemy bullets/projectiles." +
                          "\n\nSpeed: This powerup doubles your acceleration for the next 15 seconds." +
                          "\n\nAnti-gravity: This powerups makes your spaceship immune to the effects of the gravitational" +
                          "\n pull of the black hole, although your spaceship will still be destroyed if you fall into it." +
                          "\n\nInvincibility: This powerup makes you invulnerable to enemy bullets/projectiles, and even" +
                          "\n allows you to pass through the black hole. \n\nEnjoy!";
        powerups.setText(powerupsMessage);
        Powerups.add(powerups);

        tp.addTab("Controls", Controls);
        tp.addTab("Powerups", Powerups);
        
        iFrame.getContentPane().add(tp);
        iFrame.pack();
        iFrame.setVisible(true);
    }

    /**
     * Calls the functions to update the location of each spaceship.
     *
     */
    public static void IncrementTime()
    {
        Spaceship1.Move();
        
        if(!ComputerPlayer)
            Spaceship2.Move();
        else
        {
            aiPlayer.ChoosePath();
            aiPlayer.Move();
        }
        
        if (Collision())
        {
            Replay();
            GameFrame.timeKeeper.stop();
        }
    }

    /**
     * Creates a new game.
     *
     */
    public static void Replay()
    {
            GameFrame.counter = 0;
            GameFrame.powerupExists = false;
            GameFrame.healthExists = false;

            JOptionPane.showMessageDialog(null, "Thank you for playing Space Wars!");
    }

    /**
     * Checks if there is a collision between any two objects, and takes away
     * health points from each player as necessary.
     *
     * @return Raturns true if one player's spaceship is destroyed, false otherwise.
     */
    public static boolean Collision()
    {
        boolean hit1, hit2;
        Bullet temp;
        for (int i = 0; i < Spaceship1.Bullets.size(); i++)
        {
            temp = (Bullet)Spaceship1.Bullets.get(i);
            if (Math.abs(temp.getXLocation() - Temp.getXLoc()) < Spaceship.getSize() 
                    && Math.abs(temp.getYLocation() - Temp.getYLoc()) < Spaceship.getSize())
            {
                if (Temp.getPowerupID() != 1 && Temp.getPowerupID() != 4)
                    Temp.Hit(Spaceship1.getBulletDamage());
                
                Spaceship1.DestroyBullet(temp);
            }
            
            else if (Math.abs(temp.getXLocation() - hole.getXLocation()) < BlackHole.getSize() 
                    && Math.abs(temp.getYLocation() - hole.getYLocation()) < BlackHole.getSize())
                Spaceship1.DestroyBullet(temp);
        }
        
        if (Math.abs(hole.getXLocation() - Temp.getXLoc()) < (BlackHole.getSize() + Spaceship.getSize()) 
                && Math.abs(hole.getYLocation() - Temp.getYLoc()) < (BlackHole.getSize() + Spaceship.getSize()))
        {
            if (Temp.getPowerupID() != 4)
                Temp.Hit(hole.getDamage());
        }
        
        for (int i = 0; i < Temp.Bullets.size(); i++)
        {
            temp = (Bullet)Temp.Bullets.get(i);
            if (Math.abs(temp.getXLocation() - Spaceship1.getXLoc()) < Spaceship.getSize()
                    && Math.abs(temp.getYLocation() - Spaceship1.getYLoc()) < Spaceship.getSize())
            {
                if (Spaceship1.getPowerupID() != 1 && Spaceship1.getPowerupID() != 4)
                    Spaceship1.Hit(Temp.getBulletDamage());
                
                Temp.DestroyBullet(temp);
            }
            
             else if (Math.abs(temp.getXLocation() - hole.getXLocation()) < BlackHole.getSize() 
                    && Math.abs(temp.getYLocation() - hole.getYLocation()) < BlackHole.getSize())
                Temp.DestroyBullet(temp);
        }
        
        if (Math.abs(hole.getXLocation() - Spaceship1.getXLoc()) < (BlackHole.getSize() + Spaceship.getSize()) 
                && Math.abs(hole.getYLocation() - Spaceship1.getYLoc()) < (BlackHole.getSize() + Spaceship.getSize()))
        {
            if (Spaceship1.getPowerupID() != 4)
                Spaceship1.Hit(hole.getDamage());
        }
        
        if (GameFrame.powerupExists)
        {
            if (Math.abs(Spaceship1.getXLoc() - GameFrame.powerup.getXLocation()) < (Spaceship.getSize() + Powerups.getSize()) 
                    && Math.abs(Spaceship1.getYLoc() - GameFrame.powerup.getYLocation()) < (Spaceship.getSize() + Powerups.getSize()))
            {
                Spaceship1.CollectPowerup(GameFrame.powerup.getID());
                GameFrame.powerupExists = false;
            }

            if (Math.abs(Temp.getXLoc() - GameFrame.powerup.getXLocation()) < (Spaceship.getSize() + Powerups.getSize()) 
                    && Math.abs(Temp.getYLoc() - GameFrame.powerup.getYLocation()) < (Spaceship.getSize() + Powerups.getSize()))
            {
                Temp.CollectPowerup(GameFrame.powerup.getID());
                GameFrame.powerupExists = false;
            }
        }
        
        if (GameFrame.healthExists)
        {
            if (Math.abs(Spaceship1.getXLoc() - GameFrame.health.getXLocation()) < (Spaceship.getSize() + Health.getSize()) 
                    && Math.abs(Spaceship1.getYLoc() - GameFrame.health.getYLocation()) < (Spaceship.getSize() + Health.getSize()))
            {
                Spaceship1.CollectHealth();
                GameFrame.healthExists = false;
            }

            if (Math.abs(Temp.getXLoc() - GameFrame.health.getXLocation()) < (Spaceship.getSize() + Health.getSize()) 
                    && Math.abs(Temp.getYLoc() - GameFrame.health.getYLocation()) < (Spaceship.getSize() + Health.getSize()))
            {
                Temp.CollectHealth();
                GameFrame.healthExists = false;
            }
        }

        if (Math.abs(Spaceship1.getXLoc() - Temp.getXLoc()) < 2*Spaceship.getSize() 
                && Math.abs(Spaceship1.getYLoc() - Temp.getYLoc()) < 2*Spaceship.getSize())
        {
            double tempSpeed = Spaceship1.getXSpeed();
            Spaceship1.setXSpeed(Temp.getXSpeed());
            Temp.setXSpeed(tempSpeed);

            tempSpeed = Spaceship1.getYSpeed();
            Spaceship1.setYSpeed(Temp.getYSpeed());
            Temp.setYSpeed(tempSpeed);
        }

        hit1 = Spaceship1.getHitPoints() <= 0;
        hit2 = Temp.getHitPoints() <= 0;

        if (hit1 == true && hit2 == false)
        {
            Spaceship1.Destroy();
            if (Spaceship1.getIndex() >= 0)
                Players.get(Spaceship1.getIndex()).restart();
            
            if(Temp.getIndex() >= 0)
                Players.get(Temp.getIndex()).WinGame(Spaceship1.player.getGamesWon() + 1);
            
            JOptionPane.showMessageDialog(null, "Player 2 wins!");
            return true;
        }
        
        else if (hit2 == true && hit1 == false)
        {
            Temp.Destroy();
            if(!ComputerPlayer && Spaceship2.getIndex() >= 0)
                Players.get(Spaceship2.getIndex()).restart();
            
            if (Spaceship1.getIndex() >= 0)
                if (ComputerPlayer || !Spaceship2.Initialized())
                    Players.get(Spaceship1.getPlayerIndex()).WinGame(1);
                else
                    Players.get(Spaceship1.getPlayerIndex()).WinGame(Spaceship2.player.getGamesWon() + 1);

            
            JOptionPane.showMessageDialog(null, "Player 1 wins!");
            return true;
        }
        
        else if (hit1 == true && hit2 == true)
        {
            Spaceship1.Destroy();
            Temp.Destroy();
            Player2.restart();
            JOptionPane.showMessageDialog(null, "The game is a tie!");
            return true;
        }
        
        return false;
    }
}
