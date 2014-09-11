
package johnsontcs3finalproject;

/**
 * Creates the sin and cos tables for each integer angle measure.
 * @author timothy
 */
public final class Tables
{
    public static final double sin[] = new double[360];
    public static final double cos[] = new double[360];

    static
    {
        int index;
        for(index = 0; index < 360; index++)
        {
            sin[index] = Math.sin(index*Math.PI/180);
            cos[index] = Math.cos(index*Math.PI/180);
        }
    }

    /**
     * A dummy constructor.
     *
     */
    private Tables() {}
}
