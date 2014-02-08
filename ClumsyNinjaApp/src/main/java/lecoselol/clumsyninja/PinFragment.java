package lecoselol.clumsyninja;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * You're supposed to input your PIN in this thing.
 */
public class PinFragment extends Fragment {

    private TextView mPinText;
    private OnPinChangedListener mListener;
    private String mPin = "";

    public void setListener(OnPinChangedListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pin, container, false);
        PinPadListener l = new PinPadListener();

        // I don't remember the fucking view IDs LOL
        assert rootView != null;     // Debug shit here!
        rootView.findViewById(R.id.txt_one).setOnClickListener(l);
        rootView.findViewById(R.id.txt_due).setOnClickListener(l);
        rootView.findViewById(R.id.txt_troix).setOnClickListener(l);
        rootView.findViewById(R.id.txt_vier).setOnClickListener(l);
        rootView.findViewById(R.id.txt_cinco).setOnClickListener(l);
        rootView.findViewById(R.id.txt_kuusi).setOnClickListener(l);
        rootView.findViewById(R.id.txt_zeven).setOnClickListener(l);
        rootView.findViewById(R.id.txt_oito).setOnClickListener(l);
        rootView.findViewById(R.id.txt_dziewięć).setOnClickListener(l);
        rootView.findViewById(R.id.txt_null).setOnClickListener(l);

        mPinText = (TextView) rootView.findViewById(R.id.txt_pin);
        mPinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNumber();
            }
        });

        return rootView;
    }

    public void addNumber(char num) {
        mPin = mPin + num;
        mPinText.setText(mPin);

        if (mListener != null) {
            mListener.onPinChanged(mPin);
        }
    }

    public void removeNumber() {
        if (!TextUtils.isEmpty(mPin)) {
            mPin = mPin.substring(0, mPin.length() - 1);
            mPinText.setText(mPin);

            if (mListener != null) {
                mListener.onPinChanged(mPin);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPin = "";
        mPinText.setText(mPin);
    }

    public String getPin() {
        return mPin;
    }

    public class PinPadListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // WTF now I'm hating my smartassness
                case R.id.txt_one:
                    addNumber('1');
                    break;
                case R.id.txt_due:
                    addNumber('2');
                    break;
                case R.id.txt_troix:
                    addNumber('3');
                    break;
                case R.id.txt_vier:
                    addNumber('4');
                    break;
                case R.id.txt_cinco:
                    addNumber('5');
                    break;
                case R.id.txt_kuusi:
                    addNumber('6');
                    break;
                case R.id.txt_zeven:
                    addNumber('7');
                    break;
                case R.id.txt_oito:
                    addNumber('8');
                    break;
                case R.id.txt_dziewięć:
                    addNumber('9');
                    break;
                case R.id.txt_null:
                    addNumber('0');
                    break;
                default:
                    addNumber('?');
            }
        }
    }

    /**
     * Listener for PIN changes.
     */
    public interface OnPinChangedListener {
        public void onPinChanged(String currentPin);
    }
}
