package lecoselol.clumsyninja;

import android.os.AsyncTask;

public abstract class AsyncVerifyPassword extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length == 0) return false;

        final String inputPassword = params[0];

        boolean result = false;
        try {
            final String decryptedPassword = Crypto.SHA256_orYouDie(inputPassword);
            result = PasswordManager.isPasswordReallyTheSameAreYouSure(NinjaApplication.getInstance(),
                                                                       decryptedPassword);
        }
        catch (Exception e) {
            //TODO insert Emoji
            final String errorMessage = NinjaApplication.getInstance().getString(R.string.hash_error_text);
            SplendidToast.show(NinjaApplication.getInstance(), errorMessage);
        }

        return result;
    }
}
