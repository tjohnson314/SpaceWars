
package johnsontcs3finalproject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Implements the frame for the game window.
 *
 * @author timothy
 */
public class GameFrame extends JFrame
{
    public static final int Width = 800, Height = 480; //The width and height of the game window in pixels.
    public static final int Delay = 20; //The amount of time between each update in milliseonds.
    public static final int counterPowerup = 500; //The number of updates needed between each new powerup.
    //20 milliseconds * 500 = 10 seconds.
    public static Timer timeKeeper; //The timer that calls each update for the game.
    public static Timer powerupsTimer; //The timer that is called when each new powerup is created.
    public static Powerups powerup; //The next powerup to be added.
    public static Health health; //The next health powerup to be added.
    //Denote whether a game powerup or a health powerup, respectively, currently are in the game.
    public static boolean powerupExists = false, healthExists = false;
    public static int counter = 0;
    JMenuItem newGameSolo;

    /**
     * Creates the window for the game, the menus, and the listeners.
     * @param title The title for the game.
     */
    public GameFrame(String title)
    {
        super(title);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(new Color(0,0,0));
        setPreferredSize(new Dimension(Width, Height));
        setResizable(false);

        addWindowListener(new ExitListener());

        JMenu fileMenu = new JMenu("File");
        MenuListener ml = new MenuListener();
        
        newGameSolo = new JMenuItem ("New 2-Player Game");
        newGameSolo.addActionListener(ml);
        newGameSolo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        fileMenu.add(newGameSolo);
        
        JMenuItem newGameComputer = new JMenuItem ("New 1-Player Game");
        newGameComputer.addActionListener(ml);
        newGameComputer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        fileMenu.add(newGameComputer);
        
        JMenuItem Instructions = new JMenuItem ("Instructions");
        Instructions.addActionListener(ml);
        fileMenu.add(Instructions);
        
        JMenu playerMenu = new JMenu("Players");
        playerMenu.setMnemonic(KeyEvent.VK_P);
        
        JMenuItem Player1 = new JMenuItem("Player 1 Sign-in");
        Player1.addActionListener(ml);
        playerMenu.add(Player1);
        
        JMenuItem Player1Upgrade = new JMenuItem("Player 1 Upgrade");
        Player1Upgrade.addActionListener(ml);
        playerMenu.add(Player1Upgrade);
        
        JMenuItem Player2 = new JMenuItem("Player 2 Sign-in");
        Player2.addActionListener(ml);
        playerMenu.add(Player2);
        
        JMenuItem Player2Upgrade = new JMenuItem("Player 2 Upgrade");
        Player2Upgrade.addActionListener(ml);
        playerMenu.add(Player2Upgrade);

        JMenuItem NewPlayer = new JMenuItem("Create New Player");
        NewPlayer.addActionListener(ml);
        playerMenu.add(NewPlayer);

        JMenuBar bar = new JMenuBar();
        bar.add(fileMenu);
        bar.add(playerMenu);
	setJMenuBar(bar);

        TaskListener clock = new TaskListener();
        timeKeeper = new Timer(Delay, clock);

        ControlsListener controls = new ControlsListener();
        addKeyListener(controls);
    }

