package lecoselol.clumsyninja;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public final class SplendidToast
{
    public static void show(Context context, String message)
    {
        Toast toast = new Toast(context);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
