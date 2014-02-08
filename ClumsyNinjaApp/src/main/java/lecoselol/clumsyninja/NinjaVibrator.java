package lecoselol.clumsyninja;

import android.content.Context;
import android.os.Vibrator;

/**
 * 08/02/14
 * Created by takhion
 */
public class NinjaVibrator
{
    private static Vibrator vibrator; // kinky

    public static void initalize(Context context)
    {
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        if (!vibrator.hasVibrator()) vibrator = null;
    }

    /**
     * You dirty, dirty one.<br>
     * Also, <em>bzzzzzzz</em>.
     */
    public static void vibrate(long duration)
    {
        if (vibrator != null) vibrator.vibrate(duration);
    }
}