    /**
     * Saves the list of players when the game is exited.
     *
     */
    private class ExitListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            Main.savePlayers();
        }
    }

    /**
     * Determines when a menu item has been chosen, and calls the appropriate method.
     *
     */
    private class MenuListener implements ActionListener
    {
        public void actionPerformed (ActionEvent event)
        {
            String source = event.getActionCommand();
            
            if (source.equals("New 1-Player Game"))
                Main.Begin(1);
            
            else if (source.equals("New 2-Player Game"))
                Main.Begin(2);
            
            else if(source.equals("Instructions"))
                Main.PrintInstructions();
            
            else if (source.equals("Player 1 Sign-in"))
            {
                String name = JOptionPane.showInputDialog("Enter the name of this player: ");
                int index = Main.findName(name);
                
                if(index >= 0)
                    Main.Spaceship1.setIndex(index);
            }
            
            else if (source.equals("Player 1 Upgrade"))
            {
                int choice = -1;
                System.out.println("Upgrading");
                if (Main.Spaceship1.getIndex() >= 0)
                {
                    while(choice != 0 && choice != 1 && choice != 2 && choice != 3)
                    {
                        String choiceString = JOptionPane.showInputDialog(null, "Enter 0 to exit. \nEnter 1 to upgrade your bullets " +
                                "(Current level: " + Main.Spaceship1.player.getBulletLevel() + ")" + 
                                "\nEnter 2 to upgrade your speed" + "(Current level: " + Main.Spaceship1.player.getSpeedLevel() 
                                + ")" + "\nEnter 3 to upgrade your armor (Current level: " + Main.Spaceship1.player.getArmorLevel()
                                + ")");
                        choice = Integer.parseInt(choiceString);
                    }

                    if (choice == 1)
                        Main.Spaceship1.upgradeBullets();
                    else if (choice == 2)
                        Main.Spaceship1.upgradeSpeed();
                    else if (choice == 3)
                        Main.Spaceship1.upgradeArmor();
                }
                else
                    JOptionPane.showMessageDialog(null, "Please sign in, Player 1.");
            }
            
            else if (source.equals("Player 2 Sign-in"))
            {
                String name = JOptionPane.showInputDialog("Enter the name of this player: ");
                int index = Main.findName(name);
                
                 if(index >= 0)
                    Main.Spaceship2.setIndex(index);
            }
            
            else if (source.equals("Player 2 Upgrade"))
            {
                int choice = -1;
                if (!Main.ComputerPlayer)
                {
                    if(Main.Spaceship2.getIndex() >= 0)
                    {
                        System.out.println("Upgrading");
                        while(choice != 0 && choice != 1 && choice != 2 && choice != 3)
                        {
                            String choiceString = JOptionPane.showInputDialog(null, "Enter 0 to exit. " +
                                    "\nEnter 1 to upgrade your bullets (Current level: " + Main.Spaceship2.player.getBulletLevel()
                                    + ")" + "\nEnter 2 to upgrade your speed" + "(Current level: " 
                                    + Main.Spaceship2.player.getSpeedLevel()  + ")" + "\nEnter 3 to upgrade your armor " +
                                    "(Current level: " + Main.Spaceship2.player.getArmorLevel() + ")");
                            choice = Integer.parseInt(choiceString);
                        }
                    }

                    if (choice == 1)
                        Main.Spaceship2.upgradeBullets();
                    else if (choice == 2)
                        Main.Spaceship2.upgradeSpeed();
                    else if (choice == 3)
                        Main.Spaceship2.upgradeArmor();
                }
                else
                    JOptionPane.showMessageDialog(null, "Please sign in, Player 2.");
            }

        else if (source.equals("Create New Player"))
                Main.newPlayer();
        }
    }

    /**
     * Determines when one of the keyboard commands has been pressed, and calls
     * the appropriate method.
     *
     */
    private class ControlsListener implements KeyListener
    {
        public void keyPressed (KeyEvent event)
        {
            switch(event.getKeyCode())
            {
                case KeyEvent.VK_UP: Main.Spaceship1.Accelerate();
                                break;
                case KeyEvent.VK_LEFT: Main.Spaceship1.turnLeft();
                                break;
                case KeyEvent.VK_RIGHT: Main.Spaceship1.turnRight();
                                break;
                case KeyEvent.VK_DOWN: Main.Spaceship1.setBrake();
                                break;
                case KeyEvent.VK_CONTROL: Main.Spaceship1.Fire();
                                break;
                case KeyEvent.VK_W: if(!Main.ComputerPlayer) Main.Spaceship2.Accelerate();
                                break;
                case KeyEvent.VK_A: if(!Main.ComputerPlayer) Main.Spaceship2.turnLeft();
                                break;
                case KeyEvent.VK_D: if(!Main.ComputerPlayer) Main.Spaceship2.turnRight();
                                break;
                case KeyEvent.VK_S: if(!Main.ComputerPlayer) Main.Spaceship2.setBrake();
                                break;
                case KeyEvent.VK_E: if(!Main.ComputerPlayer) Main.Spaceship2.Fire();
                                break;
            }

            repaint();
        }

        public void keyTyped (KeyEvent event) {}

        public void keyReleased (KeyEvent event)
        {
            switch(event.getKeyCode())
            {
                case KeyEvent.VK_UP: Main.Spaceship1.stopAccelerate();
                                break;
                case KeyEvent.VK_LEFT: Main.Spaceship1.stopTurnLeft();
                                break;
                case KeyEvent.VK_RIGHT: Main.Spaceship1.stopTurnRight();
                                break;
                case KeyEvent.VK_DOWN: Main.Spaceship1.removeBrake();
                                break;
                                
                case KeyEvent.VK_W: if(!Main.ComputerPlayer) Main.Spaceship2.stopAccelerate();
                                break;
                case KeyEvent.VK_A: if(!Main.ComputerPlayer) Main.Spaceship2.stopTurnLeft();
                                break;
                case KeyEvent.VK_D: if(!Main.ComputerPlayer) Main.Spaceship2.stopTurnRight();
                                break;
                case KeyEvent.VK_S: if(!Main.ComputerPlayer) Main.Spaceship2.removeBrake();
                                break;
            }
        }
    }

    /**
     * Redraws the game window, and calls the methods to update each game object.
     *
     */
    private class TaskListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            repaint();
            Main.IncrementTime();
            counter++;
            
            if(counter == counterPowerup/2)
            {
                healthExists = true;
                health = new Health();
            }
            
            if(counter == counterPowerup)
            {
                counter = 0;
                powerupExists = true;
                powerup = new Powerups();
                Main.Spaceship1.setPowerupID(0);
                Main.Temp.setPowerupID(0);
            }
        }
    }
}
