package lecoselol.clumsyninja;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
}
