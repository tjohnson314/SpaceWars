
package johnsontcs3finalproject;

import javax.swing.*;
import java.awt.*;

/**
 * Implements the game inside the game window.
 * @author timothy
 */
public class GamePanel extends JPanel
{
    boolean Init = false;
    private ImageIcon image;
    private JLabel label;

    /**
     * Sets the background image and the font.
     *
     */
    public GamePanel() 
    {
        Font font = new Font("Helvetica", Font.BOLD, 14);

        setBackground(new Color(0, 0, 0));
        image = new ImageIcon("MilkyWay.jpg");
        label = new JLabel();
        label.setFont(font);
        label.setForeground(Color.WHITE);
        add(label);
    }

    /**
     * Draws each spaceship onto the screen, and draws the text for the game info.
     * @param page The page onto which all objects are drawn.
     */
    public void paintComponent(Graphics page)
    {
        super.paintComponents(page);

        label.setText(PrintText());

        image.paintIcon(this, page, 0, 0);
        if (GameFrame.timeKeeper.isRunning())
        {
            try
            {
                page.setColor(Color.GREEN);
                Main.Spaceship1.Draw(page);

                page.setColor(Color.CYAN);
                Main.Temp.Draw(page);

                page.setColor(Color.BLACK);
                Main.hole.Draw(page);
                
                if (GameFrame.powerupExists)
                    GameFrame.powerup.Draw(page);
                
                if (GameFrame.healthExists)
                    GameFrame.health.Draw(page);
            }

            catch(NullPointerException e)
            {
                System.out.println(e);
            }
        }
    }

    /**
     * Determines the game info text for the game window.
     * @return
     */
    private String PrintText()
    {
        Spaceship temp;

        if(GameFrame.timeKeeper.isRunning())
        {
            String message;
            if (Main.Spaceship1.Initialized())
                message = Main.Players.get(Main.Spaceship1.getIndex()).getName();
            else
                message = "Player 1";

            message += " Hit Points: " + Main.Spaceship1.getHitPoints() + "    ";

            if (Main.Spaceship1.getPowerupID() == 1)
                message += "Bullet-proof armor";
            else if (Main.Spaceship1.getPowerupID() == 2)
                message += "Super speed";
            else if (Main.Spaceship1.getPowerupID() == 3)
                message += "Antigravity";
            else if (Main.Spaceship1.getPowerupID() == 4)
                message += "Invincibility";
            else
                message += "No powerups";

            if(!Main.ComputerPlayer)
            {
                if (Main.Spaceship2.Initialized())
                    message += "            " + Main.Players.get(Main.Spaceship2.getIndex()).getName();
                else
                    message += "            Player 2";
            }
            else
            {
                message += "            AI Player";
            }

                message += " Hit Points: " + Main.Temp.getHitPoints() + "    ";

                if (Main.Temp.getPowerupID() == 1)
                    message += "Bullet-proof armor";
                else if (Main.Temp.getPowerupID() == 2)
                    message += "Super speed";
                else if (Main.Temp.getPowerupID() == 3)
                    message += "Antigravity";
                else if (Main.Temp.getPowerupID() == 4)
                    message += "Invincibility";
                else
                    message += "No powerups";

                message += "    Timer: " + (((GameFrame.counterPowerup - GameFrame.counter)*GameFrame.Delay)/1000 + 1);

            return message;
        }

        else
            return "";
    }
}
