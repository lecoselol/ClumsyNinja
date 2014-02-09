package lecoselol.clumsyninja;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class NinjaApplication extends Application
{
    private static NinjaApplication instance;
    private static String userKey = "";

    public NinjaApplication()
    {
        instance = this;
    }

    public static NinjaApplication getInstance()
    {
        return instance;
    }

    public static void registerUser(String newUserKey)
    {
        userKey = newUserKey;
    }

    public static void unregisterUser()
    {
        userKey = "";
    }

    public static boolean isUserRegistered()
    {
        return (userKey.length() > 0);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Handles screen off
        final ScreenOffReceiver receiver = new ScreenOffReceiver();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        registerReceiver(receiver, filter);
    }

    private static final class ScreenOffReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            unregisterUser();
        }
    }

    public static void playSoundAsync(int resid)
    {
        final MediaPlayer mp = new MediaPlayer();
        AssetFileDescriptor afd = null;
        try
        {
            afd = NinjaApplication.getInstance().getResources().openRawResourceFd(resid);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                if (afd != null) afd.close();
                mp.release();
            }
            catch (Exception ignore)
            {
            }
            return;
        }
        final AssetFileDescriptor _afd = afd;
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer)
            {
                mediaPlayer.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                mediaPlayer.release();
                try
                {
                    _afd.close();
                }
                catch (Exception ignore)
                {
                }
            }
        });
        mp.prepareAsync();
    }
}
