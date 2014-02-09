package lecoselol.clumsyninja;

import android.content.Context;
import android.content.SharedPreferences;

public class PasswordManager {
    private static final String PREFERENCE_NAME = "ThisIsNotThePasswordYouAreLookingFor";
    private static final String PASSWORD_KEY = "NoReallyDudeIAmNotThePassword";
    private static final String PASSWORD_DEFAULT_VALUE = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static String getPassword(Context context) {
        final SharedPreferences prefs = getPreferences(context);
        return prefs.getString(PASSWORD_KEY, PASSWORD_DEFAULT_VALUE);
    }

    /**
     * Checks if the password given in input matches the one stored locally.
     *
     * @param context         I don't know what this is needed for, but it was cool.
     * @param encodedPassword SHA-256 of user inputted password.
     *
     * @return true if passwords match, false if no password has been set or hash in input does not match stored
     * password.
     */
    public static boolean isPasswordReallyTheSameAreYouSure(Context context, String encodedPassword) {
        final String storedPassword = getPassword(context);
        return storedPassword.length() != 0 && storedPassword.equalsIgnoreCase(encodedPassword);
    }

    /**
     * Saves a password inside local storage.
     *
     * @param context         I don't know what this is needed for, but it was cool.
     * @param encodedPassword SHA-256 of user inputted password.
     */
    public static void itWasAboutTimeToSetABloodyPassword(Context context, String encodedPassword) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();

        editor.putString(PASSWORD_KEY, encodedPassword);
        editor.commit();
    }
}
