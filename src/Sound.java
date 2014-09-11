
package johnsontcs3finalproject;

import java.lang.Thread;
import java.applet.*;
import java.net.*;

/**
 * Plays the sound for each game action.
 * @author timothy
 */
public class Sound extends Thread
{
    String url = "";

    /**
     * Finds the file path for each sound.
     * @param choice The string that determines which sound to play.
     */
    public Sound(String choice)
    {
        if (choice.equals("Bullet"))
            url = "file:Bullet.wav";
        else if (choice.equals("Explosion"))
            url = "file:explosion.wav";
        //else if (choice.equals("Thrust"))
            //url = "file:Thrust.wav";
    }

    /**
     * Plays the chosen sound file.
     *
     */
    public void run()
    {
        super.run();
        
        try
        {
            AudioClip clip = Applet.newAudioClip(new URL(url));
            clip.play();
        }

        catch (MalformedURLException murle)
        {
            System.out.println(murle);
        }
        
        return;
    }
}
