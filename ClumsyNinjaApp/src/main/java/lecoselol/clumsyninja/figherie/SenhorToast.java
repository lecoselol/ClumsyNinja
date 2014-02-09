package lecoselol.clumsyninja.figherie;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import lecoselol.clumsyninja.R;

/**
 * Stuff the dreams are made of.
 */
public class SenhorToast extends Toast {

    private Context mContext;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     */
    public SenhorToast(Context context) {
        super(context);
        mContext = context;


        final LayoutInflater inflater = LayoutInflater.from(mContext);
        TextView v = (TextView) inflater.inflate(R.layout.senhor_toast, null);
        v.setText(R.string.toast_create_pin);
        setView(v);
    }
}
